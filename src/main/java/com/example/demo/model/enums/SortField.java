package com.example.demo.model.enums;

import lombok.Getter;

public enum SortField {

    ID("id"),
    NAME("name"),
    COUNTRY("country"),
    BIRTH_DATE("birth_date");

    @Getter
    private final String fieldName;

    private SortField(String fieldName){
        this.fieldName = fieldName;
    }
}