package com.kgk.debezium.engine.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class DBZEngineConfiguration {

    @Value("${config.debezium.engine.name}")
    private String NAME;

    @Value("${config.debezium.topic.prefix}")
    private String TOPIC_PREFIX;

    @Value("${spring.kafka.bootstrap.servers}")
    private String BOOTSTRAP_SERVERS;

    @Value("${config.debezium.kafka.offset.storage}")
    private String OFFSET_STORAGE;

    @Value("${config.debezium.kafka.offset.storage.partitions}")
    private String OFFSET_STORAGE_PARTITIONS;

    @Value("${config.debezium.kafka.offset.storage.topic}")
    private String OFFSET_STORAGE_TOPIC;

    @Value("${config.debezium.kafka.offset.storage.replication.factor}")
    private String OFFSET_STORAGE_REPLICATION_FACTOR;

    @Value("${config.debezium.kafka.offset.flush.interval.ms}")
    private String OFFSET_FLUSH_INTERVAL_MS;

    @Value("${config.debezium.database.connector.class}")
    private String CONNECTOR_CLASS;

    @Value("${config.debezium.database.plugin.name}")
    private String PLUGIN_NAME;

    @Value("${config.debezium.database.snapshot.mode}")
    private String SNAPSHOT_MODE;

    @Value("${config.debezium.database.slot.name}")
    private String SLOT_NAME;

    @Value("${config.debezium.database.publication.name}")
    private String PUBLICATION_NAME;

    @Value("${config.debezium.database.publication.auto.create.mode}")
    private String PUBLICATION_AUTO_CREATE_MODE;

    @Value("${config.debezium.database.hostname}")
    private String DATABASE_HOSTNAME;

    @Value("${config.debezium.database.port}")
    private int DATABASE_PORT;

    @Value("${config.debezium.database.user}")
    private String DATABASE_USER;

    @Value("${config.debezium.database.password}")
    private String DATABASE_PASSWORD;

    @Value("${config.debezium.database.dbname}")
    private String DATABASE_DBNAME;

    @Value("${config.debezium.database.schema.include.list}")
    private String SCHEMA_INCLUDE_LIST;

    @Value("${config.debezium.database.table.include.list}")
    private String TABLE_INCLUDE_LIST;

    @Value("${config.debezium.database.column.exclude.list}")
    private String COLUMN_EXCLUDE_LIST;

    @Value("${config.debezium.database.common.tables.slot_drop_on_stop}")
    private String SLOT_DROP_ON_STOP;

    @Value(("${config.debezium.database.decimal.handling.mode}"))
    private String DECIMAL_HANDLING_MODE;

    @Value("${config.debezium.converter.schemas.enable}")
    private String CONVERTER_SCHEMAS_ENABLE;

    @Bean("dbzProperties")
    //@Primary
    public Properties props() {
        return io.debezium.config.Configuration.create()
                /*
                    GENERAL SETTINGS
                 */
                .with("name", NAME)
                .with("topic.prefix", TOPIC_PREFIX)
                .with("heartbeat.interval.ms", "30000")

                /*
                    OFFSET SETTINGS - AS A KAFKA TOPIC
                 */
                .with("bootstrap.servers", BOOTSTRAP_SERVERS)
                .with("offset.storage", OFFSET_STORAGE)
                .with("offset.storage.partitions", OFFSET_STORAGE_PARTITIONS)
                .with("offset.storage.topic", OFFSET_STORAGE_TOPIC)
                .with("offset.storage.replication.factor", OFFSET_STORAGE_REPLICATION_FACTOR)
                .with("offset.flush.interval.ms", OFFSET_FLUSH_INTERVAL_MS)

                /*
                    DATABASE SETTINGS
                 */
                .with("connector.class", CONNECTOR_CLASS)
                .with("plugin.name", PLUGIN_NAME)
                .with("snapshot.mode", SNAPSHOT_MODE)
                .with("slot.name", SLOT_NAME)
                .with("publication.name", PUBLICATION_NAME)
                .with("publication.autocreate.mode", PUBLICATION_AUTO_CREATE_MODE)
                .with("slot.drop.on.stop", SLOT_DROP_ON_STOP)
                .with("postgresql.server.name", DATABASE_HOSTNAME + "-" + DATABASE_DBNAME)
                .with("database.hostname", DATABASE_HOSTNAME)
                .with("database.port", DATABASE_PORT)
                .with("database.user", DATABASE_USER)
                .with("database.password", DATABASE_PASSWORD)
                .with("database.dbname", DATABASE_DBNAME)
                //.with("database.sslcert", SSL_CERT)
                //.with("database.sslmode", SSL_MODE)
                .with("schema.include.list", SCHEMA_INCLUDE_LIST)
                .with("table.include.list", TABLE_INCLUDE_LIST)
                //.with("column.exclude.list", COLUMN_EXCLUDE_LIST)
                .with("decimal.handling.mode", DECIMAL_HANDLING_MODE)
                .with("time.precision.mode","connect")
                .with("skip.messages.without.change", "true")
                .with("include.before", "true") //if we need before record we need to set this as true

                /*
                    MESSAGE SETTINGS
                 */
                .with("converter.schemas.enable", CONVERTER_SCHEMAS_ENABLE)
                .with("enable.idempotence", "false")
                .build()
                .asProperties();
    }

}
