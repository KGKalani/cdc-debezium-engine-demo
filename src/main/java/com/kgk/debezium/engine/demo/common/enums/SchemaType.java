package com.kgk.debezium.engine.demo.common.enums;

import lombok.Getter;

@Getter
public enum SchemaType {
    STUDENT("demo.students", "src/main/resources/avro/StudentChangedRecord.avsc"),
    STUDENTV2("demo.studentv2", "src/main/resources/avro/studentv2.avsc"),
    GENERIC("generic", "src/main/resources/avro/GenericChangedRecord.avsc"),
    DEFAULT("default", "src/main/resources/avro/GenericChangedRecord.avsc");

    private final String keyword;
    private final String filePath;

    SchemaType(String keyword, String filePath) {
        this.keyword = keyword;
        this.filePath = filePath;
    }
}
