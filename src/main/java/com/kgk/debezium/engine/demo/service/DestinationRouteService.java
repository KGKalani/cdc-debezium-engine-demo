package com.kgk.debezium.engine.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kgk.debezium.engine.demo.common.util.DataMapperUtil;
import com.kgk.debezium.engine.demo.common.util.TargetChangedDataRecord;
import com.kgk.debezium.engine.demo.dto.ChangedRecord;
import com.kgk.debezium.engine.demo.model.DBRecord;
import io.debezium.engine.ChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DestinationRouteService {
    private final Logger logger = LoggerFactory.getLogger(DestinationRouteService.class);

    @Value("${spring.kafka.topic-avro}")
    private String topicAvro;

    @Value("${spring.kafka.topic-json}")
    private String topicJson;

    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public DestinationRouteService(){
        this.objectMapper = new ObjectMapper();
    }

    public void routeJson(ChangeEvent<String, String> changeEvent, Date receivedDateTime) {
        try {
            if (changeEvent.value() != null) {
                logger.info("Change Record Destination : {} \n changeRecord: {}", changeEvent.destination(), changeEvent);
                DBRecord<Map<String, String>> changeRecord = objectMapper.readValue(changeEvent.value(), new TypeReference<>() {});
                Map<String, String> key = objectMapper.readValue(changeEvent.key(), new TypeReference<>() {});

                TargetChangedDataRecord targetChangedDataRecord = DataMapperUtil.mapChangedRecordSourceToTarget(key, changeRecord);
                logger.info("TargetChangedDataRecord: {} ", targetChangedDataRecord);

                objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
                String kafkaMessageJson = objectMapper.writeValueAsString(targetChangedDataRecord);

                logger.info("Kafka Message: {} ", kafkaMessageJson);
                kafkaProducerService.sendMessageJson(topicJson, null, changeEvent.key(), kafkaMessageJson);
            }
        }catch(Exception ex){
            String errorMessage = "Change Events routing to destination service: " + changeEvent.destination() + " failed due to " + ex.getMessage() + " for data: " + changeEvent.value();
            logger.error(errorMessage);
        }
    }

    public void routeAvro(ChangeEvent<String, String> changeEvent, Date receivedDateTime) {
        try {
            if (changeEvent.value() != null) {
                logger.info("Change Record Destination : {} \n changeRecord: {}", changeEvent.destination(), changeEvent);
                DBRecord<Map<String, String>> changeRecord = objectMapper.readValue(changeEvent.value(), new TypeReference<>() {});
                Map<String, String> key = objectMapper.readValue(changeEvent.key(), new TypeReference<>() {});

                ChangedRecord kafkaMessageAvro = DataMapperUtil.mapChangedRecordSourceToTarget2(key, changeRecord);
                logger.info("Avro ChangedData: {} ", kafkaMessageAvro);

                kafkaProducerService.sendMessageAvro(topicAvro, null, changeEvent.key(), kafkaMessageAvro);

            }
        }catch(Exception ex){
            String errorMessage = "Change Events routing to destination service: " + changeEvent.destination() + " failed due to " + ex.getMessage() + " for data: " + changeEvent.value();
            logger.error(errorMessage);
        }
    }
}
