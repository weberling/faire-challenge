package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;
import lombok.Getter;

/**
 * Created by weberling on 23/03/19.
 */
public class MostAmountDollarOrder {

    @Getter
    private EntityStatistics mostOrderAmmountDolllar = new EntityStatistics();


    public void addOrder(Order order, OrderAmount amount) {
        if (amount.getPrice() > mostOrderAmmountDolllar.getValue()) {
            mostOrderAmmountDolllar.setValue(amount.getPrice());
            mostOrderAmmountDolllar.setKey(order.getId());
        }
    }
}
