package com.kgk.debezium.engine.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Source {
    private String db;
    private String schema;
    private String table;
    private Long lsn;
    @JsonProperty("ts_ms")
    private Long tsMs;

    @Override
    public String toString() {
        return "Source{" +
                "db='" + db + '\'' +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", lsn=" + lsn +
                '}';
    }
}
