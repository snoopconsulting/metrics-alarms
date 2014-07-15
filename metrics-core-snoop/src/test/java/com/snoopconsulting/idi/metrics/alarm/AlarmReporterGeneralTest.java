package com.snoopconsulting.idi.metrics.alarm;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.snoopconsulting.idi.metrics.alarm.AlarmConfig.PARAMETER;
import com.snoopconsulting.idi.metrics.alarm.AlarmConfig.THRESHOLD;
import com.snoopconsulting.idi.metrics.utils.AlarmResultTest;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.MetricsRegistry;

@RunWith(JUnit4.class)
public class AlarmReporterGeneralTest {

	private MetricsRegistry registry;
	private AlarmReporter reporter;
	private AlarmResultTest alarmResult;
	private Counter counter;

	@Before
	public void init() {
		alarmResult = new AlarmResultTest();
		registry = new MetricsRegistry();
		reporter = new AlarmReporter(registry, MetricPredicate.ALL, alarmResult);
		counter = registry.newCounter(AlarmReporterCounterTest.class,
				"requests");

	}

	@org.junit.Test
	public void testInvalidAlarm() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE, null);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertEquals(0, alarmResult.getSize());
	}

	@org.junit.Test
	public void testInvalidAlarm2() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1, null, PARAMETER.VALUE, null, false);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertEquals(0, alarmResult.getSize());
	}
}
