package com.ajjpj.asysmon.processing;

import com.ajjpj.asysmon.data.AHierarchicalData;

/**
 * @author arno
 */
public interface ADataSink {
    void onStartedHierarchicalMeasurement();
    void onFinishedHierarchicalMeasurement(AHierarchicalData data);
}
