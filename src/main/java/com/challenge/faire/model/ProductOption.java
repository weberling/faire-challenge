package com.challenge.faire.model;

import lombok.Data;

/**
 * Created by weberling on 23/03/19.
 */
@Data
public class ProductOption {
    private String id;
    private String sku;
    private Boolean active;
    private Integer availableQuantity;
}
