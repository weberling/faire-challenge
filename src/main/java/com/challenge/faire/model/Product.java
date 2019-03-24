package com.challenge.faire.model;

import lombok.Data;

import java.util.List;

/**
 * Created by weberling on 23/03/19.
 */
@Data
public class Product {
    private String id;
    private Boolean active;
    private String brandId;
    List<ProductOption> options;
}
