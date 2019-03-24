package com.challenge.faire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@AllArgsConstructor
public class Inventory {
    private String sku;
    private Integer currentQuantity;
}