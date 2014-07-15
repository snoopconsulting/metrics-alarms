package com.snoopconsulting.idi.metrics.config.alarm;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.snoopconsulting.idi.metrics.config.ReporterConfig;
import com.snoopconsulting.idi.metrics.config.utils.AlarmResulConfigTest;
import com.snoopconsulting.idi.metrics.config.utils.ReporterConfigTest;

public class AlarmReporterTestCarga extends ReporterConfigTest {
	@Override
	public String getNombreArchivo() {
		return "src/test/resources/sample/alarmReporter.yaml";
	}

	@Test
	public void testCargaAlarma() throws IOException {
		config = ReporterConfig
				.loadFromFile("src/test/resources/sample/alarmReporter.yaml");
		alarmTest = new AlarmResulConfigTest();
		config.getAlarm().iterator().next().setAlarm(alarmTest);
		config.getAlarm().iterator().next().cargarAlarmas();
		alarmReporter = config.getAlarm().get(0).getReporter();
		assertEquals(1, config.getAlarm().size());
		assertEquals(3, config.getAlarm().iterator().next().getMetrics().size());
	}
}
