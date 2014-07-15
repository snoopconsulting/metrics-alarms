package com.snoopconsulting.idi.metrics.config.utils;

import com.snoopconsulting.idi.metrics.alarm.Alarm;
import com.snoopconsulting.idi.metrics.alarm.AlarmConfig;
import com.yammer.metrics.core.Metric;

public class AlarmResulConfigTest extends Alarm {
	private final Boolean[] resultados;
	private final String[] tipos;
	private int size;

	public AlarmResulConfigTest() {
		resultados = new Boolean[100];
		tipos = new String[100];
		size = 0;
	}

	@Override
	public void checkMetric(boolean resultado, Metric metric,
			Double valorActual, AlarmConfig alarmConfig) {
		resultados[size] = resultado;
		tipos[size] = metric.getClass().getSimpleName().toLowerCase();
		size = size + 1;
	}

	public Boolean[] getResultados() {
		return resultados;
	}

	public String[] getTipos() {
		return tipos;
	}

	public int getSize() {
		return size;
	}
}
