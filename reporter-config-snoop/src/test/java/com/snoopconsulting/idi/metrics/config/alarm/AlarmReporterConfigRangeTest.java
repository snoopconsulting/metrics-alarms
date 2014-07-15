package com.snoopconsulting.idi.metrics.config.alarm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.snoopconsulting.idi.metrics.config.utils.ReporterConfigTest;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;

public class AlarmReporterConfigRangeTest extends ReporterConfigTest {
	@Override
	public String getNombreArchivo() {
		return "src/test/resources/sample/alarmReporter.yaml";
	}
	
	@Test
	public void alarmaNoExiste() throws Exception {
		Metrics.newCounter(getClass(), "requestRangeTETE");
		this.alarmReporter.run();
		assertEquals(0, alarmTest.getSize());
	}

	@Test
	public void alarmTest() throws Exception {
		Metrics.newCounter(getClass(), "requestRange");
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest2() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest3() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest4() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		c.inc();
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest5() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		c.inc();
		c.inc();
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest6() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		c.inc();
		c.inc();
		c.inc();
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}


}
