package com.challenge.faire.service;

import com.challenge.faire.model.*;
import com.challenge.faire.statistics.Statistics;
import com.challenge.faire.statistics.StatisticsDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by weberling on 23/03/19.
 */

// TODO DOUBTS
// AMOUNT DOLLAR IS QUANTITY MULTIPLE PRICE?
// IF THE MOST ANYTHING* HAS 2 OR MORE ITEMS, WHAT SHOULD I PRINT?
// WHAT IS unit_multiplier
@Component
public class SolutionService {

    private static String BRAND_ID = "b_d2481b88";
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private InventoryService inventoryService;


    public void consumeAndStatistics() {

        List<Product> products = productService.getAllProductsByBrand(BRAND_ID);

        Map<String, Integer> skuQuantity = mapActiveProductOptionsAvailableBySku(products);


        List<Order> orders = orderService.getAllOrders();


        Statistics statistics = new Statistics();
        AtomicBoolean updateInventory = new AtomicBoolean(Boolean.FALSE);


        orders.stream().forEach(order -> {
            statistics.addOrder(order);
            boolean updated = consumeOrder(skuQuantity, order);
            if(updated){
                updateInventory.set(updated);
            }
        });

        statistics.print();
        if(updateInventory.get()) {
            // TODO Im not sure id I have to update inventory, how I know the most recent sku?
            inventoryService.update(getInventoryRequest(skuQuantity));
        }

    }

    public void consume() {

        List<Product> products = productService.getAllProductsByBrand(BRAND_ID);

        Map<String, Integer> skuQuantity = mapActiveProductOptionsAvailableBySku(products);

        List<Order> orders = orderService.getAllOrders();

        orders.stream().map(order -> consumeOrder(skuQuantity, order)).filter(Boolean::booleanValue).findFirst().ifPresent(update -> {
                inventoryService.update(getInventoryRequest(skuQuantity));
        });


    }

    public StatisticsDto statistics() {

        List<Order> orders = orderService.getAllOrders();

        Statistics statistics = new Statistics();


        orders.stream().forEach(order -> statistics.addOrder(order));

        return statistics.getStatistics();

    }

    private boolean consumeOrder(Map<String, Integer> skuQuantity, Order order) {
        if ("NEW".equals(order.getState())) {
            Map<String, BackOrderItem> backOrderItems = new HashMap<>();

            order.getItems().stream().forEach(orderItem -> {

                String key = getProductKey(orderItem);
                int quantityAvailable = getProductQuantity(key, skuQuantity);

                // there is inventory to fulfill the order so decrement inventory
                if (quantityAvailable != 0 && quantityAvailable >= orderItem.getQuantity().intValue()) {
                    skuQuantity.put(key, quantityAvailable - orderItem.getQuantity());

                    // item has no enough inventory
                } else {
                    backOrderItems.put(orderItem.getId(), new BackOrderItem(orderItem.getQuantity()));
                }
            });

            // accept order
            if (CollectionUtils.isEmpty(backOrderItems.entrySet())) {
                orderService.acceptOrder(order.getId());
                return true;

            } else {
                // there is item without fulfill
                orderService.backOrderItem(order.getId(), backOrderItems);
                return false;
            }


        }
        return false;
    }

    private Map<String, Integer> mapActiveProductOptionsAvailableBySku(List<Product> products) {
        return products.stream()
                .filter(Product::getActive)
                .flatMap(product -> product.getOptions().stream())
                .filter(ProductOption::getActive)
                .filter(productOption -> productOption.getAvailableQuantity() != null)
                .collect(Collectors.toMap(this::getProductKey, productOption -> productOption.getAvailableQuantity()));

    }

    private String getProductKey(OrderItem orderItem){
        return getProductKey(orderItem.getProductId(), orderItem.getProductOptionId(), orderItem.getSku());
    }

    private String getProductKey(ProductOption productOption){
        return getProductKey(productOption.getProductId(), productOption.getId(), productOption.getSku());
    }

    private String getProductKey(String productId, String productOptionId, String sku){
        return String.format("%s_%s", productId, productOptionId);
    }

    private InventoryRequest getInventoryRequest(Map<String, Integer> quantityBySku) {
        return new InventoryRequest(quantityBySku.entrySet().stream()
                .map(entry -> new Inventory(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList()));
    }

    private Integer getProductQuantity(String key, Map<String, Integer> skuQuantity) {
        Integer availableQuantity = skuQuantity.get(key);
        return availableQuantity != null ? availableQuantity : 0;
    }

}
