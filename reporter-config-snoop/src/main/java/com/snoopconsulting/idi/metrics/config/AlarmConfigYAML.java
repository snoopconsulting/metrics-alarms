package com.snoopconsulting.idi.metrics.config;

import javax.validation.constraints.NotNull;

import com.snoopconsulting.idi.metrics.alarm.AlarmConfig;
import com.snoopconsulting.idi.metrics.alarm.AlarmConfig.PARAMETER;
import com.snoopconsulting.idi.metrics.alarm.AlarmConfig.THRESHOLD;

public class AlarmConfigYAML {

	@NotNull
	private String metric;
	@NotNull
	private AlarmConfig.THRESHOLD threshold;
	@NotNull
	private Double value;
	private AlarmConfig.PARAMETER parameter;
	private Double parameterValue;
	private Double valueMax; // Solo valido para rango
	private boolean negated = false;

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public THRESHOLD getThreshold() {
		return threshold;
	}

	public void setThreshold(THRESHOLD type) {
		this.threshold = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public PARAMETER getParameter() {
		return parameter;
	}

	public void setParameter(PARAMETER parameter) {
		this.parameter = parameter;
	}

	public Double getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Double parameterValue) {
		this.parameterValue = parameterValue;
	}

	public Double getValueMax() {
		return valueMax;
	}

	public void setValueMax(Double valueMax) {
		this.valueMax = valueMax;
	}

	public boolean isNegated() {
		return negated;
	}

	public void setNegated(boolean negated) {
		this.negated = negated;
	}

	public Double getValueMin() {
		return value;
	}

	public void setValueMin(Double valueMin) {
		this.value = valueMin;
	}

}
