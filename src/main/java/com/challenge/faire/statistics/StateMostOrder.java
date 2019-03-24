package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weberling on 23/03/19.
 */
public class StateMostOrder {
    private Map<String, Integer> stateOrdersQuantity = new HashMap<>();
    private Map<String, Integer> stateMostDollarAmountMap = new HashMap<>();

    @Getter
    private String stateMostOrder;
    @Getter
    private Integer stateMostOrderQuantity = 0;

    @Getter
    private String stateMostDollar;
    @Getter
    private Integer stateMostDollarAmount = 0;


    public void addOrder(Order order, OrderAmount amount) {
        handleMostOrderQuantity(order);
        handleDollarAmount(order, amount);
    }

    private void handleDollarAmount(Order order, OrderAmount amount) {
        String state = order.getAddress().getState();
        Integer stateAmount = stateMostDollarAmountMap.get(state);

        if (stateAmount == null) {
            stateAmount = amount.getPrice();
            stateMostDollarAmountMap.put(state, stateAmount);
        } else {
            stateAmount += amount.getPrice();
            stateMostDollarAmountMap.put(state, stateAmount);
        }

        if (stateAmount > stateMostDollarAmount) {
            stateMostDollarAmount = stateAmount;
            stateMostDollar = state;
        }
    }

    private void handleMostOrderQuantity(Order order) {
        String state = order.getAddress().getState();
        Integer stateQuantity = stateOrdersQuantity.get(state);

        if (stateQuantity == null) {
            stateQuantity = 1;
            stateOrdersQuantity.put(state, stateQuantity);
        } else {
            stateOrdersQuantity.put(state, ++stateQuantity);
        }

        if (stateQuantity > stateMostOrderQuantity) {
            stateMostOrderQuantity = stateQuantity;
            stateMostOrder = state;
        }
    }
}
