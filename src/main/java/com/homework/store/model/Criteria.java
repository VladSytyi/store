package com.homework.store.model;

public enum Criteria {
    ID("id"),
    NAME("name"),
    BRAND("brand");

    private final String value;

    Criteria(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
