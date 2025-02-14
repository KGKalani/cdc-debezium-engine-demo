package com.kgk.debezium.engine.demo.service;

import com.kgk.debezium.engine.demo.dto.StudentV2;
import com.kgk.debezium.engine.demo.model.StudentRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class StudentEventAvroService {

    @Value("${spring.kafka.topic-student-avro}")
    private String topic;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public void sendKafkaMessage(StudentRequestModel studentRequestModel){
        StudentV2 studentRecord = StudentV2.newBuilder()
                .setId(studentRequestModel.getId())
                .setName(studentRequestModel.getName())
//                .setFullName(studentRequestModel.getName())
                .setAddressId(studentRequestModel.getAddress_id())
                .setDepartmentId(studentRequestModel.getDepartment_id())
                .setDepartment(studentRequestModel.getDepartment())
                .setIsActive(studentRequestModel.is_active())
                .setCreatedDate(LocalDate.now())
                .setModifiedDate(LocalDate.now()).build();
        kafkaProducerService.sendStudentMessageAvro(topic, null, String.valueOf(studentRecord.getId()), studentRecord);
    }
}
