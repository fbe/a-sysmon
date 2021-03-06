package com.ajjpj.asysmon.config;

import com.ajjpj.asysmon.config.log.ALog4JLogger;
import com.ajjpj.asysmon.config.log.AStdOutLogger;
import com.ajjpj.asysmon.config.log.ASysMonLogger;
import com.ajjpj.asysmon.config.presentation.APresentationMenuEntry;
import com.ajjpj.asysmon.config.presentation.APresentationPageDefinition;
import com.ajjpj.asysmon.datasink.ADataSink;
import com.ajjpj.asysmon.measure.environment.AEnvironmentMeasurer;
import com.ajjpj.asysmon.measure.scalar.AScalarMeasurer;
import com.ajjpj.asysmon.util.timer.ASystemNanoTimer;
import com.ajjpj.asysmon.util.timer.ATimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author arno
 */
public class ASysMonConfigBuilder {
    private String applicationId;
    private String applicationVersionId;
    private String applicationInstanceId;
    private String applicationInstanceHtmlColorCode;

    private int averagingDelayForScalarsMillis = 1000;

    private long measurementTimeoutNanos = 20*1000*1000;
    private int maxNumMeasurementTimeouts = 3;

    private long dataSinkTimeoutNanos = 100*1000;
    private int maxNumDataSinkTimeouts = 3;

    private ASysMonLogger logger = defaultLogger();
    private ATimer timer = new ASystemNanoTimer();

    private final List<AEnvironmentMeasurer> environmentMeasurers = new ArrayList<AEnvironmentMeasurer>();
    private final List<AScalarMeasurer> scalarMeasurers = new ArrayList<AScalarMeasurer>();
    private final List<ADataSink> dataSinks = new ArrayList<ADataSink>();

    private final List<APresentationMenuEntry> presentationMenuEntries = new ArrayList<APresentationMenuEntry>();

    public ASysMonConfigBuilder(String applicationId, String applicationVersionId, String applicationInstanceId, String applicationInstanceHtmlColorCode) {
        this.applicationId = applicationId;
        this.applicationVersionId = applicationVersionId;
        this.applicationInstanceId = applicationInstanceId;
        this.applicationInstanceHtmlColorCode = applicationInstanceHtmlColorCode;
    }

    @SuppressWarnings("unused")
    public ASysMonConfigBuilder setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }
    @SuppressWarnings("unused")
    public ASysMonConfigBuilder setApplicationVersionId(String applicationVersionId) {
        this.applicationVersionId = applicationVersionId;
        return this;
    }
    @SuppressWarnings("unused")
    public ASysMonConfigBuilder setApplicationInstanceId(String applicationInstanceId) {
        this.applicationInstanceId = applicationInstanceId;
        return this;
    }
    @SuppressWarnings("unused")
    public ASysMonConfigBuilder setApplicationInstanceHtmlColorCode(String applicationInstanceHtmlColorCode) {
        this.applicationInstanceHtmlColorCode = applicationInstanceHtmlColorCode;
        return this;
    }

    public ASysMonConfigBuilder setLogger(ASysMonLogger logger) {
        this.logger = logger;
        return this;
    }
    public ASysMonConfigBuilder setTimer(ATimer timer) {
        this.timer = timer;
        return this;
    }

    public ASysMonConfigBuilder setAveragingDelayForScalarsMillis(int averagingDelayForScalarsMillis) {
        this.averagingDelayForScalarsMillis = averagingDelayForScalarsMillis;
        return this;
    }

    public ASysMonConfigBuilder setMeasurementTimeoutNanos(long measurementTimeoutNanos) {
        this.measurementTimeoutNanos = measurementTimeoutNanos;
        return this;
    }

    public ASysMonConfigBuilder setMaxNumMeasurementTimeouts(int maxNumMeasurementTimeouts) {
        this.maxNumMeasurementTimeouts = maxNumMeasurementTimeouts;
        return this;
    }

    public ASysMonConfigBuilder setDataSinkTimeoutNanos(long dataSinkTimeoutNanos) {
        this.dataSinkTimeoutNanos = dataSinkTimeoutNanos;
        return this;
    }

    public ASysMonConfigBuilder setMaxNumDataSinkTimeouts(int maxNumDataSinkTimeouts) {
        this.maxNumDataSinkTimeouts = maxNumDataSinkTimeouts;
        return this;
    }

    public ASysMonConfigBuilder addEnvironmentMeasurer(AEnvironmentMeasurer environmentMeasurer) {
        this.environmentMeasurers.add(environmentMeasurer);
        return this;
    }

    public ASysMonConfigBuilder addScalarMeasurer(AScalarMeasurer scalarMeasurer) {
        this.scalarMeasurers.add(scalarMeasurer);
        return this;
    }

    public ASysMonConfigBuilder addDataSink(ADataSink dataSink) {
        this.dataSinks.add(dataSink);
        return this;
    }

    public ASysMonConfigBuilder addPresentationMenuEntry(String label, List<APresentationPageDefinition> entries) {
        presentationMenuEntries.add(new APresentationMenuEntry(label, entries));
        return this;
    }

    @SuppressWarnings("unused")
    public ASysMonConfigBuilder addPresentationMenuEntry(String label, APresentationPageDefinition... entries) {
        return addPresentationMenuEntry(label, Arrays.asList(entries));
    }

    public ASysMonConfig build() {
        return new ASysMonConfig(
                applicationId, applicationVersionId, applicationInstanceId, applicationInstanceHtmlColorCode,
                averagingDelayForScalarsMillis,
                measurementTimeoutNanos, maxNumMeasurementTimeouts,
                dataSinkTimeoutNanos, maxNumDataSinkTimeouts,
                logger, timer,
                environmentMeasurers, scalarMeasurers, dataSinks,
                presentationMenuEntries
                );
    }

    public static ASysMonLogger defaultLogger() {
        try {
            return ALog4JLogger.INSTANCE; //TODO verify that this works without log4j
        }
        catch (Throwable th) {
            return AStdOutLogger.INSTANCE;
        }
    }
}
