package com.challenge.faire.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class BackOrderItem {
    private Integer availableQuantity;
}
