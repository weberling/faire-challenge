package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;
import lombok.Getter;

/**
 * Created by weberling on 23/03/19.
 */

public class Statistics {


    @Getter
    private MostAmountDollarOrder mostAmountDollarOrder = new MostAmountDollarOrder();
    @Getter
    private StateMostOrder stateMostOrder = new StateMostOrder();
    @Getter
    private BestSellingProduct bestSellingProductOption = new BestSellingProduct();

    public void addOrder(Order order) {
        OrderAmount amount = OrderAmount.getAmmount(order);
        mostAmountDollarOrder.addOrder(order, amount);
        stateMostOrder.addOrder(order, amount);
        bestSellingProductOption.addOrder(order);
    }


}
