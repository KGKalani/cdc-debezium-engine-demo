package com.kgk.debezium.engine.demo.config;

import com.kgk.debezium.engine.demo.dto.ChangedRecord;
import com.kgk.debezium.engine.demo.dto.StudentV2;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap.servers}")
    private String bootstrapServers;

//    @Value("${spring.kafka.ssl.keystore.location}")
//    private String keystoreLocation;
//
//    @Value("${spring.kafka.ssl.keystore.password}")
//    private String keystorePassword;

    @Value("${spring.kafka.schema.registry.url}")
    private String schemaRegistryUrl;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> kafkaProducerConfigProps = new HashMap<>();
        kafkaProducerConfigProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaProducerConfigProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProducerConfigProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfigProps);
    }

    @Bean
    public ProducerFactory<String, ChangedRecord> producerFactoryAvro() {
        Map<String, Object> kafkaProducerConfigProps = new HashMap<>();
        kafkaProducerConfigProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaProducerConfigProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProducerConfigProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        kafkaProducerConfigProps.put("schema.registry.url", schemaRegistryUrl);
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfigProps);
    }

    @Bean
    public ProducerFactory<String, StudentV2> producerFactoryStudentAvro() {
        Map<String, Object> kafkaProducerConfigProps = new HashMap<>();
        kafkaProducerConfigProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        kafkaProducerConfigProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProducerConfigProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        kafkaProducerConfigProps.put("schema.registry.url", schemaRegistryUrl);
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfigProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTemplate<String, ChangedRecord> kafkaTemplateAvro() {
        return new KafkaTemplate<>(producerFactoryAvro());
    }

    @Bean
    public KafkaTemplate<String, StudentV2> kafkaTemplateStudentAvro() {
        return new KafkaTemplate<>(producerFactoryStudentAvro());
    }
}
