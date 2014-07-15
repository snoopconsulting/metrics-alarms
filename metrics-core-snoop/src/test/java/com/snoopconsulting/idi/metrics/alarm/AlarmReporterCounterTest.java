/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.snoopconsulting.idi.metrics.alarm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.snoopconsulting.idi.metrics.alarm.AlarmConfig.THRESHOLD;
import com.snoopconsulting.idi.metrics.utils.AlarmResultTest;
import com.yammer.metrics.core.Counter;
import com.yammer.metrics.core.MetricPredicate;
import com.yammer.metrics.core.MetricsRegistry;

@RunWith(JUnit4.class)
public class AlarmReporterCounterTest {

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
	public void testCounterMaxFalse() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxFalse2() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxTrue() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxTrue2() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxTrue3() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrue1() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrue() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrue2() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrue3() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxFalseNegated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxFalse2Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxTrueNegated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxTrue2Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMaxTrue3Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.ABOVE,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrue1Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrueNegated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrue2Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterMinTrue3Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.BELOW,
				(double) 1, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue1() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue2() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue3() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue4() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue5() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue1Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3, true);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertTrue(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue2Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue3Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();

		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterRangeTrue4Negated() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1, (double) 3, true);
		AlarmReporter.addAlarmConfig(alarm);
		counter.inc();
		counter.inc();
		counter.inc();
		reporter.run();
		assertTrue(alarmResult.getSize() == 1);
		assertFalse(alarmResult.getResultados()[0]);
	}

	@org.junit.Test
	public void testCounterErroneo1() {
		AlarmConfig alarm = new AlarmConfig("requests", THRESHOLD.RANGE,
				(double) 1);
		AlarmReporter.addAlarmConfig(alarm);
		reporter.run();

		assertTrue(alarmResult.getSize() == 0);

	}

}
