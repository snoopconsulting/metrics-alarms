package com.snoopconsulting.idi.metrics.alarm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.snoopconsulting.idi.metrics.alarm.AlarmConfig.THRESHOLD;
import com.snoopconsulting.idi.metrics.utils.AlarmResultTest;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.Meter;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.MetricsRegistry;

@RunWith(JUnit4.class)
public class AlarmReporteMultipleAlarmTest {

	MetricsRegistry registry;
	AlarmReporter reporter;

	AlarmResultTest alarmResult;
	Counter counter;

	/*
	 * en esta version los elementos no se procesan en orden, REVISARRRRR pueden
	 * fallar
	 */
	@Before
	public void init() {
		alarmResult = new AlarmResultTest();
		registry = new MetricsRegistry();
		reporter = new AlarmReporter(registry, MetricPredicate.ALL, alarmResult);
		counter = registry.newCounter(AlarmReporterCounterTest.class,
				"requests");
	}

	@Test
	public void TestMultiplesAlarm1() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);

		alarm = new AlarmConfig("requests", THRESHOLD.ABOVE, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);

		reporter.run();
		assertTrue(alarmResult.getSize() == 2);
		assertTrue(alarmResult.getResultados()[0]);
		assertEquals("counter", alarmResult.getTipos()[0]);

		assertFalse(alarmResult.getResultados()[1]);
		assertEquals("counter", alarmResult.getTipos()[1]);
	}

	@Test
	public void TestMultiplesAlarm2() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);

		alarm = new AlarmConfig("requests", THRESHOLD.ABOVE, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);

		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 2);
		assertFalse(alarmResult.getResultados()[0]);
		assertEquals("counter", alarmResult.getTipos()[0]);

		assertFalse(alarmResult.getResultados()[1]);
		assertEquals("counter", alarmResult.getTipos()[1]);
	}

	@Test
	public void TestMultiplesAlarm3() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);

		alarm = new AlarmConfig("requests", THRESHOLD.ABOVE, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		reporter.run();

		assertTrue(alarmResult.getSize() == 2);
		assertFalse(alarmResult.getResultados()[0]);
		assertEquals("counter", alarmResult.getTipos()[0]);

		assertFalse(alarmResult.getResultados()[1]);
		assertEquals("counter", alarmResult.getTipos()[1]);
	}

	@Test
	public void TestMultiplesAlarm4() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);

		alarm = new AlarmConfig("requests", THRESHOLD.ABOVE, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);

		counter.inc();
		counter.inc();
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 2);
		assertFalse(alarmResult.getResultados()[0]);
		assertEquals("counter", alarmResult.getTipos()[0]);

		assertFalse(alarmResult.getResultados()[1]);
		assertEquals("counter", alarmResult.getTipos()[1]);
	}

	@Test
	public void TestMultiplesAlarm5() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);

		alarm = new AlarmConfig("requests", THRESHOLD.ABOVE, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);

		counter.inc();
		counter.inc();
		counter.inc();
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 2);
		assertFalse(alarmResult.getResultados()[0]);
		assertEquals("counter", alarmResult.getTipos()[0]);

		assertTrue(alarmResult.getResultados()[1]);
		assertEquals("counter", alarmResult.getTipos()[1]);
	}

	@Test
	public void TestMultiplesAlarmTypes() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);

		counter.inc();
		counter.inc();
		counter.inc();
		counter.inc();

		Meter meter = registry.newMeter(AlarmReporterCounterTest.class,
				"requests-2", "thing", TimeUnit.SECONDS);

		alarm = new AlarmConfig("requests-2", THRESHOLD.ABOVE, (double) 5);
		AlarmReporter.addAlarmConfig(alarm);

		meter.mark(10);

		reporter.run();
		// No se procesan en ordennnnnn
		assertEquals(2, alarmResult.getSize());
		assertFalse(alarmResult.getResultados()[0]);
		assertEquals("counter", alarmResult.getTipos()[0]);

		assertTrue(alarmResult.getResultados()[1]);
		assertEquals("meter", alarmResult.getTipos()[1]);

	}

}
