package com.snoopconsulting.idi.metrics.alarm;

/**
 * The Class AlarmConfig. It is a configuration of an alarm, in which all
 * parameters are encuentrans lamisma settings. Be careful that there may be
 * invalid combinations. To see more details of valid combinations see
 * README.txt
 */
public class AlarmConfig {

    /**
     * Alarm Type
     */
    public enum THRESHOLD {

        /**
         * The above.
         */
        ABOVE, /**
         * The below.
         */
        BELOW, /**
         * The range.
         */
        RANGE
    }

    /**
     * Metric Type, on which the alarm applies
     */
    enum METRIC_TYPE {

        GAUGE, /**
         * The counter.
         */
        COUNTER, /**
         * The meter.
         */
        METER, /**
         * The histogram.
         */
        HISTOGRAM, /**
         * The timer.
         */
        TIMER
    }

    /**
     * Metric Type, on Which Applies the alarm, you must be careful as there may
     * be invalid combinations
     */
    public enum PARAMETER {

        /**
         * The count.
         */
        COUNT, /**
         * The value.
         */
        VALUE, /**
         * The min.
         */
        MIN, /**
         * The max.
         */
        MAX, /**
         * The mean.
         */
        MEAN, /**
         * The stddev.
         */
        STDDEV, /**
         * The median.
         */
        MEDIAN, /**
         * The percentile.
         */
        PERCENTILE, /**
         * The minute.
         */
        MINUTE,
    }

    /**
     * The metric name.
     */
    private String metricName;

    /**
     * The threshold.
     */
    private THRESHOLD threshold;

    /**
     * The value.
     */
    private Double value;

    /**
     * The parameter.
     */
    private PARAMETER parameter;

    /**
     * The parameter value.
     */
    private Double parameterValue;

    /**
     * The value max.
     */
    private Double valueMax; // Solo valido para rango

    /**
     * The negated.
     */
    private boolean negated = false;

    /**
     * Instantiates a new alarm config.
     *
     * @param metricName Name of the metric to which it applies. (Required)
     * @param threshold The Threshold can see ABOVE, BELOW, or RANGE (Required)
     * @param valueMin Alarm value (Required)
     * @param valueMax Maximum alarm value (only valid for THRESHOLD.RANGUE)
     * @param parameter Parameter of the metric is analyzed, if not specific is
     * calculated on the default parameter
     * @param parameterValue Parameter value on which it is applied, it is only
     * valid for PARAMETER.MINUTE or PARAMETER.PERCENTILE If the parameter is
     * minute be setup values ​​of 1, 5, 15, if the parameter is percentiles can
     * be any real value setear
     * @param negated Indicates whether the condition of the metric is denied or
     * not
     */
    public AlarmConfig(String metricName, THRESHOLD threshold, Double valueMin,
            Double valueMax, PARAMETER parameter, Double parameterValue,
            boolean negated) {
        super();
        this.metricName = metricName;
        this.threshold = threshold;
        this.value = valueMin;
        this.parameter = parameter;
        this.parameterValue = parameterValue;
        this.valueMax = valueMax;
        this.negated = negated;
    }

    /**
     * Instantiates a new alarm config.
     */
    public AlarmConfig() {
    }

    /**
     * Instantiates a new alarm config.
     *
     * @param metricName Name of the metric to which it applies. (Required)
     * @param threshold The Threshold can see ABOVE, BELOW, or RANGE (Required)
     * @param value Alarm value (Required)
     */
    public AlarmConfig(String metricName, THRESHOLD threshold, Double value) {
        this(metricName, threshold, value, null, null, null, false);
    }

    /**
     * Instantiates a new alarm config.
     *
     * @param metricName Name of the metric to which it applies. (Required)
     * @param threshold The Threshold can see ABOVE, BELOW, or RANGE (Required)
     * @param value Alarm value (Required)
     * @param negated Indicates whether the condition of the metric is denied or
     * not
     */
    public AlarmConfig(String metricName, THRESHOLD threshold, Double value,
            boolean negated) {
        this(metricName, threshold, value, null, null, null, negated);
    }

    /**
     * Instantiates a new alarm config. Solo valido para rango
     *
     * @param metricName Name of the metric to which it applies. (Required)
     * @param threshold The Threshold can see ABOVE, BELOW, or RANGE (Required)
     * @param valueMin Minimum value of the alarm (Required)
     * @param valueMax Maximum value alarm (Only valid for THRESHOLD.RANGUE)
     */
    public AlarmConfig(String metricName, THRESHOLD threshold, Double valueMin,
            Double valueMax) {
        this(metricName, threshold, valueMin, valueMax, null, null, false);
    }

    /**
     * Instantiates a new alarm config. Solo valido para rango
     *
     * @param metricName Name of the metric to which it applies. (Required)
     * @param threshold The Threshold can see ABOVE, BELOW, or RANGE (Required)
     * @param valueMin Minimum value of the alarm (Required)
     * @param valueMax Maximum value alarm (Only valid for THRESHOLD.RANGUE)
     * @param negated Indicates whether the condition of the metric is denied or
     * not
     */
    public AlarmConfig(String metricName, THRESHOLD threshold, Double valueMin,
            Double valueMax, boolean negated) {
        this(metricName, threshold, valueMin, valueMax, null, null, negated);
    }

    /**
     * Gets the metric name.
     *
     * @return the metric name
     */
    public String getMetricName() {
        return metricName;
    }

    /**
     * Sets the metric name.
     *
     * @param metricName the new metric name
     */
    public void setMetricName(String metricName) {
        this.metricName = metricName;
    }

    /**
     * Gets the threshold.
     *
     * @return the threshold
     */
    public THRESHOLD getThreshold() {
        return threshold;
    }

    /**
     * Sets the threshold.
     *
     * @param threshold the new threshold
     */
    public void setThreshold(THRESHOLD threshold) {
        this.threshold = threshold;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public PARAMETER getParameter() {
        return parameter;
    }

    /**
     * Sets the parameter.
     *
     * @param parameter the new parameter
     */
    public void setParameter(PARAMETER parameter) {
        this.parameter = parameter;
    }

    /**
     * Gets the parameter value.
     *
     * @return the parameter value
     */
    public Double getParameterValue() {
        return parameterValue;
    }

    /**
     * Sets the parameter value.
     *
     * @param parameterValue the new parameter value
     */
    public void setParameterValue(Double parameterValue) {
        this.parameterValue = parameterValue;
    }

    /**
     * Gets the value max.
     *
     * @return the value max
     */
    public Double getValueMax() {
        return valueMax;
    }

    /**
     * Sets the value max.
     *
     * @param valueMax the new value max
     */
    public void setValueMax(Double valueMax) {
        this.valueMax = valueMax;
    }

    /**
     * Checks if is negated.
     *
     * @return true, if is negated
     */
    public boolean isNegated() {
        return negated;
    }

    /**
     * Sets the negated.
     *
     * @param negated the new negated
     */
    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AlarmConfig [metricName=" + metricName + ", threshold="
                + threshold + ", value=" + value + ", parameter=" + parameter
                + ", parameterValue=" + parameterValue + ", valueMax="
                + valueMax + ", negated=" + negated + "]";
    }

}
