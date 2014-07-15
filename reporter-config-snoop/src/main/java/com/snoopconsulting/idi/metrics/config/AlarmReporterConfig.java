package com.snoopconsulting.idi.metrics.config;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snoopconsulting.idi.metrics.alarm.AlarmConfig;
import com.snoopconsulting.idi.metrics.alarm.AlarmReporter;
import com.snoopconsulting.idi.metrics.alarm.Alarm;
import com.snoopconsulting.idi.metrics.alarm.util.EmailAlarm;
import com.yammer.metrics.Metrics;

/**
 * The Class AlarmReporterConfig.
 */
public class AlarmReporterConfig extends AbstractReporterConfig {

	/** The Constant log. */
	private static final Logger log = LoggerFactory
			.getLogger(AlarmReporterConfig.class);

	/** The period alarm. */
	@NotNull
	private long periodAlarm = 0;

	/** The timeunit alarm. */
	@Pattern(regexp = "^(DAYS|HOURS|MICROSECONDS|MILLISECONDS|MINUTES|NANOSECONDS|SECONDS)$", message = "must be a valid java.util.concurrent.TimeUnit")
	@NotNull
	private String timeunitAlarm;

	/** The metrics. */
	@Valid
	public static List<AlarmConfigYAML> metrics;

	/** The email. */
	@Valid
	protected EmailYAML email;

	/**
	 * Gets the email.
	 * 
	 * @return the email
	 */
	public EmailYAML getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 * 
	 * @param email
	 *            the new email
	 */
	public void setEmail(EmailYAML email) {
		this.email = email;
	}

	/** The outfile. */
	private String outfile = null;

	/** The alarm interface. */
	private Alarm alarmInterface;

	/** The reporter. */
	private AlarmReporter reporter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.snoopconsulting.idi.metrics.config.AbstractReporterConfig#enable()
	 */
	@Override
	public boolean enable() {
		return enable(true);
	}

	/**
	 * Inicializa el AlarmReporter interno y carga las metricas del archivo en
	 * el AlarmReporter
	 * 
	 * @param initReporter
	 *            indica si de inizializa el start o no (false: solo para test)
	 * @return true, if successful
	 */
	private boolean enable(boolean initReporter) {
		try {
			if (alarmInterface == null) {
				boolean valido = (email != null && email.getMail() != null
						&& email.getPassword() != null
						&& email.getSmtpAddres() != null
						&& email.getSmtpPort() != null
						&& email.getIssue() != null && email.getAddressee() != null);

				if (valido) {
					this.setAlarm(new EmailAlarm(email.getMail(), email
							.getPassword(), email.getSmtpAddres(), email
							.getSmtpPort(), email.isTLS(), email.getIssue(),
							email.getAddressee()));
				} else {
					log.warn("Algunos atributos de EmailAlarm no son validos");
				}
			}

			if (alarmInterface == null) {
				log.error("No hay una alarma configurada en el reporter");
			}

			reporter = new AlarmReporter(Metrics.defaultRegistry(),
					getMetricPredicate(), getAlarm());
			if (initReporter)
				getReporter().start(getPeriod(), getRealTimeunit(),
						periodAlarm, getRealTimeUnitAlarm());

			for (AlarmConfigYAML m : metrics) {
				AlarmConfig p = new AlarmConfig(m.getMetric(),
						m.getThreshold(), m.getValue(), m.getValueMax(),
						m.getParameter(), m.getParameterValue(), m.isNegated());
				if (AlarmReporter.validarParametro(p, null)) {
					AlarmReporter.addAlarmConfig(p);
				}

			}
		} catch (Exception e) {
			log.error("Failure while enabling alarm reporter", e);
			return false;
		}
		return true;
	}

	/**
	 * Gets the real time unit alarm.
	 * 
	 * @return the real time unit alarm
	 */
	private TimeUnit getRealTimeUnitAlarm() {
		return TimeUnit.valueOf(timeunitAlarm);
	}

	/**
	 * Cargar alarmas.
	 * 
	 * @return true, if successful
	 */
	public boolean cargarAlarmas() {
		return enable(false);
	}

	/**
	 * Gets the alarm.
	 * 
	 * @return the alarm
	 */
	private Alarm getAlarm() {
		return alarmInterface;
	}

	/**
	 * Sets the alarm.
	 * 
	 * @param alarm
	 *            the new alarm
	 */
	public void setAlarm(Alarm alarm) {
		this.alarmInterface = alarm;
	}

	/**
	 * Gets the outfile.
	 * 
	 * @return the outfile
	 */
	public String getOutfile() {
		return outfile;
	}

	/**
	 * Sets the outfile.
	 * 
	 * @param outfile
	 *            the new outfile
	 */
	public void setOutfile(String outfile) {
		this.outfile = outfile;
	}

	/**
	 * Gets the reporter.
	 * 
	 * @return the reporter
	 */
	public AlarmReporter getReporter() {
		return reporter;
	}

	/**
	 * Sets the reporter.
	 * 
	 * @param reporter
	 *            the new reporter
	 */
	public void setReporter(AlarmReporter reporter) {
		this.reporter = reporter;
	}

	/**
	 * Gets the period alarm.
	 * 
	 * @return the period alarm
	 */
	public long getPeriodAlarm() {
		return periodAlarm;
	}

	/**
	 * Sets the period alarm.
	 * 
	 * @param periodAlarm
	 *            the new period alarm
	 */
	public void setPeriodAlarm(long periodAlarm) {
		this.periodAlarm = periodAlarm;
	}

	/**
	 * Gets the timeunit alarm.
	 * 
	 * @return the timeunit alarm
	 */
	public String getTimeunitAlarm() {
		return timeunitAlarm;
	}

	/**
	 * Sets the timeunit alarm.
	 * 
	 * @param timeunitAlarm
	 *            the new timeunit alarm
	 */
	public void setTimeunitAlarm(String timeunitAlarm) {
		this.timeunitAlarm = timeunitAlarm;
	}

	/**
	 * Gets the timeunit alarm enum.
	 * 
	 * @return the timeunit alarm enum
	 */
	public TimeUnit getTimeunitAlarmEnum() {
		return TimeUnit.valueOf(timeunitAlarm);
	}

	/**
	 * Gets the alarm interface.
	 * 
	 * @return the alarm interface
	 */
	public Alarm getAlarmInterface() {
		return alarmInterface;
	}

	/**
	 * Sets the alarm interface.
	 * 
	 * @param alarmInterface
	 *            the new alarm interface
	 */
	public void setAlarmInterface(Alarm alarmInterface) {
		this.alarmInterface = alarmInterface;
	}

	/**
	 * Gets the log.
	 * 
	 * @return the log
	 */
	public static Logger getLog() {
		return log;
	}

	/**
	 * Gets the metrics.
	 * 
	 * @return the metrics
	 */
	public List<AlarmConfigYAML> getMetrics() {
		return metrics;
	}

	/**
	 * Sets the metrics.
	 * 
	 * @param metrics
	 *            the new metrics
	 */
	public void setMetrics(List<AlarmConfigYAML> metrics) {
		AlarmReporterConfig.metrics = metrics;
	}

}
