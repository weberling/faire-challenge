package com.challenge.faire;

import com.challenge.faire.model.*;
import com.challenge.faire.service.InventoryService;
import com.challenge.faire.service.OrderService;
import com.challenge.faire.service.ProductService;
import com.challenge.faire.statistics.Statistics;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by weberling on 23/03/19.
 */
@Component
public class Solution {

    private static String BRAND_ID = "b_d2481b88";
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;


    // DOUBTS
    // SAME SKU FOR DIFFERENTS PRODUCTS?
    // AMOUNT DOLLAR IS QUANTITY MULTIPLE PRICE?
    // IF THE MOST ANYTHING* HAS 2 OR MORE ITEMS, WHAT SHOULD I PRINT?
    public void execute() {

        List<Product> products = productService.getAllProductsByBrand(BRAND_ID);

        Map<String, Integer> skuQuantity = products.stream()
                .filter(Product::getActive)
                .flatMap(product -> product.getOptions().stream())
                .filter(ProductOption::getActive)
                .filter(productOption -> productOption.getAvailableQuantity() != null)
                .filter(productOption -> productOption.getAvailableQuantity() >= 0)
                .filter(productOption -> StringUtils.isNotBlank(productOption.getSku()))
                .collect(Collectors.toMap(productOption -> productOption.getSku(), productOption -> productOption.getAvailableQuantity()));


        List<Order> orders = orderService.getAllOrders();


        Statistics statistics = new Statistics();


        orders.stream().forEach(order -> {

            statistics.addOrder(order);

            if ("NEW".equals(order.getState())) {
                Map<String, BackOrderItem> backOrderItems = new HashMap<>();

                order.getItems().stream().forEach(orderItem -> {

                    int quantityAvailable = getProductQuantity(orderItem.getSku(), skuQuantity);

                    // there is inventory to fulfill the order so decrement inventory
                    if (quantityAvailable != 0 && quantityAvailable >= orderItem.getQuantity().intValue()) {
                        skuQuantity.put(orderItem.getSku(), quantityAvailable - orderItem.getQuantity());

                    // item has no enough inventory
                    } else {
                        backOrderItems.put(orderItem.getId(), new BackOrderItem(orderItem.getQuantity()));
                    }
                });

                // accept order
                if (CollectionUtils.isEmpty(backOrderItems.entrySet())) {
                    orderService.acceptOrder(order.getId());

                } else {
                    // there is item without fulfill
                    orderService.backOrderItem(order.getId(), backOrderItems);
                }


            }
        });

        statistics.print();
        inventoryService.update(getInventoryRequest(skuQuantity));

    }

    private InventoryRequest getInventoryRequest(Map<String, Integer> quantityBySku) {
        return new InventoryRequest(quantityBySku.entrySet().stream()
                .map(entry -> new Inventory(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
    }

    private Integer getProductQuantity(String sku, Map<String, Integer> skuQuantity) {
        Integer availableQuantity = skuQuantity.get(sku);
        return availableQuantity != null ? availableQuantity : 0;
    }

}
