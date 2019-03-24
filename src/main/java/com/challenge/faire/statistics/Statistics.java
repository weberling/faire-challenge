package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;

/**
 * Created by weberling on 23/03/19.
 */

public class Statistics {


    MostAmountDollarOrder mostAmountDollarOrder = new MostAmountDollarOrder();

    StateMostOrder stateMostOrder = new StateMostOrder();

    BestSellingProduct bestSellingProductOption = new BestSellingProduct();

    public void addOrder(Order order) {
        OrderAmount amount = OrderAmount.getAmmount(order);
        mostAmountDollarOrder.addOrder(order, amount);
        stateMostOrder.addOrder(order, amount);
        bestSellingProductOption.addOrder(order);
    }

    public void print() {
        System.out.println(String.format("best selling product option id: %s, quantity: %d",
                bestSellingProductOption.getProductOptionIdMost(), bestSellingProductOption.getProductOptionIdMostQuantity()));
        System.out.println(String.format("largest order by dollar ammount id: %s, amount: %d",
                mostAmountDollarOrder.getMostOrderAmmountDolllar(), mostAmountDollarOrder.getMostAmmountDolllar()));
        System.out.println(String.format("the state with the most orders is state: %s, quantity: %d",
                stateMostOrder.getStateMostOrder(), stateMostOrder.getStateMostOrderQuantity()));


        System.out.println(String.format("best selling sku: %s, quantity: %d",
                bestSellingProductOption.getSkuMost(), bestSellingProductOption.getSkuMostQuantity()));
        System.out.println(String.format("the state with the most orders dollar amount is state: %s, amount: %d",
                stateMostOrder.getStateMostDollar(), stateMostOrder.getStateMostDollarAmount()));

    }


}
