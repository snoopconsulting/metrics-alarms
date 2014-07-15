package com.snoopconsulting.idi.metrics.config.alarm;

import org.junit.Test;

import com.snoopconsulting.idi.metrics.config.utils.ReporterConfigTest;

public class AlarmReporterConfigParameterTest extends ReporterConfigTest{
	@Override
	public String getNombreArchivo() {
		return "src/test/resources/sample/alarmReporter-parameter.yaml";
	}

	@Test
	public void alarmaNoExiste() throws Exception {
		System.out.println(this.config.getAlarm().iterator().next().getMetrics().iterator().next());
	}
}
