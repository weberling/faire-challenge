package com.challenge.faire.statistics;

import com.challenge.faire.model.Address;
import com.challenge.faire.model.Order;
import com.challenge.faire.model.OrderItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

/**
 * Created by weberling on 24/03/19.
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticsTest {

    @Test
    public void testStatistics(){
        String sku1 = "sku1";
        String sku2 = "sku2";
        String productOption1 = "productOption1";
        String productOption2 = "productOption2";
        String productOption3 = "productOption3";
        String state1 = "Florida";
        String state2 = "California";
        Integer orderId = 1;
        Statistics statistics = new Statistics();
        statistics.addOrder(createOrder(orderId++, state1, sku1, productOption1));
        statistics.addOrder(createOrder(orderId++, state1, sku1, productOption2));
        statistics.addOrder(createOrder(orderId++, state1, sku2, productOption3));
        statistics.addOrder(createOrder(orderId++, state2, sku2, productOption1));
        statistics.addOrder(createOrder(orderId++, state1, sku2, productOption3));
        statistics.addOrder(createOrder(orderId++, state2, sku2, productOption1, 5));

        Assert.assertEquals(statistics.bestSellingProductOption.getProductOptionIdMost(), productOption1);
        Assert.assertEquals(statistics.bestSellingProductOption.getProductOptionIdMostQuantity(), new Integer(3));


        Assert.assertEquals(statistics.bestSellingProductOption.getSkuMost(), sku2);
        Assert.assertEquals(statistics.bestSellingProductOption.getSkuMostQuantity(), new Integer(4));

        Assert.assertEquals(statistics.mostAmountDollarOrder.getMostOrderAmmountDolllar(), "6");
        Assert.assertEquals(statistics.mostAmountDollarOrder.getMostAmmountDolllar(), new Integer(25));


        Assert.assertEquals(statistics.stateMostOrder.getStateMostOrder(), state1);
        Assert.assertEquals(statistics.stateMostOrder.getStateMostOrderQuantity(), new Integer(4));

        Assert.assertEquals(statistics.stateMostOrder.getStateMostDollar(), state2);
        Assert.assertEquals(statistics.stateMostOrder.getStateMostDollarAmount(), new Integer(26));
    }


    private Order createOrder(Integer id, String state, String sku, String productOption){
        return Order.builder()
                .id(id.toString())
                .address(new Address(state))
                .items(Collections.singletonList(createItem(sku, productOption, 1)))
                .build();
    }


    private Order createOrder(Integer id, String state, String sku, String productOption, Integer amount){
        return Order.builder()
                .id(id.toString())
                .address(new Address(state))
                .items(Collections.singletonList(createItem(sku, productOption, amount)))
                .build();
    }

    private OrderItem createItem(String sku, String productOption, Integer amount){
        return OrderItem.builder()
                .priceCents(amount)
                .quantity(amount)
                .sku(sku)
                .productOptionId(productOption)
                .build();
    }
}
