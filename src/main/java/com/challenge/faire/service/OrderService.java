package com.challenge.faire.service;

import com.challenge.faire.gateway.FaireGateway;
import com.challenge.faire.model.BackOrderItem;
import com.challenge.faire.model.Order;
import com.challenge.faire.model.OrderPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weberling on 23/03/19.
 */
@Service
public class OrderService {

    @Autowired
    private FaireGateway faireGateway;

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        int page = 1;
        boolean paging = true;

        while (paging) {

            OrderPage orderPage = faireGateway.listOrder(page);

            if (CollectionUtils.isEmpty(orderPage.getOrders())) {
                paging = false;

            } else {
                orders.addAll(orderPage.getOrders());
                page++;
            }
        }

        return orders;
    }

    public void backOrderItem(String orderId, Map<String, BackOrderItem> backOrderItemMap) {
        faireGateway.backOrderItem(orderId, backOrderItemMap);
    }

    public void acceptOrder(String orderId) {
        faireGateway.acceptOrder(orderId);
    }
}
