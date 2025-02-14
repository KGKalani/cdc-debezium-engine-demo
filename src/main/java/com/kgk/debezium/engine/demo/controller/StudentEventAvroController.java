package com.kgk.debezium.engine.demo.controller;

import com.kgk.debezium.engine.demo.model.StudentRequestModel;
import com.kgk.debezium.engine.demo.service.StudentEventAvroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

@RestController
@ConditionalOnProperty(name = "config.application.enable-rest-api", havingValue = "true", matchIfMissing = false)
public class StudentEventAvroController {

    @Autowired
    private StudentEventAvroService studentEventAvroService;

    @PostMapping("/event")
    public String sendMessageAvro(@RequestBody StudentRequestModel studentRecord){
        studentEventAvroService.sendKafkaMessage(studentRecord);
        return "Success";
    }
}
