package com.challenge.faire;

import com.challenge.faire.model.*;
import com.challenge.faire.service.InventoryService;
import com.challenge.faire.service.OrderService;
import com.challenge.faire.service.ProductService;
import com.challenge.faire.statistics.Statistics;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
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

    public void execute() {
        /*Map<String, ProductOption> quantityBySku = productService.getAllProductsByBrand(BRAND_ID).stream()
                .flatMap(product -> product.getOptions().stream())
                .collect(Collectors.toMap(productOption -> productOption.getId(), productOption -> productOption));*/


        Map<String, ProductOptionQuantity> quantityByProductOption = productService.getAllProductsByBrand(BRAND_ID).stream()
                .filter(Product::getActive)
                .flatMap(product -> product.getOptions().stream())
                .filter(ProductOption::getActive)
                .collect(Collectors.toMap(productOption -> productOption.getId(), productOption -> getAvailableQuantity(productOption)));

        List<Order> orders = orderService.getAllOrders();


        Statistics statistics = new Statistics();


        orders.stream().forEach(order -> {

            statistics.addOrder(order);

            if ("NEW".equals(order.getState())) {
                Map<String, BackOrderItem> backOrderItems = new HashMap<>();

                order.getItems().stream().forEach(orderItem -> {

                    int quantityAvailable = getProductQuantity(orderItem, quantityByProductOption).getQuantity();

                    // there is inventory to fulfill the order so decrement inventory
                    if (quantityAvailable != 0 && quantityAvailable >= orderItem.getQuantity().intValue()) {
                        //TODO dont forget to test this
                        quantityByProductOption.get(orderItem.getProductOptionId()).accumulate(orderItem.getQuantity());

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

        inventoryService.update(getInventoryRequest(quantityByProductOption));


        // requireds
        // best selling product option by id
        // largest order by dollar ammount
        // the state with the most orders


        // best selling sku
        // the state with the most ammount dollar
        // the state with the most quantity unit
        System.out.println(String.format("best selling product option id: %s, quantity: $d",
                statistics.getBestSellingProductOption().getProductOptionIdMost(), statistics.getBestSellingProductOption().getProductOptionIdMostQuantity()));
        System.out.println(String.format("largest order by dollar ammount id: %s, amount: $d",
                statistics.getMostAmountDollarOrder().getMostOrderAmmountDolllar(), statistics.getMostAmountDollarOrder().getMostAmmountDolllar()));
        System.out.println(String.format("the state with the most orders is state: %s, quantity: $d",
                statistics.getStateMostOrder().getStateMostOrder(), statistics.getStateMostOrder().getStateMostOrderQuantity()));


        System.out.println(String.format("best selling sku: %s, quantity: $d",
                statistics.getBestSellingProductOption().getSkuMost(), statistics.getBestSellingProductOption().getSkuMostQuantity()));
        System.out.println(String.format("the state with the most orders dollar amount is state: %s, amount: $d",
                statistics.getStateMostOrder().getStateMostDollar(), statistics.getStateMostOrder().getStateMostDollarAmount()));

    }

    private InventoryRequest getInventoryRequest(Map<String, ProductOptionQuantity> quantityByProductOption) {
        Map<String, ProductOptionQuantity> sku quantityByProductOption.values().stream().collect(
                Collectors.groupingBy(
                        ProductOptionQuantity::getSku,
                        Collectors.reducing((optionA, optionB) ->
                                new ProductOptionQuantity(optionA.getSku(), optionA.getQuantity() + optionB.getQuantity()))
                )
        );
        return new InventoryRequest(quantityBySku.entrySet().stream()
                .map(entry -> new Inventory(entry.getKey(), entry.getValue().get()))
                .collect(Collectors.toList()));
    }

    private ProductOptionQuantity getProductQuantity(OrderItem orderItem, Map<String, ProductOptionQuantity> productOptionQuantity) {
        String key = orderItem.getProductOptionId();
        ProductOptionQuantity availableQuantity = productOptionQuantity.get(key);
        if (availableQuantity == null) {
            return new ProductOptionQuantity(orderItem.getSku(), 0);
        }
        return availableQuantity;
    }

    private ProductOptionQuantity getAvailableQuantity(ProductOption productOption) {
        Integer availableQuantity = productOption.getAvailableQuantity();
        if (availableQuantity == null) {
            return new ProductOptionQuantity(productOption.getSku(), 0);
        }

        return new ProductOptionQuantity(productOption.getSku(), 0);
    }

    @Data
    @AllArgsConstructor
    public class ProductOptionQuantity {
        private String sku;
        private Integer quantity = 0;

        public void accumulate(Integer quantity) {
            this.quantity += quantity;
        }
    }

}
