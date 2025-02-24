package com.kgk.debezium.engine.demo.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgk.debezium.engine.demo.common.transform.AvroTransformer;
import com.kgk.debezium.engine.demo.dto.GenericChangedRecord;
import com.kgk.debezium.engine.demo.dto.Source;
import com.kgk.debezium.engine.demo.model.DBRecord;
import com.kgk.debezium.engine.demo.service.SchemaLoaderService;
import io.debezium.data.Envelope;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class KafkaAvroPayloadConverter {
    private final Logger logger = LoggerFactory.getLogger(KafkaAvroPayloadConverter.class);

    public static GenericRecord convertSourceChangeEventInJsonToTargetGenericRecord(Schema targetSchema, DBRecord<Map<String, Object>> sourceChangedEvent) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GenericRecord avroTransformedChangeEvent = new GenericData.Record(targetSchema);

        //Select changeEvent "after" or "before" section for "data" section
        Map<String, Object> data = Envelope.Operation.DELETE.code().equals(sourceChangedEvent.getOperation())
                ? sourceChangedEvent.getBeforeData() : sourceChangedEvent.getAfterData();

        //Extract the schema for the "data" section
        Schema dataSchema = SchemaLoaderService.extractSubSchemaByField(targetSchema, "data");

        // Convert Map to JSON string and crate generic record for "data" section from JSON String
        String json = objectMapper.writeValueAsString(data);
        GenericRecord avroDataRecord = AvroTransformer.transformJsonToGenericRecordThroughAvroJsonDecoder(dataSchema, json);

        // Create a SpecificRecord record for the "source" section
        Source source = Source.newBuilder()
                .setDb(sourceChangedEvent.getSource().getDb())
                .setSchema$(sourceChangedEvent.getSource().getSchema())
                .setTable(sourceChangedEvent.getSource().getTable())
                .setTsMs(sourceChangedEvent.getSource().getTsMs())
                .setLsn(sourceChangedEvent.getSource().getLsn())
                .build();

        // Create a GenericRecord for the full schema
        avroTransformedChangeEvent.put("source", source);
        avroTransformedChangeEvent.put("operation", sourceChangedEvent.getOperation());
        avroTransformedChangeEvent.put("ts_ms", sourceChangedEvent.getTsMs());
        avroTransformedChangeEvent.put("data", avroDataRecord);

        return avroTransformedChangeEvent;
    }

    public static GenericRecord convertSourceChangeEventInMapToTargetGenericRecord(Schema targetSchema, DBRecord<Map<String, Object>> sourceChangeEvent) {
        GenericRecord avroTransformedChangeEvent = new GenericData.Record(targetSchema);

        // Create data record
        Map<String, Object> data = Envelope.Operation.DELETE.code().equals(sourceChangeEvent.getOperation())
                ? sourceChangeEvent.getBeforeData() : sourceChangeEvent.getAfterData();

        //Get the schema of after record
        Schema dataSchema = SchemaLoaderService.extractSubSchemaByField(targetSchema, "data");

        // Create Data GenericRecord
        GenericRecord avroDataRecord = AvroTransformer.transformMapToGenericRecord(dataSchema, data);

        // Create a SpecificRecord record for the "source" section
        Source source = Source.newBuilder()
                .setDb(sourceChangeEvent.getSource().getDb())
                .setSchema$(sourceChangeEvent.getSource().getSchema())
                .setTable(sourceChangeEvent.getSource().getTable())
                .setTsMs(sourceChangeEvent.getSource().getTsMs())
                .setLsn(sourceChangeEvent.getSource().getLsn())
                .build();

        // Create a GenericRecord for the full schema
        avroTransformedChangeEvent.put("source", source);
        avroTransformedChangeEvent.put("operation", sourceChangeEvent.getOperation());
        avroTransformedChangeEvent.put("ts_ms", sourceChangeEvent.getTsMs());
        avroTransformedChangeEvent.put("data", avroDataRecord);

        return avroTransformedChangeEvent;
    }

    public static GenericRecord convertSourceGenericRecordToTargetGenericRecord(Schema schema, GenericRecord valueGenericRecord){
        GenericRecord sourceGenericRecord = (GenericRecord)valueGenericRecord.get("source");
        GenericRecord afterGenericRecord = (GenericRecord)valueGenericRecord.get("after");

        Source source = Source.newBuilder()
                .setDb(String.valueOf(sourceGenericRecord.get("db")))
                .setSchema$(String.valueOf(sourceGenericRecord.get("schema")))
                .setTable(String.valueOf(sourceGenericRecord.get("table")))
                .setLsn((Long) sourceGenericRecord.get("lsn"))
                .setTsMs((Long) sourceGenericRecord.get("ts_ms"))
                .build();

        Object object = SpecificData.get().deepCopy(afterGenericRecord.getSchema(), (GenericRecord)valueGenericRecord.get("after"));

        GenericRecord avroRecord = new GenericData.Record(schema);
        avroRecord.put("source", source);
        avroRecord.put("operation", String.valueOf(valueGenericRecord.get("op")));
        avroRecord.put("ts_ms", valueGenericRecord.get("ts_ms"));
        avroRecord.put("data", object);

        return avroRecord;

    }

    public static GenericChangedRecord convertSourceChangeEventToTargetGenericChangedRecord(Map<String, Object> key, DBRecord<Map<String, Object>> sourceChangedRecord){
        // Map the source field
        Source source = Source.newBuilder()
                .setDb(sourceChangedRecord.getSource().getDb())
                .setSchema$(sourceChangedRecord.getSource().getSchema())
                .setTable(sourceChangedRecord.getSource().getTable())
                .setLsn(sourceChangedRecord.getSource().getLsn())
                .build();

        // Map the data field (assuming it's a Map<String, Object>)
        Map<String, Object> data = Envelope.Operation.DELETE.code().equals(sourceChangedRecord.getOperation())
                ? key : sourceChangedRecord.getAfterData();

        return GenericChangedRecord.newBuilder()
                .setSource(source)
                .setOperation(sourceChangedRecord.getOperation())
                .setTsMs(sourceChangedRecord.getTsMs())
                .setData(data)
                .build();
    }
}
