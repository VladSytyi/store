package com.homework.store.model;

import java.math.BigDecimal;

public record Item(Long id, String name, String brand, String description, String category,BigDecimal price) {

}
