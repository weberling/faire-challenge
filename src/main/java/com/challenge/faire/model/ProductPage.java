package com.challenge.faire.model;

import lombok.Data;

import java.util.List;

/**
 * Created by weberling on 23/03/19.
 */
@Data
public class ProductPage {

    private List<Product> products;
    private Integer page;
    private Integer limit;
}
