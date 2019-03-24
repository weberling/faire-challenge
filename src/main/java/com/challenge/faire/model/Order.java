package com.challenge.faire.model;

import lombok.*;

import java.util.List;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Order {
    private String id;
    private List<OrderItem> items;
    private String state;
    private Address address;
}
