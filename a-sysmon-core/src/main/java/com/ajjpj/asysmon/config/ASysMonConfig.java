package com.ajjpj.asysmon.config;

import com.ajjpj.asysmon.config.log.ASysMonLogger;
import com.ajjpj.asysmon.config.presentation.APresentationMenuEntry;
import com.ajjpj.asysmon.datasink.ADataSink;
import com.ajjpj.asysmon.measure.environment.AEnvironmentMeasurer;
import com.ajjpj.asysmon.measure.scalar.AScalarMeasurer;
import com.ajjpj.asysmon.util.timer.ATimer;

import java.util.Collections;
import java.util.List;


/**
 * @author arno
 */
public class ASysMonConfig {
    public final String applicationId;
    public final String applicationVersionId;
    public final String applicationInstanceId;
    public final String applicationInstanceHtmlColorCode;

    public final int averagingDelayForScalarsMillis;

    public final long measurementTimeoutNanos;
    public final int maxNumMeasurementTimeouts;

    public final long dataSinkTimeoutNanos;
    public final int maxNumDataSinkTimeouts;

    public final ASysMonLogger logger;
    public final ATimer timer; //TODO make configurable

    public final List<AEnvironmentMeasurer> initialEnvironmentMeasurers;
    public final List<AScalarMeasurer> initialScalarMeasurers;
    public final List<ADataSink> initialDataSinks;

    public final List<APresentationMenuEntry> presentationMenuEntries;

    public ASysMonConfig(String applicationId, String applicationVersionId, String instanceId, String instanceHtmlColorCode,
                         int averagingDelayForScalarsMillis, long measurementTimeoutNanos, int maxNumMeasurementTimeouts, long dataSinkTimeoutNanos, int maxNumDataSinkTimeouts,
                         ASysMonLogger logger, ATimer timer,
                         List<AEnvironmentMeasurer> environmentMeasurers, List<AScalarMeasurer> initialScalarMeasurers, List<ADataSink> initialDataSinks, List<APresentationMenuEntry> presentationMenuEntries) {
        this.applicationId = applicationId;
        this.applicationVersionId = applicationVersionId;
        this.applicationInstanceId = instanceId;
        this.applicationInstanceHtmlColorCode = instanceHtmlColorCode;
        this.averagingDelayForScalarsMillis = averagingDelayForScalarsMillis;
        this.measurementTimeoutNanos = measurementTimeoutNanos;
        this.maxNumMeasurementTimeouts = maxNumMeasurementTimeouts;
        this.dataSinkTimeoutNanos = dataSinkTimeoutNanos;
        this.maxNumDataSinkTimeouts = maxNumDataSinkTimeouts;
        this.logger = logger;
        this.timer = timer;
        this.initialEnvironmentMeasurers = Collections.unmodifiableList(environmentMeasurers);
        this.initialScalarMeasurers = Collections.unmodifiableList(initialScalarMeasurers);
        this.initialDataSinks = Collections.unmodifiableList(initialDataSinks);
        this.presentationMenuEntries = Collections.unmodifiableList(presentationMenuEntries);
    }

    /**
     * This flag switches off all 'risky' (or potentially expensive) functionality. It serves as a safeguard in case
     *  A-SysMon has a bug that impacts an application.
     */
    public boolean isGloballyDisabled() {
        final String s = System.getProperty("com.ajjpj.asysmon.globallydisabled");
        return "true".equals(s);
    }
}
