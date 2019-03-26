package com.challenge.faire.gateway;

import com.challenge.faire.model.BackOrderItem;
import com.challenge.faire.model.InventoryRequest;
import com.challenge.faire.model.OrderPage;
import com.challenge.faire.model.ProductPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by weberling on 23/03/19.
 */
@Component
public class FaireGateway {

    @Autowired
    private FaireProxy faireProxy;

    @Value("${faire_token}")
    private String token;

    public ProductPage listProduct(Integer page, String brandId) {
        return faireProxy.listProduct(page, brandId, token);
    }

    public OrderPage listOrder(Integer page) {
        return faireProxy.listOrderByState(page, token);
    }

    public void updateInventory(InventoryRequest inventoryRequest) {
        faireProxy.updateInventory(token, inventoryRequest);
    }

    public void acceptOrder(String orderId) {
        faireProxy.acceptOrder(orderId, token);
    }

    public void backOrderItem(String orderId, Map<String, BackOrderItem> backOrderItemMap) {
        faireProxy.backOrderItem(orderId, token, backOrderItemMap);
    }
}
