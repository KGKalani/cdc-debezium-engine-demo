package com.kgk.debezium.engine.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kgk.debezium.engine.demo.common.util.DataMapperUtil;
import com.kgk.debezium.engine.demo.common.util.TargetChangeRecord;
import com.kgk.debezium.engine.demo.converter.KafkaAvroPayloadConverter;
import com.kgk.debezium.engine.demo.model.DBRecord;
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
import java.util.Map;

@Service
public class DestinationRouteService {
    private final Logger logger = LoggerFactory.getLogger(DestinationRouteService.class);

    @Value("${spring.kafka.topic-avro}")
    private String topicAvro;

    @Value("${spring.kafka.topic-json}")
    private String topicJson;

    @Value("${config.debezium.topic.prefix}")
    private String topicPrefix;

    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public DestinationRouteService(){
        this.objectMapper = new ObjectMapper();
    }

    public void routeJson(ChangeEvent<String, String> changeEvent, Date receivedDateTime) {
        try {
            if (changeEvent.value() != null) {
                logger.info("Change Record Destination : {} Received Time {},  ChangeRecord: {}", changeEvent.destination(), receivedDateTime, changeEvent);
                DBRecord<Map<String, String>> changeRecord = objectMapper.readValue(changeEvent.value(), new TypeReference<>() {});
                Map<String, String> key = objectMapper.readValue(changeEvent.key(), new TypeReference<>() {});

                TargetChangeRecord targetChangeRecord = DataMapperUtil.convertSourceChangeEventToTarget(key, changeRecord);
                logger.info("TargetChangedDataRecord: {} ", targetChangeRecord);

                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
                String kafkaMessageJson = objectMapper.writeValueAsString(targetChangeRecord);

                logger.info("Kafka Message: {} ", kafkaMessageJson);
                kafkaProducerService.sendMessageWithJsonSerialization(topicJson, null, changeEvent.key(), kafkaMessageJson);
            }
        }catch(Exception ex){
            String errorMessage = "Change Events routing to destination service: " + changeEvent.destination() + " failed due to " + ex.getMessage() + " for data: " + changeEvent.value();
            logger.error(errorMessage);
        }
    }

    public void routeAvro(ChangeEvent<String, String> changeEvent, Date receivedDateTime) {
        try {
            if (changeEvent.value() != null) {
                logger.info("Change Record Destination : {} Received Time {},  ChangeRecord: {}", changeEvent.destination(), receivedDateTime, changeEvent);
                DBRecord<Map<String, Object>> changeValueEvent = objectMapper.readValue(changeEvent.value(), new TypeReference<>() {});

                //GenericRecord kafkaMessageAvro = DataMapperUtil.mapStudentJsonChangedSourceEventToTargetInAvro(key, changeRecord, changeEvent);
                String destination = changeEvent.destination();
                String schemaType = destination.contains(topicPrefix) ? destination.substring(destination.indexOf(".") + 1) : destination;
                Schema targetSchema = SchemaLoaderService.getSchema(schemaType);

                long start = epochMicro();
               // GenericRecord kafkaMessageAvro = KafkaAvroPayloadConverter.convertSourceChangeEventInJsonToTargetGenericRecord(targetSchema, changeValueEvent);
                long end = epochMicro();
                //logger.info("Json: Time taken to convert Json String to Avro {} Ms", (end - start));

                start = epochMicro();
                GenericRecord kafkaMessageAvro2 = KafkaAvroPayloadConverter.convertSourceChangeEventInMapToTargetGenericRecord(targetSchema, changeValueEvent);
                end = epochMicro();
                logger.info("Map: Time taken to convert Map to Avro {} Ms", (end - start));

                //kafkaProducerService.sendGenericRecordMessageWithAvroSerialization(topicAvro, null, changeEvent.key(), kafkaMessageAvro);
                kafkaProducerService.sendGenericRecordMessageWithAvroSerialization(topicAvro, null, changeEvent.key(), kafkaMessageAvro2);

            }
        }catch(Exception ex){
            String errorMessage = "Change Events routing to destination service: " + changeEvent.destination() + " failed due to " + ex.getMessage() + " for data: " + changeEvent.value();
            logger.error(errorMessage);
        }
    }

    private long epochMicro() {
        return Instant.now().toEpochMilli() * 1_000 + Instant.now().getNano() / 1_000;
    }

//    public void routeSourceRecordJson(ChangeEvent<SourceRecord, SourceRecord> changeEvent) {
//        try {
//            if (changeEvent.value() != null) {
//                logger.info("Change Record Destination : {} \n changeRecord: {}", changeEvent.destination(), changeEvent);
//                DBRecord<Map<String, String>> changeRecord = objectMapper.readValue(changeEvent.value(), new TypeReference<>() {});
//                Map<String, String> key = objectMapper.readValue(changeEvent.key(), new TypeReference<>() {});
//
//                TargetChangedDataRecord targetChangedDataRecord = DataMapperUtil.mapChangedRecordSourceToTarget(key, changeRecord);
//                logger.info("TargetChangedDataRecord: {} ", targetChangedDataRecord);
//
//                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
//                String kafkaMessageJson = objectMapper.writeValueAsString(targetChangedDataRecord);
//
//                logger.info("Kafka Message: {} ", kafkaMessageJson);
//                kafkaProducerService.sendMessageJson(topicJson, null, changeEvent.key(), kafkaMessageJson);
//            }
//        }catch(Exception ex){
//            String errorMessage = "Change Events routing to destination service: " + changeEvent.destination() + " failed due to " + ex.getMessage() + " for data: " + changeEvent.value();
//            logger.error(errorMessage);
//        }
//    }


}
