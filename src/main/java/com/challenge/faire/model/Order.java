package com.challenge.faire.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@Builder
public class Order {
    private String id;
    private List<OrderItem> items;
    private String state;
    private Address address;
}
