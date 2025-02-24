package com.kgk.debezium.engine.demo.common.util;

import com.kgk.debezium.engine.demo.model.Source;

public record TargetChangeRecord(Source source, String operation, Long tsMs, Object data){}
