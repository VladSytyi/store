package com.homework.store.model;

import java.math.BigDecimal;

public record Item(Long id, String name, BigDecimal price, String description) {

}
