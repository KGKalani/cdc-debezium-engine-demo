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
@ConditionalOnProperty(name = "config.application.enable-debezium-cdc-engine-json", havingValue = "true", matchIfMissing = false)
public class CDCListenerWithJson {
    private final Logger logger = LoggerFactory.getLogger(CDCListenerWithJson.class);
    @Autowired
    private DestinationRouteService destinationRouteService;
    /**
     * The Debezium engine which needs to be loaded with the configurations, Started and Stopped - for the
     * CDC to work.
     */
    private DebeziumEngine<ChangeEvent<String, String>> engineInJson;


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
    public CDCListenerWithJson(@Qualifier("dbzProperties") Properties dbzProperties) {
        this.executor = Executors.newSingleThreadExecutor();
        this.estZoneId = ZoneId.of("America/New_York") ;

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
        executor.execute(engineInJson);
    }

    /**
     * This method is called when the container is being destroyed. This stops the debezium, merging the Executor.
     */
    @PreDestroy
    public void stop() {
        logger.warn("Shutting Down The Debezium Engine !!");
        try {
            engineInJson.close();
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
