package com.kgk.debezium.engine.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBRecord<T> {
    Source source;

    @JsonProperty("op")
    String operation;

    @JsonProperty("ts_ms")
    Long tsMs;

    @JsonProperty("after")
    T afterData;

    @JsonProperty("before")
    T beforeData;

    @Override
    public String toString() {
        return "DBRecord{" +
                "source=" + source +
                ", operation='" + operation + '\'' +
                ", ts_ms=" + tsMs +
                '}';
    }
}
