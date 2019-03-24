package com.challenge.faire.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@AllArgsConstructor
public class Inventory {
    private String sku;
    private Integer currentQuantity;
}