package com.assessment.orderbook.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Instrument {
    private final Long id;
    private final String name;
}
