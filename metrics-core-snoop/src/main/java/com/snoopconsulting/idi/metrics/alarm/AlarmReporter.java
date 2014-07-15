package com.snoopconsulting.idi.metrics.alarm;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Clock;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.Histogram;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.Metered;
import com.yammer.metrics.core.Metric;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.MetricProcessor;
import com.yammer.metrics.core.MetricsRegistry;
import com.yammer.metrics.core.Timer;
import com.yammer.metrics.reporting.AbstractPollingReporter;

/**
 * The Class AlarmReporter. Responsible to receive event, and process alarms
 */
public class AlarmReporter extends AbstractPollingReporter implements
        MetricProcessor<PrintStream> {

    /**
     * The Enum CALL. Used internally to identify the type of event that
     * occurred.
     */
    private enum CALL {

        /**
         * Indicates to run the check for metric
         */
        METRIC,
        /**
         * Indicates to run the alarm
         */
        ALARM
    }

    /**
     * Indicates whether an error of that configuration already logging. To
     * avoid filling the log
     */
    private static Set<AlarmConfig> seLogueo = new HashSet<AlarmConfig>();

    /**
     * The Constant log.
     */
    private static final Logger log = LoggerFactory
            .getLogger(AlarmReporter.class);

    /**
     * Stored alarms (String = name of the metric, Collect configurations)
     */
    private static HashMap<String, Collection<AlarmConfig>> alarmas;

    /**
     * The predicate.
     */
    private final MetricPredicate predicate;

    /**
     * The alarm.
     */
    private Alarm alarm;

    private java.util.Timer timerAlarm;

    /**
     * Responsible for executing the task alarmTimer according the specified
     * time, the time Sets the start ()
     */
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            synchronizedMethod(CALL.ALARM, false, null, null, null);
        }
    };

    /**
     * Get events and calls checkMetric alarmTimer or according to the
     * appropriate parties. It is necessary that there is a synchronized so that
     * there are no conflicts between the events and the alarm timer
     *
     * @param metodo Used internally to call Indicates whether alarmTimer() or
     * checkMetric()
     * @param resultado Indicates whether the alert is active or not (Only valid
     * when calling checkMetric())
     * @param metric The metric analysis (only valid when calling checkMetric()
     * @param valorActual Current value of the parameter that was analyzed (Only
     * valid when calling checkMetric())
     * @param alarmConfig Configuration of the alarm to run (Only valid when
     * calling checkMetric())
     */
    private synchronized void synchronizedMethod(CALL metodo,
            boolean resultado, Metric metric, Double valorActual,
            AlarmConfig alarmConfig) {
        if (metodo.equals(CALL.ALARM)) {
            alarm.alarmTimer();
        } else if (metodo.equals(CALL.METRIC)) {
            alarm.checkMetric(resultado, metric, valorActual, alarmConfig);
        }
    }

    /**
     * Inicializa un AlarmReporter
     *
     * @param alarm Alarm used
     * @param period period in which the metric is checked
     * @param unit Each time the metric is chekea
     * @param periodAlarm period in which the alarm is checked
     * @param unitAlarm Each time the alarm is analyzed
     */
    public static void enable(Alarm alarm, long period, TimeUnit unit,
            long periodAlarm, TimeUnit unitAlarm) {
        enable(alarm, Metrics.defaultRegistry(), period, unit, periodAlarm,
                unitAlarm);
    }

    /**
     * Enable.
     *
     * @param alarm Alarm used
     * @param metricsRegistry the metrics registry
     * @param period Each time the alarm is analyzed
     * @param unit Each time that the metrics are analyzed
     * @param periodAlarm period in which the alarm is checked
     * @param unitAlarm Each time the alarm is analyzed
     */
    public static void enable(Alarm alarm, MetricsRegistry metricsRegistry,
            long period, TimeUnit unit, long periodAlarm, TimeUnit unitAlarm) {
        final AlarmReporter reporter = new AlarmReporter(metricsRegistry,
                MetricPredicate.ALL, alarm);
        reporter.start(period, unit, periodAlarm, unitAlarm);
    }

    /**
     * Creates a new {@link AlarmReporter} for the default metrics registry,
     * with unrestricted output.
     *
     * @param alarm the alarm
     */
    public AlarmReporter(Alarm alarm) {
        this(Metrics.defaultRegistry(), MetricPredicate.ALL, alarm);
    }

    /**
     * Creates a new {@link AlarmReporter} for a given metrics registry.
     *
     * @param metricsRegistry the metrics registry
     * @param predicate the {@link MetricPredicate} used to determine whether a
     * metric will be output
     * @param alarm the alarm
     */
    public AlarmReporter(MetricsRegistry metricsRegistry,
            MetricPredicate predicate, Alarm alarm) {
        this(metricsRegistry, predicate, Clock.defaultClock(), TimeZone
                .getDefault(), alarm);
    }

    /**
     * Creates a new {@link AlarmReporter} for a given metrics registry.
     *
     * @param metricsRegistry the metrics registry
     * @param predicate the {@link MetricPredicate} used to determine whether a
     * metric will be output
     * @param clock the {@link Clock} used to print time
     * @param timeZone the {@link TimeZone} used to print time
     * @param alarm the alarm
     */
    public AlarmReporter(MetricsRegistry metricsRegistry,
            MetricPredicate predicate, Clock clock, TimeZone timeZone,
            Alarm alarm) {
        this(metricsRegistry, predicate, clock, timeZone, Locale.getDefault(),
                alarm);
    }

    /**
     * Creates a new {@link AlarmReporter} for a given metrics registry.
     *
     * @param metricsRegistry the metrics registry
     * @param predicate the {@link MetricPredicate} used to determine whether a
     * metric will be output
     * @param clock the {@link com.yammer.metrics.core.Clock} used to print time
     * @param timeZone the {@link TimeZone} used to print time
     * @param locale the {@link Locale} used to print values
     * @param alarm the alarm
     */
    public AlarmReporter(MetricsRegistry metricsRegistry,
            MetricPredicate predicate, Clock clock, TimeZone timeZone,
            Locale locale, Alarm alarm) {
        super(metricsRegistry, "alarm-reporter");
        this.predicate = predicate;
        this.setAlarm(alarm);
        alarmas = new HashMap<String, Collection<AlarmConfig>>();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.yammer.metrics.reporting.AbstractPollingReporter#run()
     */
    @Override
    public void run() {
        try {
            for (Entry<String, SortedMap<MetricName, Metric>> entry : getMetricsRegistry()
                    .groupedMetrics(predicate).entrySet()) {
                for (Entry<MetricName, Metric> subEntry : entry.getValue()
                        .entrySet()) {
                    subEntry.getValue().processWith(this, subEntry.getKey(),
                            System.out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yammer.metrics.core.MetricProcessor#processGauge(com.yammer.metrics
     * .core.MetricName, com.yammer.metrics.core.Gauge, java.lang.Object)
     */
    @Override
    public void processGauge(MetricName name, Gauge<?> gauge, PrintStream stream) {
        chekearAlarma(name.getName(), gauge, AlarmConfig.METRIC_TYPE.GAUGE,
                alarmas.get(name.getName().toLowerCase()));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yammer.metrics.core.MetricProcessor#processCounter(com.yammer.metrics
     * .core.MetricName, com.yammer.metrics.core.Counter, java.lang.Object)
     */
    @Override
    public void processCounter(MetricName name, Counter counter,
            PrintStream stream) {
        String nombre = name.getName().toLowerCase();
        chekearAlarma(nombre, counter, AlarmConfig.METRIC_TYPE.COUNTER,
                alarmas.get(nombre));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yammer.metrics.core.MetricProcessor#processMeter(com.yammer.metrics
     * .core.MetricName, com.yammer.metrics.core.Metered, java.lang.Object)
     */
    @Override
    public void processMeter(MetricName name, Metered meter, PrintStream stream) {
        chekearAlarma(name.getName(), meter, AlarmConfig.METRIC_TYPE.METER,
                alarmas.get(name.getName().toLowerCase()));

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yammer.metrics.core.MetricProcessor#processHistogram(com.yammer.metrics
     * .core.MetricName, com.yammer.metrics.core.Histogram, java.lang.Object)
     */
    @Override
    public void processHistogram(MetricName name, Histogram histogram,
            PrintStream stream) {
        chekearAlarma(name.getName(), histogram,
                AlarmConfig.METRIC_TYPE.HISTOGRAM,
                alarmas.get(name.getName().toLowerCase()));

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.yammer.metrics.core.MetricProcessor#processTimer(com.yammer.metrics
     * .core.MetricName, com.yammer.metrics.core.Timer, java.lang.Object)
     */
    @Override
    public void processTimer(MetricName name, Timer timer, PrintStream stream) {
        chekearAlarma(name.getName(), timer, AlarmConfig.METRIC_TYPE.TIMER,
                alarmas.get(name.getName().toLowerCase()));
    }

    /**
     * Metodo encargado de chekear la alarma. La misma es validada de acuerdo al
     * tipo de metrica(ver Documentacion)
     *
     * @param key El nombre de la metrica
     * @param value La metrica
     * @param metric_type El tipo de metrica que se analiza
     * @param alarmas alarmas para la metrica a analizar
     */
    private void chekearAlarma(String key, Metric value,
            AlarmConfig.METRIC_TYPE metric_type, Collection<AlarmConfig> alarmas) {
        if (alarmas != null) {

            for (AlarmConfig p : alarmas) {
                if (AlarmReporter.validarParametro(p, metric_type)) {
                    AlarmConfig.THRESHOLD tipoOperacion = p.getThreshold();
                    Double valorActual = null;
                    switch (metric_type) {
                        case GAUGE:
                            valorActual = Double.valueOf(((Gauge) value).value()
                                    .toString());
                            break;
                        case COUNTER:
                            valorActual = Double.valueOf(((Counter) value).count());
                            break;
                        case METER: {
                            Meter m = (Meter) value;
                            switch (p.getParameter()) {
                                case COUNT:
                                    valorActual = Double.valueOf(m.count());
                                    break;
                                case MEAN:
                                    valorActual = m.meanRate();
                                    break;
                                case MINUTE:
                                    if (p.getParameterValue().equals(1)) {
                                        valorActual = m.oneMinuteRate();
                                    } else if (p.getParameterValue().equals(5)) {
                                        valorActual = m.fiveMinuteRate();
                                    } else if (p.getParameterValue().equals(15)) {
                                        valorActual = m.fifteenMinuteRate();
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                        case HISTOGRAM: {
                            Histogram m = (Histogram) value;
                            switch (p.getParameter()) {
                                case COUNT:
                                    valorActual = Double.valueOf(m.count());
                                    break;
                                case MIN:
                                    valorActual = Double.valueOf(m.min());
                                    break;
                                case MAX:
                                    valorActual = Double.valueOf(m.max());
                                    break;
                                case MEAN:
                                    valorActual = m.mean();
                                    break;
                                case STDDEV:
                                    valorActual = m.stdDev();
                                    break;
                                case MEDIAN:
                                    valorActual = m.getSnapshot().getMedian();
                                    break;
                                case PERCENTILE:
                                    valorActual = m.getSnapshot().getValue(
                                            p.getParameterValue() / 100);
                                    break;
                                default:
                                    break;
                            }
                        }
                        break;
                        case TIMER:
                            Timer m = (Timer) value;
                            switch (p.getParameter()) {
                                case COUNT:
                                    valorActual = Double.valueOf(m.count());
                                    break;
                                case MIN:
                                    valorActual = Double.valueOf(m.min());
                                    break;
                                case MAX:
                                    valorActual = Double.valueOf(m.max());
                                    break;
                                case MEAN:
                                    valorActual = m.mean();
                                    break;
                                case STDDEV:
                                    valorActual = m.stdDev();
                                    break;
                                case MEDIAN:
                                    valorActual = m.getSnapshot().getMedian();
                                    break;
                                case PERCENTILE:
                                    valorActual = m.getSnapshot().getValue(
                                            p.getParameterValue() / 100);
                                    break;
                                case MINUTE:
                                    if (p.getParameterValue().equals(1)) {
                                        valorActual = m.oneMinuteRate();
                                    } else if (p.getParameterValue().equals(5)) {
                                        valorActual = m.fiveMinuteRate();
                                    } else if (p.getParameterValue().equals(15)) {
                                        valorActual = m.fifteenMinuteRate();
                                    }
                                    break;
                                default:
                                    break;
                            }

                    }
                    boolean resultado = false;
                    switch (tipoOperacion) {
                        case ABOVE:
                            if (valorActual.compareTo(p.getValue()) > 0) {
                                resultado = true;
                            }
                            break;
                        case BELOW:
                            if (valorActual.compareTo(p.getValue()) < 0) {
                                resultado = true;
                            }
                            break;
                        case RANGE:
                            if ((valorActual.compareTo(p.getValue()) >= 0)
                                    && ((valorActual.compareTo(p.getValueMax()) <= 0))) {
                                resultado = true;
                            }
                            break;
                    }

                    if (p.isNegated()) {
                        resultado = !resultado;
                    }

                    synchronizedMethod(CALL.METRIC, resultado, value,
                            valorActual, p);
                }
            }
        }

    }

    /**
     * Agregar alarma.
     *
     * @param p the p
     */
    public static void addAlarmConfig(AlarmConfig p) {
        if (alarmas == null) {
            alarmas = new HashMap<String, Collection<AlarmConfig>>();
        }

        if (!alarmas.containsKey(p.getMetricName().toLowerCase())) {
            alarmas.put(p.getMetricName().toLowerCase(),
                    new ArrayList<AlarmConfig>());
        }

        Collection<AlarmConfig> col = alarmas.get(p.getMetricName()
                .toLowerCase());
        col.add(p);
    }

    /**
     * Gets the metric registry.
     *
     * @return the metric registry
     */
    public MetricsRegistry getMetricRegistry() {
        return getMetricsRegistry();
    }

    /**
     * Method responsible for analyzing whether the alarm setting is valid. The
     * kind of metrics is optional If it is invalid logs warn level
     *
     * @param alarm The alert will analyze
     * @param metric_type Metric Type the alert applies
     * @return true, if successful
     */
    public static boolean validarParametro(AlarmConfig alarm,
            AlarmConfig.METRIC_TYPE metric_type) {
        boolean esValido = (alarm.getThreshold() != null) && (alarm.getValue() != null);
        if (alarm.getThreshold() == AlarmConfig.THRESHOLD.RANGE) {
            esValido = esValido
                    && (alarm.getValue() != null && alarm.getValueMax() != null);
        }
        if (metric_type != null) {
            switch (metric_type) {
                case GAUGE:
                    if (alarm.getParameter() == null) {
                        alarm.setParameter(AlarmConfig.PARAMETER.VALUE);
                    } else if (!alarm.getParameter().equals(AlarmConfig.PARAMETER.VALUE)) {
                        esValido = false;
                    }
                    break;
                case COUNTER:
                    if (alarm.getParameter() == null) {
                        alarm.setParameter(AlarmConfig.PARAMETER.COUNT);
                    } else if (!alarm.getParameter().equals(AlarmConfig.PARAMETER.COUNT)) {
                        esValido = false;
                    }
                    break;
                case METER:
                    if (alarm.getParameter() == null) {
                        alarm.setParameter(AlarmConfig.PARAMETER.COUNT);
                    }
                    esValido = esValido
                            && (alarm.getParameter() == AlarmConfig.PARAMETER.MINUTE
                            || alarm.getParameter() == AlarmConfig.PARAMETER.MEAN || alarm
                            .getParameter() == AlarmConfig.PARAMETER.COUNT);
                    if (alarm.getParameter() == AlarmConfig.PARAMETER.MINUTE) {
                        esValido = esValido && (validarMinutos(alarm));
                    } else {
                        alarm.setParameterValue(null);
                    }
                    break;
                case HISTOGRAM:
                    if (alarm.getParameter() == null) {
                        alarm.setParameter(AlarmConfig.PARAMETER.COUNT);
                    }
                    esValido = esValido
                            && (alarm.getParameter() != AlarmConfig.PARAMETER.VALUE && alarm
                            .getParameter() != AlarmConfig.PARAMETER.MINUTE);

                    if (alarm.getParameter() == AlarmConfig.PARAMETER.PERCENTILE) {
                        esValido = esValido && alarm.getParameterValue() != null;
                    } else {
                        alarm.setParameterValue(null);
                    }
                    break;
                case TIMER:
                    if (alarm.getParameter() == null) {
                        alarm.setParameter(AlarmConfig.PARAMETER.COUNT);
                    }
                    esValido = esValido
                            && (alarm.getParameter() != AlarmConfig.PARAMETER.VALUE);

                    if (alarm.getParameter() == AlarmConfig.PARAMETER.PERCENTILE) {
                        esValido = esValido && alarm.getParameterValue() != null;
                    } else if (alarm.getParameter() == AlarmConfig.PARAMETER.MINUTE) {
                        esValido = esValido && (validarMinutos(alarm));
                    } else {
                        alarm.setParameterValue(null);
                    }
                    break;
            }
        }
        if (!esValido) {
            if (!seLogueo.contains(alarm)) {
                log.warn("La alarma " + alarm + " no es valida, se ignora");
                seLogueo.add(alarm);
            }
        }
        return esValido;
    }

    /**
     * Validates that the minutes are valid.
     *
     * @param p the p
     * @return true, if successful
     */
    private static boolean validarMinutos(AlarmConfig p) {
        return p.getValue().equals(Double.valueOf(1))
                || p.getValue().equals(Double.valueOf(5))
                || p.getValue().equals(Double.valueOf(15));
    }

    /**
     * Convertir string a double.
     *
     * @param s the s
     * @return the double
     */
    private static Double convertirStringDouble(String s) {
        return Double.parseDouble(s.replaceAll("\\,", "\\."));
    }

    /**
     * Gets the alarm.
     *
     * @return the alarm
     */
    public Alarm getAlarm() {
        return alarm;
    }

    /**
     * Sets the alarm.
     *
     * @param alarm the new alarm
     */
    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    /**
     * It starts working the reporter, this means that events with the specified
     * period are analyzed and alarms are configured activarans
     *
     * @param period period in which the metric is checked
     * @param unit Each time the metric is chekea
     * @param periodAlarm Period in which the alarm is checked
     * @param unitAlarm Each time the alarm is analyzed
     */
    public void start(long period, TimeUnit unit, long periodAlarm,
            TimeUnit unitAlarm) {
        super.start(period, unit);

        if (timerAlarm == null) {
            timerAlarm = new java.util.Timer();
            timerAlarm.scheduleAtFixedRate(timerTask, 0,
                    TimeUnit.MILLISECONDS.convert(periodAlarm, unitAlarm));
        }
    }

}
