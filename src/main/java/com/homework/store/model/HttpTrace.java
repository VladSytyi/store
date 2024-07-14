package com.homework.store.model;

public record HttpTrace(String method, String uri, String query, String body, String trace) {
}
