package com.snoopconsulting.idi.metrics.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snoopconsulting.idi.metrics.alarm.util.EmailAlarm;
import com.yammer.metrics.core.Metric;

/**
 * The Class Alarm. Subclass this class to create a different behavior when the
 * alarm is activated. You can see an example of using the class
 * {@link EmailAlarm}
 */
public class Alarm {

    protected static Logger log = LoggerFactory.getLogger(Alarm.class);

    /**
     * Check metric. This method receives as parameters all the event data with
     * the periodicity specified in the reporter. An example of use can be seen
     * in the next class {@link EmailAlarm}
     *
     * @param resultado Indicates whether the alarm is active
     * @param metric The metrics analyzed
     * @param value The parameter value of the metric
     * @param alarmConfig The alarm settings
     */
    public void checkMetric(boolean resultado, Metric metric,
            Double value, AlarmConfig alarmConfig) {

    }

    /**
     * Behavior when the alarm is activated. Example usage: {@link EmailAlarm}
     */
    public void alarmTimer() {

    }

}
