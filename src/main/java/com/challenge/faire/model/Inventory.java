package com.challenge.faire.model;

import lombok.*;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Inventory {
    private String sku;
    private Integer currentQuantity;
}