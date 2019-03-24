package com.challenge.faire.model;

import lombok.Data;

import java.util.List;

/**
 * Created by weberling on 23/03/19.
 */
@Data
public class Order {
    private String id;
    private List<OrderItem> items;
    private String state;
    private Address address;
}
