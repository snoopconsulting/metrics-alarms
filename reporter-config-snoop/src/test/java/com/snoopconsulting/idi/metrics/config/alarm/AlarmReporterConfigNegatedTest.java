package com.snoopconsulting.idi.metrics.config.alarm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.snoopconsulting.idi.metrics.config.ReporterConfig;
import com.snoopconsulting.idi.metrics.config.utils.AlarmResulConfigTest;
import com.snoopconsulting.idi.metrics.config.utils.ReporterConfigTest;
import com.yammer.metrics.Metrics;
import com.yammer.metrics.core.Counter;
@RunWith(JUnit4.class)
public class AlarmReporterConfigNegatedTest extends ReporterConfigTest {
	@Override
	public String getNombreArchivo() {
		return "src/test/resources/sample/alarmReporter-negated.yaml";
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
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest2() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest3() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTest4() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestRange");
		c.inc();
		c.inc();
		c.inc();
		this.alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
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
		assertFalse(alarmTest.getResultados()[0]);
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
		assertTrue(alarmTest.getResultados()[0]);
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
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMax3() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "request");
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMax4() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "request");
		c.inc();
		c.inc();

		alarmReporter.run();

		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin2() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin3() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		c.inc();
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertFalse(alarmTest.getResultados()[0]);
	}

	@Test
	public void alarmTestMin4() throws Exception {
		Counter c = Metrics.newCounter(getClass(), "requestBelow");
		c.inc();
		c.inc();
		c.inc();
		alarmReporter.run();
		assertEquals(1, alarmTest.getSize());
		assertTrue(alarmTest.getResultados()[0]);
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
		assertTrue(alarmTest.getResultados()[0]);
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
