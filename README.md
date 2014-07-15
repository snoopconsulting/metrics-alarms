
Alarms for Metrics: An Extension for the Metrics Library, to set up alarms
--------------------


This is a Java library that enhances <a href=”http://metrics.codahale.com/”>Metrics Version 2</a> and <a href=”https://github.com/addthis/metrics-reporter-config”>metrics-reporter-config</a> to be able to:

* define alarms triggered when some monitored value goes over,
below some value, or out of a given range.
* send alarm notifications through different channels.
Notifications can be throttled if an alarm is triggered too
often.
* ability to configure alarm triggers and frequency of reporting 
through configuration files.

Currently only works with version 2 of the Metrics Library.

Installing Alarms for Metrics
-----------------------------

The source code contains an add-on to metrics, and an extension 
optional to metrics-reporter-config, which was modified to
support configuring alarms in addition to metrics reports.

Use maven 3 for compile it. It will create two jars:

* metrics-alarms/target/metrics-alarms-1.0.jar
* reporter-config-alarms/target/reporter-config-alarms-1.0.jar

reporter-config-alarms is optional. It allows to define and
configure reports and alarms (but not metrics) in a yaml file
outside the source. You can use alarm-metrics with or
without reporter-config-alarms. If you do not use
reporter-config-alarms you must configure all the alarms in the
source. If you also use reporter-config-alarms, you can use
it to configure both metric reporters and alarm definitions and
theirs reporters, since it is based on the alarm-metrics
library.

Including metrics-alarm and in your proyect
-------------------------------------------

Add the following section to your project pom.xml:

```
<dependency>
    <groupId>com.snoopconsulting.idi.metrics</groupId>
    <artifactId>metrics-alarms</artifactId>
    <version>1.0</version>
</dependency>
```
```

<dependency>
    <groupId>com.snoopconsulting.idi.metrics</groupId>
    <artifactId>reporter-config-alarms</artifactId>
    <version>1.0</version>
</dependency>
```

Using Alarms for Metric
-----------------------

Creating an alarm is split into two steps:

1. defining how to report all alarm events, and

2. defining one or more alarms.

##Define how to report alarms

Before defining alarms define how to report all alarms:

```
/* 
 * Create an alarm reporting channel -- in this case via email
 *
 */
Alarm alarmChannel = new EmailAlarm();

/* create the metrics registry */
registry = new MetricsRegistry();

/* tell the library to define an alarm over a metric and a
reporting channel */
AlarmReporter watcher = new AlarmReporter(registry, MetricPredicate.ALL, alarmChannel);

/* the alarm reporter to start watching for alarms */
watcher.start(1, TimeUnit.MILLISECONDS, 45, TimeUnit.MINUTES);
```
which will check all alarms every millisecond and send an alarm
report via email at a rate of at most one alarm event every 45
minutes. In one or mores alarms are triggered within 45
minutes, the report will include all alarms that were triggered
within the this lapse.

Configuring alarms in source
---------------------------------

You can alarms either in source:
```
AlarmConfig tooHigh, outOfRange, tooLow;
tooHigh = new AlarmConfig("requests", THRESHOLD.ABOVE, 3.0);
watcher.addAlarmConfig(tooHigh);
outOfRange = new AlarmConfig("requestTime", THRESHOLD.RANGE, 2.0,4.0);
watcher.addAlarmConfig(outOfRange);
tooLow = new AlarmConfig("visits", THRESHOLD.ABOVE, 3.0);
watcher.addAlarmConfig(tooLow);
```

which will create 3 new alarms:

* an alarm over the “requests” metric, and raise an alarm if it
goes higher than 3.
* an alarm over the “timeout” metric, and raise an alarm if it
goes lower than 2 o higher than 4.
* an alarm over the “visits” metric, and raises an alarm if it
goes lower than 3

Alternatively you can define the same alarms via a yaml
configuration file:

Configuring alarms via metrics-alarms.yaml
------------------------------------------

```
alarm:
 -
   period: 1
   timeunit: 'NANOSECONDS'
   periodAlarm: 30
   timeunitAlarm: 'SECONDS'
   metrics:
     - metric: request
       threshold: ABOVE
       value: 1
     - metric: requestRange
       threshold: RANGE
       valueMin: 2
       valueMax: 4
     - metric: requestBelow
       threshold: BELOW
       value: 3
```

and then load the configuration file:
```
alarmConfig = ReporterConfig.loadFromFile("metric-alarms.yaml");
config.enableAll();
```

metric-alarms.yaml always need at least:

* how often to watch for an alarm condition (period and timeUnit)
* how often to send alarm notifications (periodAlarm and
timeunitAlarm)
* at least one metric to watch for alarms (inside the metrics
section)

Configuring alarms and metric reporters
---------------------------------------

Since metrics-reporter-config is an extension to metrics-config
(alarms is just a new top-level section in the config file),
you can define both alarms and/or metrics reporters in the same
configuration file. <a
href=”https://github.com/addthis/metrics-reporter-config”>See
metrics config documentation</a> for more details.

Metrics types and valid alarms
------------------------------

Alarms are always defined over a measure (called a “Parameter”
in metrics-alarm parlance) associated to a given metric. You
can tell over which measure to define an alarm, otherwise the
defaulte measure (in bold font) will be used:


* GAUGE
 * **VALUE**
* COUNTER:
  * **COUNT**
* METER:
  * **COUNT**
  * MEAN
  * MINUTE
* HISTOGRAM:
 * **COUNT**
 * MIN
 * MAX
 * MEAN
 * STDDEN
 * MEDIAN
 * PERCENTILE
* TIMER:
 * **COUNT**
 * MIN
 * MAX
 * MEAN
 * STDDEN
 * MEDIAN
 * PERCENTILE
 * MINUTE

To define an alarm over any measure you must use:
```
AlarmConfig(String metricName, THRESHOLD threshold, Double valueMin,

Double valueMax, PARAMETER parameter, Double parameterValue, boolean negated)
```
for example:

```
alarm = new AlarmConfig(“requests”, THRESHOLD.MAX, 3.0, 0, PARAMETER.STDDEV, 0, false);
```

creates a new alarm over a metric named “requests”. The alarm
must be triggered when the alarm standard deviation goes over
3.
```
alarm = new AlarmConfig(“responseTime”, THRESHOLD.RANGE, 0.1, 0.8, PARAMETER.PERCENTILE, 0.75, false);
```
creates a new alarm over a metric named “responseTime”. The alarm 
must be triggered when the alarm 75th percentile goes out 
of the [0.1,0.8] range. Only PARAMETER.TIME and PARAMETER.PERCENTILE 
require a parameterValue.


Creating new alarm reporters
----------------------------

This bookseller only includes only one type of alarm EmailAlarm. 
If you need another type must subclass Alarm and implement the 
following methods:

```
public void checkMetric(boolean resultado, Metric metric,Double valorActual, AlarmConfig alarmConfig)// This method is run whenever metrics are analyzed

public void alarm Timer()//It runs in a given time, usually used to encode the behavior of the alarm
```

The periodicity of these methods are executed are defined when the reporter starts
