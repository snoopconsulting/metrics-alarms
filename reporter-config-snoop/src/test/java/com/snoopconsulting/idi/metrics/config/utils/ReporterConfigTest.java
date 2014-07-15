package com.snoopconsulting.idi.metrics.config.utils;

import org.junit.After;
import org.junit.Before;

import com.snoopconsulting.idi.metrics.alarm.AlarmReporter;
import com.snoopconsulting.idi.metrics.config.ReporterConfig;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

public abstract class ReporterConfigTest {
	public ReporterConfig config;
	public AlarmResulConfigTest alarmTest;
	public AlarmReporter alarmReporter;

	@Before
	public void init() throws Exception {
		config = ReporterConfig.loadFromFile(this.getNombreArchivo());
		alarmTest = new AlarmResulConfigTest();
		config.setAlarm(alarmTest);
		config.getAlarm().iterator().next().cargarAlarmas();
		alarmReporter = config.getAlarm().get(0).getReporter();
	}

	@After
	public void after() throws Exception {
		MetricsRegistry dr = Metrics.defaultRegistry();
		dr.allMetrics().keySet();
		for (MetricName m : dr.allMetrics().keySet()) {
			dr.removeMetric(m);
		}
	}

	public abstract String getNombreArchivo();
}
