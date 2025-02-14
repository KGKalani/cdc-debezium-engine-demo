package com.kgk.debezium.engine.demo.common.util;


import com.kgk.debezium.engine.demo.dto.ChangedRecord;
import com.kgk.debezium.engine.demo.dto.Source;
import com.kgk.debezium.engine.demo.model.DBRecord;
import io.debezium.data.Envelope;

import java.util.Map;

public class DataMapperUtil {
    public static TargetChangedDataRecord mapChangedRecordSourceToTarget(Object key, DBRecord<Map<String, String>> sourceChangedRecord){
        return new TargetChangedDataRecord(
                sourceChangedRecord.getSource(),
                sourceChangedRecord.getOperation(),
                sourceChangedRecord.getTsMs(),
                Envelope.Operation.DELETE.code().equals(sourceChangedRecord.getOperation())
                        ? key : sourceChangedRecord.getAfterData());
    }

    public static ChangedRecord mapChangedRecordSourceToTarget2(Map<String, String> key, DBRecord<Map<String, String>> sourceChangedRecord){
        // Map the source field
        Source source = Source.newBuilder()
                .setDb(sourceChangedRecord.getSource().getDb())
                .setSchema$(sourceChangedRecord.getSource().getSchema())
                .setTable(sourceChangedRecord.getSource().getTable())
                .setLsn(sourceChangedRecord.getSource().getLsn())
                .build();

        // Map the data field (assuming it's a Map<String, Object>)
        Map<String, String> data = Envelope.Operation.DELETE.code().equals(sourceChangedRecord.getOperation())
                ? key : sourceChangedRecord.getAfterData();

        return ChangedRecord.newBuilder()
                .setSource(source)
                .setOperation(sourceChangedRecord.getOperation())
                .setTsMs(sourceChangedRecord.getTsMs())
                .setData(data)
                .build();
    }


}
