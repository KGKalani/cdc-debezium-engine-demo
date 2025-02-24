package com.kgk.debezium.engine.demo.common.transform;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class AvroTransformer {
    private final static Logger logger = LoggerFactory.getLogger(AvroTransformer.class);

    /***
     * Converts a JSON string into a GenericRecord using Avro's JsonDecoder.
     * <p>This method parses the input JSON string and decodes it into an Avro GenericRecord
     * based on the provided schema. It ensures proper type conversion and validation
     * according to the Avro schema definition.</p>
     *
     * @param schema The Avro schema defining the structure of the GenericRecord.
     * @param jsonString The JSON string representing the record data.
     * @return A GenericRecord populated with values from the JSON string, adhering to the given schema.
     * @throws IOException IOException If parsing the JSON string or decoding the data fails.
     */
    public static GenericRecord transformJsonToGenericRecordThroughAvroJsonDecoder(Schema schema, String jsonString) throws IOException {
        logger.info("Start Converting Json String to Avro GenericRecord Format");
        //Transform the JSON string to handle union types
        String transformedJson = JsonTransformer.transformJsonForUnionTypesAndMissingFields(schema, jsonString);

        // Create a JsonDecoder
        Decoder decoder = DecoderFactory.get().jsonDecoder(schema, transformedJson);

        // Decode the JSON string into a GenericRecord
        GenericDatumReader<GenericRecord> datumJsonReader = new GenericDatumReader<>(schema);

        logger.info("End Converting Json String to Avro GenericRecord Format");

        return datumJsonReader.read(null, decoder);
    }

    /***
     * Converts a Map<String, Object> into an Avro GenericRecord based on the provided Avro schema
     * @param schema The Avro schema defining the structure of the GenericRecord.
     * @param dataMap The map containing key-value pairs where keys are field names
     *                and values are the corresponding field values.
     * @return A GenericRecord populated with the values from the dataMap, adhering to the given schema.
     */
    public static GenericRecord transformMapToGenericRecord(Schema schema, Map<String, Object> dataMap) {
        logger.info("Start Converting Map to Avro GenericRecord Format");
        GenericRecord record = new GenericData.Record(schema);
        for (Schema.Field field : schema.getFields()) {
            record.put(field.name(), dataMap.getOrDefault(field.name(), field.hasDefaultValue() ? field.defaultVal() : null));
        }
        logger.info("End Converting Map to Avro GenericRecord Format");
        return record;
    }

    /***
     * Converts a byte[] into an Avro GenericRecord based on the provided Avro schema
     * @param schema The Avro schema defining the structure of the GenericRecord.
     * @param value byte[] which represent data
     * @return A GenericRecord populated with values from the byte[], adhering to the given schema.
     * @throws IOException IOException If decoding the data fails.
     */
    public static GenericRecord transformByteArrayToGenericRecordThroughAvroBinaryDecoder(Schema schema, byte[] value) throws IOException {
         // Deserializing the byte array using GenericDatumReader
        ByteArrayInputStream inputStream = new ByteArrayInputStream(value);
        Decoder decoder = DecoderFactory.get().binaryDecoder(inputStream, null);

        // Create a GenericDatumReader for deserialization (to Create Avro Message) and Deserialize into a GenericRecord
        GenericDatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        return datumReader.read(null, decoder);
    }
}