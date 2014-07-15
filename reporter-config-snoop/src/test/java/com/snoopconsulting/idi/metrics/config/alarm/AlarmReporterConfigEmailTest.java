package com.snoopconsulting.idi.metrics.config.alarm;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.snoopconsulting.idi.metrics.alarm.AlarmReporter;
import com.snoopconsulting.idi.metrics.alarm.util.EmailAlarm;
import com.snoopconsulting.idi.metrics.config.ReporterConfig;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricsRegistry;

@RunWith(JUnit4.class)
public class AlarmReporterConfigEmailTest {
	private ReporterConfig config;
	private AlarmReporter alarmReporter;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@After
	public void after() throws Exception {
		MetricsRegistry dr = Metrics.defaultRegistry();
		dr.allMetrics().keySet();
		for (MetricName m : dr.allMetrics().keySet()) {
			dr.removeMetric(m);
		}
	}

	@Test
	public void alarmaNoExiste() throws Exception {
		config = ReporterConfig
				.loadFromFile("src/test/resources/sample/alarmReporter-email.yaml");
		config.getAlarm().iterator().next().cargarAlarmas();
		alarmReporter = config.getAlarm().get(0).getReporter();
		EmailAlarm alarmTest = (EmailAlarm) alarmReporter.getAlarm();
		assertEquals("nicolas.just@snoopconsulting.com", alarmTest.getMail());
		assertEquals("pepepe", alarmTest.getPassword());
		assertEquals("smtp.gmail.com", alarmTest.getSmtpAddres());
		assertEquals(587, alarmTest.getSmtpPort());
		assertEquals(true, alarmTest.isTLS());
		assertEquals("Alarma de Metric", alarmTest.getIssue());
		assertEquals("nicolas.just@snoopconsulting.com",
				alarmTest.getAddressee());
	}

	@Test
	public void emailInvalid() throws Exception {
		thrown.expect(ReporterConfig.ReporterConfigurationException.class);
		ReporterConfig config = ReporterConfig
				.loadFromFileAndValidate("src/test/resources/invalid/alarmReporter-emailInvalid.yaml");
	}
}
