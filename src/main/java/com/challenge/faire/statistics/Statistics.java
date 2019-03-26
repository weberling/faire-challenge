package com.challenge.faire.statistics;

import com.challenge.faire.model.Order;

/**
 * Created by weberling on 23/03/19.
 */
public class Statistics {


    MostAmountDollarOrder mostAmountDollarOrder = new MostAmountDollarOrder();

    StateMost stateMost = new StateMost();

    BestSellingProduct bestSellingProductOption = new BestSellingProduct();

    public void addOrder(Order order) {
        OrderAmount amount = OrderAmount.getAmmount(order);
        mostAmountDollarOrder.addOrder(order, amount);
        stateMost.addOrder(order, amount);
        bestSellingProductOption.addOrder(order);
    }

    public void print() {
        System.out.println(String.format("best selling product option id: %s, quantity: %d",
                bestSellingProductOption.getProductOptionIdMost().getKey(), bestSellingProductOption.getProductOptionIdMost().getValue()));
        System.out.println(String.format("largest order by dollar ammount id: %s, amount: %d",
                mostAmountDollarOrder.getMostOrderAmmountDolllar().getKey(), mostAmountDollarOrder.getMostOrderAmmountDolllar().getValue()));
        System.out.println(String.format("the state with the most orders is state: %s, quantity: %d",
                stateMost.getStateMostOrder().getKey(), stateMost.getStateMostOrder().getValue()));


        System.out.println(String.format("best selling sku: %s, quantity: %d",
                bestSellingProductOption.getSkuMost().getKey(), bestSellingProductOption.getSkuMost().getValue()));
        System.out.println(String.format("the state with the most orders dollar amount is state: %s, amount: %d",
                stateMost.getStateMostDollar().getKey(), stateMost.getStateMostDollar().getValue()));

    }

    public StatisticsDto getStatistics(){
        return StatisticsDto.builder()
                .bestSellingProductOption(bestSellingProductOption.getProductOptionIdMost())
                .bestSellingSku(bestSellingProductOption.getSkuMost())
                .stateMostAmountOrder(stateMost.getStateMostDollar())
                .stateMostOrder(stateMost.getStateMostOrder())
                .largestOrderByDollarAmmount(mostAmountDollarOrder.getMostOrderAmmountDolllar())
                .build();
    }


}
