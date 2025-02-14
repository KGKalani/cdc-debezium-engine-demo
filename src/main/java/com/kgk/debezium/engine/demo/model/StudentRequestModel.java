package com.kgk.debezium.engine.demo.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRequestModel {
    private int id;
    private String name;
    private Integer address_id;
    private Integer department_id;
    private String department;
    private boolean is_active;
    private LocalDate created_date;
    private LocalDate modified_date;
}
