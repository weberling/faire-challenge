package com.challenge.faire.model;

import lombok.Builder;
import lombok.Data;

/**
 * Created by weberling on 23/03/19.
 */
@Data
@Builder
public class OrderItem {
    private String id;
    private String productId;
    private String productOptionId;
    private Integer quantity;
    private Integer priceCents;
    private String sku;
}
