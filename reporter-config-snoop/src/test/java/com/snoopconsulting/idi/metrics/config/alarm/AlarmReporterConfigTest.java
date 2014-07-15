package com.snoopconsulting.idi.metrics.config.alarm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.snoopconsulting.idi.metrics.config.utils.ReporterConfigTest;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;

public class AlarmReporterConfigTest extends ReporterConfigTest {
	@Override
	public String getNombreArchivo() {
		return "src/test/resources/sample/alarmReporter.yaml";
	}
	@Test
	public void alarmTestMax() throws Exception {
		Metrics.newCounter(getClass(), "noHay");
		alarmReporter.run();
		assertEquals(0, alarmTest.getSize());
	}

	@Test
	public void alarmTestMax2() throws Exception {
		Metrics.newCounter(getClass(), "request");
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMax3() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "request");
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMax4() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "request");
		c.inc();
		c.inc();

		alarmReporter.run();

		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin2() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin3() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		c.inc();
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin4() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		c.inc();
		c.inc();
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin5() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		c.inc();
		c.inc();
		c.inc();
		c.inc();

		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}
}
