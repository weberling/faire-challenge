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
public class ProductPage {

    private List<Product> products;
    private Integer page;
    private Integer limit;
}
