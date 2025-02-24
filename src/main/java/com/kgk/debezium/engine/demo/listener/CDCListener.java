package com.kgk.debezium.engine.demo.listener;

import com.kgk.debezium.engine.demo.service.DestinationRouteService;
import io.debezium.embedded.Connect;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import io.debezium.engine.format.Json;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
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
@ConditionalOnProperty(name = "config.application.enable-debezium-cdc-engine", havingValue = "true", matchIfMissing = false)
public class CDCListener {
    private final Logger logger = LoggerFactory.getLogger(CDCListener.class);
    @Autowired
    private DestinationRouteService destinationRouteService;
    /**
     * The Debezium engine which needs to be loaded with the configurations, Started and Stopped - for the
     * CDC to work.
     */
    private DebeziumEngine<ChangeEvent<String, String>> engineInJson;
    private DebeziumEngine<ChangeEvent<SourceRecord, SourceRecord>> engineInConnect;
    private DebeziumEngine<RecordChangeEvent<SourceRecord>> engineInBatch;

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
    public CDCListener(@Qualifier("dbzProperties") Properties dbzProperties) {
        this.executor = Executors.newSingleThreadExecutor();
        this.estZoneId = ZoneId.of("America/New_York") ;

        try (DebeziumEngine<RecordChangeEvent<SourceRecord>> engine = DebeziumEngine
                .create(ChangeEventFormat.of(Connect.class))
                .using(dbzProperties)
                .notifying(this::handleChangeEventInBatch)
                .build()
        ) {
            this.engineInBatch = engine;
        } catch (IOException ioException) {
            logger.error("RTIOException", ioException);
        }

        //---------------------Connect.class with ChangeEvent<SourceRecord, SourceRecord>
        try(DebeziumEngine<ChangeEvent<SourceRecord, SourceRecord>> engine = DebeziumEngine
                .create(Connect.class)
                .using(dbzProperties)
                .notifying(this::handleChangeEventInConnectFormat)
                .build()
        ){
            this.engineInConnect = engine;
        } catch (IOException ioException) {
            logger.error("RTIOException", ioException);
        }

        //---------------------Json.class with ChangeEvent<String, String>
        try(DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine
                .create(Json.class)
                .using(dbzProperties)
                .notifying(this::handleChangeEventInJson)
                .build()
        ){
            this.engineInJson = engine;
        } catch (IOException ioException) {
            logger.error("RTIOException", ioException);
        }


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
                    destinationRouteService.routeJson(changeRecord, Timestamp.valueOf(LocalDateTime.now(estZoneId)));
                 }
            }
        } catch (Exception e) {
            logger.error("Change Event Handler failed due to {} ",e.getMessage());
        }
    }

    //========================Batch process : Capturing Batch Events in Json Format====================================================
    /**
     * Json.class
     * Capturing Change Event in Json format
     */
 /*   private void handleChangeEventJsonList(List<ChangeEvent<String, String>> changeEvents, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> changeEventRecordCommitter) throws InterruptedException {
        logger.info("Record Count {} ", changeEvents.size());
        try {
            logger.info("Start sleep {} ", Instant.now());
            sleep(30000);
            logger.info("End sleep {} ", Instant.now());
            for (ChangeEvent<String, String> changeRecord : changeEvents) {
                if (changeRecord.destination() != null) {
                    if (changeRecord.destination().contains("__debezium-heartbeat")) {
                        logger.info("This is heartbeat event. source {}", changeRecord.value());

                    } else {
                        destinationRouteService.route(changeRecord, changeEventRecordCommitter);
                    }
                }
//                logger.info("Changed Record : {}", changeRecord);
//                logger.info("Key: {}", changeRecord.key());
//                logger.info("Value: {}", changeRecord.value());
//                logger.info("destination: {}", changeRecord.destination());
//                logger.info("headers: {}", changeRecord.headers());
//                logger.info("partition: {}", changeRecord.partition());
            }
        } catch (Exception e) {
            logger.error("Change Event Handler failed due to {} ",e.getMessage());
        }finally {
            changeEventRecordCommitter.markBatchFinished();
        }

    }*/

//==================================Capturing Change Event in Connect Class format====================================================================
    /**
     * Connect.class
     * Capturing Change Event in Connect Class format
     */
    private void handleChangeEventInConnectFormat(List<ChangeEvent<SourceRecord, SourceRecord>> changeEvents, DebeziumEngine.RecordCommitter<ChangeEvent<SourceRecord, SourceRecord>> changeEventRecordCommitter) {
        logger.info("Changed Record Size: {}", changeEvents.size());
        for(ChangeEvent<SourceRecord, SourceRecord> changeRecord : changeEvents){
            logger.info("Changed Record : {}", changeRecord);
            logger.info("Key: {}" , changeRecord.key());
            logger.info("Value: {}", changeRecord.value());

            Struct sourceRecordValue = (Struct) changeRecord.value().value();
            logger.info("sourceRecordValue : "+ sourceRecordValue);
        }
    }

//====================================Batch process : Capturing Batch Events====================================================
    /**
     * Batch process
     * Capturing Batch Events
      */
    private void handleChangeEventInBatch(List<RecordChangeEvent<SourceRecord>> recordChangeEvents, DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordChangeEventRecordCommitter) {
        logger.info("recordChangeEvents Size : {} ", recordChangeEvents.size());
        for (RecordChangeEvent<SourceRecord> changeRecord : recordChangeEvents) {
            logger.info("Changed Record : {}", changeRecord);
            logger.info("Value: {}", changeRecord.record().value());

            //  Struct sourceRecordValue = (Struct) changeRecord.value().value();
        }
    }

    @PostConstruct
    public void start() {
        executor.execute(engineInJson);
        //executor.execute(engineInConnect);
        //executor.execute(engineInBatch);
    }

    /**
     * This method is called when the container is being destroyed. This stops the debezium, merging the Executor.
     */
    @PreDestroy
    public void stop() {
        logger.warn("Shutting Down The Debezium Engine !!");
        try {
            engineInJson.close();
            //engineInConnect.close();
            //engineInBatch.close();
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
