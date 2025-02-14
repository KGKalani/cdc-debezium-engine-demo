package com.kgk.debezium.engine.demo.common.util;

import com.kgk.debezium.engine.demo.model.Source;

public record TargetChangedDataRecord (Source source, String operation, Long tsMs, Object data){}
