package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weberling on 23/03/19.
 */
public class StateMost {
    private Map<String, Integer> stateOrdersQuantity = new HashMap<>();
    private Map<String, Integer> stateMostDollarAmountMap = new HashMap<>();

    @Getter
    private EntityStatistics stateMostOrder = new EntityStatistics();

    @Getter
    private EntityStatistics stateMostDollar = new EntityStatistics();


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

        if (stateAmount > stateMostDollar.getValue()) {
            stateMostDollar.setValue(stateAmount);
            stateMostDollar.setKey(state);
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

        if (stateQuantity > stateMostOrder.getValue()) {
            stateMostOrder.setValue(stateQuantity);
            stateMostOrder.setKey(state);
        }
    }
}
