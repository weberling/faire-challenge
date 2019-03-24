package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;
import lombok.Getter;

/**
 * Created by weberling on 23/03/19.
 */
public class MostAmountDollarOrder {

    @Getter
    private String mostOrderAmmountDolllar;
    @Getter
    private Integer mostAmmountDolllar = 0;


    public void addOrder(Order order, OrderAmount amount) {
        if (amount.getPrice() > mostAmmountDolllar) {
            mostAmmountDolllar = amount.getPrice();
            mostOrderAmmountDolllar = order.getId();
        }
    }
}
