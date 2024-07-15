package com.homework.store.model;

public enum SearchCriteria {
    ID("id"),
    NAME("name"),
    BRAND("brand");

    private final String value;

    SearchCriteria(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
