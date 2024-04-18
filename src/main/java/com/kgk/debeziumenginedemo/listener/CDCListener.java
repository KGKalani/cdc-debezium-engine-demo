package com.kgk.debeziumenginedemo.listener;

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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Struct;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class CDCListener {
    private final Logger logger = LoggerFactory.getLogger(CDCListener.class);
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

    /**
     * Constructor which loads the configurations and sets a callback method 'handleEvent', which is invoked when
     * a DataBase transactional operation is performed.
     */
    @Autowired
    public CDCListener(Properties dbzProperties) {

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

        //---------------------Json.class with ChangeEvent<SourceRecord, SourceRecord>
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

        this.executor = Executors.newSingleThreadExecutor();
    }

    /**
     * Capture Single Records
     * @param changeRecord
     */
    private void handleChangeEventInJson(ChangeEvent<String, String> changeRecord) {
        try {
            if (changeRecord.destination() != null) {
                if (changeRecord.destination().contains("__debezium-heartbeat")) {
                    logger.info("This is heartbeat event. source {}", changeRecord.value());
                } else {
                    logger.info("Change Record: "+ changeRecord);
                }
            }
        } catch (Exception e) {
            logger.error("Change Event Handler failed due to {} ",e.getMessage());
        }
    }
    //=======================================================================================
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

//======================================================================================================
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

//========================================================================================

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

//    private void handleChangeEvent(List<ChangeEvent<String, String>> records, DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) throws InterruptedException {
//        log.info("*** records: {} ", records);
//        for (ChangeEvent<String, String> r : records) {
//            try {
//                log.info("*** record: {} ", r);
//                DBRecord record = mapper.readerFor(DBRecord.class).readValue(r.value());
//                if (record.isHeartBeat()) {
//                    log.info("heartbeat, ts_ms : \"{}\"", record.getTs_ms());
//                    continue;
//                }
//                if (record.getSource().getTable().equalsIgnoreCase("ftp_current")) {
//                    //TODO
//                    log.info("ftp_current");
//                }
//                log.info("{}", record);
//            } catch (JsonProcessingException jsonProcessingException) {
//                log.error("error deserializing to DBRecord, record {}, exception", r, jsonProcessingException);
//            } finally {
//                committer.markProcessed(r);
//            }
//        }
//    }



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
