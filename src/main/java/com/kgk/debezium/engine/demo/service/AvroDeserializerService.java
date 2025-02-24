package com.kgk.debezium.engine.demo.service;


import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class AvroDeserializerService {
    private final KafkaAvroDeserializer deserializer;

    public AvroDeserializerService(
            @Value("${spring.kafka.schema.registry.url}") String schemaRegistryUrl) {

        this.deserializer = new KafkaAvroDeserializer();

        // Create config map from application properties
        Map<String, Object> config = new HashMap<>();
        config.put("schema.registry.url", schemaRegistryUrl);
        config.put("specific.avro.reader", false);

        // Configure the deserializer
        this.deserializer.configure(config, false);
    }

}
