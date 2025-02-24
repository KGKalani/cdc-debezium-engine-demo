package com.kgk.debezium.engine.demo.common.util;


import com.kgk.debezium.engine.demo.model.DBRecord;
import io.debezium.data.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


public class DataMapperUtil {
    private final Logger logger = LoggerFactory.getLogger(DataMapperUtil.class);

    public static TargetChangeRecord convertSourceChangeEventToTarget(Object key, DBRecord<Map<String, String>> sourceChangedEvent){
        return new TargetChangeRecord(
                sourceChangedEvent.getSource(),
                sourceChangedEvent.getOperation(),
                sourceChangedEvent.getTsMs(),
                Envelope.Operation.DELETE.code().equals(sourceChangedEvent.getOperation())
                        ? key : sourceChangedEvent.getAfterData());
    }




}
