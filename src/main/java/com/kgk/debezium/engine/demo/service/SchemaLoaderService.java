package com.kgk.debezium.engine.demo.service;

import com.kgk.debezium.engine.demo.common.enums.SchemaType;
import org.apache.avro.Schema;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class SchemaLoaderService {

    private static final Map<String, SchemaType> LOOKUP_MAP =
            EnumSet.allOf(SchemaType.class).stream()
                    .collect(Collectors.toMap(SchemaType::getKeyword, Function.identity()));


    public static SchemaType fromSchemaType(String schemaType) {
        return LOOKUP_MAP.entrySet().stream()
                .filter(entry -> entry.getKey().contains(schemaType))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(SchemaType.DEFAULT);
    }

    public static Schema getSchema(String schemaType) throws IOException {
        // Get schema file path from the enum
        String filePath = fromSchemaType(schemaType).getFilePath();

        // Parse and return Avro schema
        return new Schema.Parser().parse(new File(filePath));
    }

    /**
     * Extracts the schema of a specific field from a given Avro schema.
     *
     * @param fullSchema The complete Avro schema.
     * @param fieldName The name of the field whose schema needs to be extracted.
     * @return The schema of the specified field, or null if the field is not found.
     */
    public static Schema extractSubSchemaByField(Schema fullSchema, String fieldName){
        if (fullSchema == null || fieldName == null || fieldName.isEmpty()) {
            throw new IllegalArgumentException("Schema and field name must not be null or empty");
        }
        Schema.Field field = fullSchema.getField(fieldName);
        if (field != null) {
            return field.schema();
        }

        return null; // Field not found in the schema
    }
}
