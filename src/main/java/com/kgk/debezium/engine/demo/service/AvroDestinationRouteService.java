package com.kgk.debezium.engine.demo.service;

import com.kgk.debezium.engine.demo.common.transform.AvroTransformer;
import com.kgk.debezium.engine.demo.converter.KafkaAvroPayloadConverter;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.debezium.engine.ChangeEvent;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AvroDestinationRouteService {
    private final Logger logger = LoggerFactory.getLogger(AvroDestinationRouteService.class);

    @Value("${spring.kafka.topic-avro-generic}")
    private String topic;

    @Value("${config.debezium.topic.prefix}")
    private String topicPrefix;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    private final KafkaAvroDeserializer deserializer;

    public AvroDestinationRouteService(AvroDeserializerService avroDeserializerService){
        deserializer = avroDeserializerService.getDeserializer();
    }

    public void routeAvroByteArray(ChangeEvent<byte[], byte[]> changeEvent, Date receivedDateTime) {
        try {
            /*
             * Deserializing using KafkaAvroDeserializer
             */
            GenericRecord keyGenericRecord = (GenericRecord) deserializer.deserialize(null, changeEvent.key());
            GenericRecord valueGenericRecord = (GenericRecord) deserializer.deserialize(null, changeEvent.value());

            /*
             * Deserializing using DatumReader
             */
            GenericRecord valueGenericRecord2 = deserializeAvroUsingDatumReader(changeEvent);

            /*
             * Create Produced Kafka Message in SpecificRecord to Serialized to Avro
             */
            String destination = changeEvent.destination();
            String schemaType = destination.contains(topicPrefix) ? destination.substring(destination.indexOf(".") + 1) : destination;
            Schema targetSchema = SchemaLoaderService.getSchema(schemaType);

            GenericRecord kafkaMessageGenericRecord = KafkaAvroPayloadConverter.convertSourceGenericRecordToTargetGenericRecord(targetSchema, valueGenericRecord);
            String kafkaKey = String.valueOf(keyGenericRecord.get(0));

            kafkaProducerService.sendGenericRecordMessageWithAvroSerialization(topic, null, kafkaKey, kafkaMessageGenericRecord);


        } catch (Exception ex) {
            String errorMessage = "Change Events routing to destination service: " + changeEvent.destination() + " failed due to " + ex.getMessage() + " for data: " + changeEvent.value();
            logger.error(errorMessage);
        }
    }

    private GenericRecord deserializeAvroUsingDatumReader(ChangeEvent<byte[], byte[]> changeEvent) {
        GenericRecord record = null;
        try {
            byte[] value = changeEvent.value();

            /*
             * Remove first 5 bytes from byte array because it defines schema version
             */
            byte[] avroDataWithoutMagicByteOfValue = new byte[value.length - 5];
            System.arraycopy(value, 5, avroDataWithoutMagicByteOfValue, 0, avroDataWithoutMagicByteOfValue.length);

            Schema schema = SchemaLoaderService.getSchema("debezium");
            record = AvroTransformer.transformByteArrayToGenericRecordThroughAvroBinaryDecoder(schema, avroDataWithoutMagicByteOfValue);

        } catch (Exception ex) {
            logger.error("Deserialize error : {} ", ex.getMessage());
        }
        logger.info("before {}", record.get("before"));
        logger.info("after {}", record.get("after"));

        return record;
    }

    private long epochMicro() {
        return Instant.now().toEpochMilli() * 1_000 + Instant.now().getNano() / 1_000;
    }

}
