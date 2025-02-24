package com.kgk.debezium.engine.demo.common.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;

/**
 * This class is responsible for transforming JSON strings to handle:
 * <ul>
 *   <li><b>1. Union Fields</b> (fields that are part of a union type, e.g., ["null", "string"])</li>
 *   <li>Example: Converts a JSON field like <code>"name": "Peter"</code> to <code>"name": {"string": "Peter"}</code></li>
 *   <li><b>2. Missing Fields</b>Handle missing fields by setting default values and set them to Json String</li>
 * </ul>
 */
public class JsonTransformer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Transforms a JSON string to correctly handle Avro union types and missing fields.
     *
     * <p>This method processes the input JSON string by ensuring that union types are
     * properly formatted according to the provided Avro schema. Additionally, it fills
     * in missing fields with their default values as defined in the schema.</p>
     *
     * @param schema The Avro schema that defines the structure of the data.
     * @param jsonString The input JSON string that needs to be transformed.
     * @return  A transformed JSON string that conforms to the Avro schema.
     * @throws JsonProcessingException JsonProcessingException If there is an error processing the JSON string.
     */
    public static String transformJsonForUnionTypesAndMissingFields(Schema schema, String jsonString) throws JsonProcessingException {
        // Parse the JSON string into a JsonNode
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // Transform union fields
        transformUnionFieldsAndMissingFields(schema, rootNode);

        // Convert the transformed JsonNode back to a JSON string
        return objectMapper.writeValueAsString(rootNode);
    }

    /**
     * Transform union fields and handle missing fields
     * @param node: JsonNode
     * @param schema: Schema
     */
    private static void transformUnionFieldsAndMissingFields(Schema schema, JsonNode node) {
        if(node.isObject()){
            ObjectNode objectNode = (ObjectNode) node;

            // Iterate over the fields in the schema
            for (Schema.Field field : schema.getFields()) {
                String fieldName = field.name();
                JsonNode fieldValue = objectNode.get(fieldName);

                // Add default values for missing fields in json string
                if (null == fieldValue && field.hasDefaultValue()) {
                    Object defaultValue = GenericData.get().getDefaultValue(field);
                    JsonNode defaultJsonNode = objectMapper.valueToTree(defaultValue);
                    objectNode.set(fieldName, defaultJsonNode);
                }

                // Handle UnionTypes
                if (null != fieldValue && field.schema().getType() == Schema.Type.UNION) {
                    // Wrap the value in the required format
                    ObjectNode wrappedValue = objectMapper.createObjectNode();
                    String typeName = getUnionTypeName(fieldValue);
                    wrappedValue.set(typeName, fieldValue);
                    objectNode.set(fieldName, wrappedValue);
                }

            }
        }
    }

    /***
     * Get Union Type Name
     * @param value:JsonNode : Field Value
     * @return : String : Union Type Name
     */
    private static String getUnionTypeName(JsonNode value) {
        if (value.isTextual()) {
            return "string";
        } else if (value.isInt()) {
            return "int";
        } else if (value.isLong()) {
            return "long";
        } else if (value.isBoolean()) {
            return "boolean";
        } else if (value.isNull()) {
            return "null";
        } else {
            throw new IllegalArgumentException("Unsupported type: " + value.getNodeType());
        }
    }
}
