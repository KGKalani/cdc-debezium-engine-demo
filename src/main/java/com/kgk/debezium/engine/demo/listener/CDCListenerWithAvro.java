package com.kgk.debezium.engine.demo.listener;

import com.kgk.debezium.engine.demo.service.DestinationRouteService;
import io.confluent.connect.avro.AvroConverter;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.Avro;
import io.debezium.engine.format.ChangeEventFormat;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.connect.source.SourceRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Struct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@ConditionalOnProperty(name = "config.application.enable-debezium-cdc-engine-avro", havingValue = "true", matchIfMissing = false)
public class CDCListenerWithAvro {
    private final Logger logger = LoggerFactory.getLogger(CDCListenerWithAvro.class);
    @Autowired
    private DestinationRouteService destinationRouteService;
    /**
     * The Debezium engine which needs to be loaded with the configurations, Started and Stopped - for the
     * CDC to work.
     */
    private DebeziumEngine<ChangeEvent<byte[], byte[]>> engineInAvro;

    /**
     * Single thread which runs the Debezium engine asynchronously.
     */
    private final ExecutorService executor;
    private final ZoneId estZoneId;

    /**
     * Constructor which loads the configurations and sets a callback method 'handleEvent', which is invoked when
     * a DataBase transactional operation is performed.
     */
    @Autowired
    public CDCListenerWithAvro(@Qualifier("dbzAvroProperties") Properties dbzProperties) {
        this.executor = Executors.newSingleThreadExecutor();
        this.estZoneId = ZoneId.of("America/New_York") ;

        //---------------------Json.class with ChangeEvent<String, String>
        try(DebeziumEngine<ChangeEvent<byte[], byte[]>> engine = DebeziumEngine
                .create(Avro.class)
                .using(dbzProperties)
                .notifying(this::handleChangeEventInAvro)
//                .notifying(record -> {
//                    GenericRecord genericRecordRecordChangeEvent =  new GenericRecord();
//                    for (RecordChangeEvent<GenericRecord> recordChange : record) {
//                        GenericRecord record = recordChange.record();      // Now you can directly access the Avro record
//                        GenericRecord after = (GenericRecord) record.get("after");
//                        if (after != null) {
//                            Long id = (Long) after.get("id");
//                            String name = after.get("name").toString();
//                        }
//                    }
//                })
                .build()
        ){
            this.engineInAvro = engine;
        } catch (IOException ioException) {
            logger.error("RTIOException", ioException);
        }


    }

    //=============================Capture Single Records in Avro Format==========================================================
    private void handleChangeEventInAvro(ChangeEvent<byte[],byte[]> changeEvent) {
        logger.info("destination {}", changeEvent.destination());
        logger.info("key {}", changeEvent.key());
        logger.info("value {}", changeEvent.value());

    }

    //=============================Capture Single Records in Json Format==========================================================
    /**
     * Capture Single Records in Json Format
     * @param changeRecord : ChangeEvent
     */
    private void handleChangeEventInJson(ChangeEvent<String, String> changeRecord) {
        try {
            if (changeRecord.destination() != null) {
                if (changeRecord.destination().contains("__debezium-heartbeat")) {
                    logger.info("This is heartbeat event. source {}", changeRecord.value());
                } else {
                    //destinationRouteService.routeJson(changeRecord, Timestamp.valueOf(LocalDateTime.now(estZoneId)));
                    destinationRouteService.routeAvro(changeRecord, Timestamp.valueOf(LocalDateTime.now(estZoneId)));
                }
            }
        } catch (Exception e) {
            logger.error("Change Event Handler failed due to {} ",e.getMessage());
        }
    }

    @PostConstruct
    public void start() {
        executor.execute(engineInAvro);
    }

    /**
     * This method is called when the container is being destroyed. This stops the debezium, merging the Executor.
     */
    @PreDestroy
    public void stop() {
        logger.warn("Shutting Down The Debezium Engine !!");
        try {
            engineInAvro.close();
            executor.shutdown();
            while (!executor.awaitTermination(5, TimeUnit.SECONDS)){
                logger.info("Waiting another 5 seconds for the debezium engine to shut down");
            }
        } catch (IOException ioException) {
            logger.error("IOException Thrown by the Engine");
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        }
    }
}
