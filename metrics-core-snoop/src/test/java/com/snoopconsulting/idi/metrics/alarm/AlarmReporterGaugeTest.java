package com.snoopconsulting.idi.metrics.alarm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

import com.snoopconsulting.idi.metrics.alarm.AlarmConfig.THRESHOLD;
import com.snoopconsulting.idi.metrics.utils.AlarmResultTest;
import com.yammer.metrics.core.Gauge;
import com.yammer.metrics.core.MetricName;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.MetricsRegistry;

public class AlarmReporterGaugeTest {

	private MetricsRegistry registry;
	private AlarmReporter reporter;
	private AlarmResultTest alarmResult;
	private Integer value = 0;
	private final Gauge<Integer> gauge = new Gauge<Integer>() {
		@Override
		public Integer value() {
			return value;
		}
	};

	@Before
	public void init() {
		alarmResult = new AlarmResultTest();
		registry = new MetricsRegistry();
		reporter = new AlarmReporter(registry, MetricPredicate.ALL, alarmResult);
		registry.newGauge(new MetricName(AlarmReporter.class, "request"), gauge);
	}

	@org.junit.Test
	public void testCounterMaxFalse() {
		AlarmConfig alarm = new AlarmConfig("request", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		value = 0;
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxFalse2() {
		AlarmConfig alarm = new AlarmConfig("request", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		value = 1;
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxTrue() {
		AlarmConfig alarm = new AlarmConfig("request", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		value = 2;
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

}
