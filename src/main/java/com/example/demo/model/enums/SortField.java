package com.example.demo.model.enums;

import lombok.Getter;

@Getter
public enum SortField {

    ID("id"),
    NAME("name"),
    COUNTRY("country"),
    BIRTHDATE("birthDate");

    private final String fieldName;

    SortField(String fieldName){
        this.fieldName = fieldName;
    }
}