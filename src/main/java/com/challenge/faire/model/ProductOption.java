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
public class ProductOption {
    private String id;
    private String sku;
    private Boolean active;
    private Integer availableQuantity;
    private String productId;
}
