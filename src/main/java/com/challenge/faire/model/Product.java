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
public class Product {
    private String id;
    private Boolean active;
    private String brandId;
    List<ProductOption> options;
}
