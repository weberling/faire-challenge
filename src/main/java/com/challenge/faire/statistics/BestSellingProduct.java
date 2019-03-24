package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;
import com.challenge.faire.model.OrderItem;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weberling on 23/03/19.
 */
public class BestSellingProduct {
    private Map<String, Integer> productOptionsQuantity = new HashMap<>();
    private Map<String, Integer> skusQuantity = new HashMap<>();

    @Getter
    private String productOptionIdMost;
    @Getter
    private Integer productOptionIdMostQuantity = 0;

    @Getter
    private String skuMost;
    @Getter
    private Integer skuMostQuantity = 0;


    public void addOrder(Order order) {

        order.getItems().stream().forEach(item -> {
            handleMostProductOption(item);
            handleMostSku(item);
        });

    }

    private void handleMostProductOption(OrderItem item) {
        String productOptionId = item.getProductOptionId();
        Integer productOptionQuantity = productOptionsQuantity.get(productOptionId);

        if (productOptionQuantity == null) {
            productOptionQuantity = 1;
            productOptionsQuantity.put(productOptionId, productOptionQuantity);
        } else {
            productOptionsQuantity.put(productOptionId, ++productOptionQuantity);
        }

        if (productOptionQuantity > productOptionIdMostQuantity) {
            productOptionIdMostQuantity = productOptionQuantity;
            productOptionIdMost = productOptionId;
        }
    }

    private void handleMostSku(OrderItem item) {
        String sku = item.getSku();
        Integer skuQuantity = skusQuantity.get(sku);

        if (skuQuantity == null) {
            skuQuantity = 1;
            skusQuantity.put(sku, skuQuantity);
        } else {
            skusQuantity.put(sku, ++skuQuantity);
        }

        if (skuQuantity > skuMostQuantity) {
            skuMostQuantity = skuQuantity;
            skuMost = sku;
        }
    }
}
