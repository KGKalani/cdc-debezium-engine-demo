package com.kgk.debezium.engine.demo.service;

import com.kgk.debezium.engine.demo.dto.ChangedRecord;
import com.kgk.debezium.engine.demo.dto.StudentV2;
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
    private KafkaTemplate<String, ChangedRecord> kafkaTemplateAvro;

    @Autowired
    private KafkaTemplate<String, StudentV2> kafkaTemplateStudentAvro;

    public void sendMessageJson(String topic,Integer partition, String key, String message) {
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

    public void sendMessageAvro(String topic,Integer partition, String key, ChangedRecord message) {
        logger.info("kafka key : {} message : {} ", key, message);
        try {
            CompletableFuture<SendResult<String, ChangedRecord>> future = kafkaTemplateAvro.send(topic, key, message);
            //kafkaTemplate.send(topic, partition, key, message);
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

    public void sendStudentMessageAvro(String topic,Integer partition, String key, StudentV2 message) {
        logger.info("kafka key : {} message : {} ", key, message);
        try {
            CompletableFuture<SendResult<String, StudentV2>> future = kafkaTemplateStudentAvro.send(topic, key, message);
            //kafkaTemplate.send(topic, partition, key, message);
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
