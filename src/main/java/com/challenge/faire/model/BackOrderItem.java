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
public class BackOrderItem {
    private Integer availableQuantity;
}
