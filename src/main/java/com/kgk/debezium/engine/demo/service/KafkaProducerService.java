package com.kgk.debezium.engine.demo.service;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    private final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Value("${application.env}")
    private String topicPrefix;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, SpecificRecord> kafkaTemplateAvroSpecificRecord;

    @Autowired
    private KafkaTemplate<String, GenericRecord> kafkaTemplateAvroGenericRecord;

    public void sendMessageWithJsonSerialization(String topic, Integer partition, String key, String message) {
        logger.info("kafka key : {} message : {} ", key, message);
        try {
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, message);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Message {} sent to topic-> {}, partition -> {} with offset -> {}", message, topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

                } else {
                    logger.info("Unable to send message= {} due to : {}", message, ex.getMessage());
                }
            });
        }catch (Exception ex){
            logger.error("Message does not send because of the exception : {} ", ex.getMessage());
        }
    }

    public void sendSpecificRecordMessageWithAvroSerialization(String topic, Integer partition, String key, SpecificRecord message) {
        logger.info("kafka key : {} message : {} ", key, message);
        try {
            CompletableFuture<SendResult<String, SpecificRecord>> future = kafkaTemplateAvroSpecificRecord.send(topic, key, message);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Message {} sent to topic-> {}, partition -> {} with offset -> {}", message, topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

                } else {
                    logger.info("Unable to send message= {} due to : {}", message, ex.getMessage());
                }
            });
        }catch (Exception ex){
            logger.error("Message does not send because of the exception : {} ", ex.getMessage());
        }
    }

    public void sendGenericRecordMessageWithAvroSerialization(String topic, Integer partition, String key, GenericRecord message) {
        logger.info("kafka key : {} message : {} ", key, message);
        try {
            CompletableFuture<SendResult<String, GenericRecord>> future = kafkaTemplateAvroGenericRecord.send(topic, key, message);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Message {} sent to topic-> {}, partition -> {} with offset -> {}", message, topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

                } else {
                    logger.info("Unable to send message= {} due to : {}", message, ex.getMessage());
                }
            });
        }catch (Exception ex){
            logger.error("Message does not send because of the exception : {} ", ex.getMessage());
        }
    }
}
