package com.challenge.faire.gateway;

import com.challenge.faire.model.BackOrderItem;
import com.challenge.faire.model.InventoryRequest;
import com.challenge.faire.model.OrderPage;
import com.challenge.faire.model.ProductPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by weberling on 23/03/19.
 */
@Component
public class FaireAdapter {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${url}")
    private String baseUrl;

    @Value("${faire_token}")
    private String token;

    public ProductPage listProduct(Integer page, String brandId) {
        return connect(baseUrl).listProduct(page, brandId, token);
    }

    public OrderPage listOrder(Integer page) {
        return connect(baseUrl).listOrderByState(page, token);
    }

    public void updateInventory(InventoryRequest inventoryRequest) {
        connect(baseUrl).updateInventory(token, inventoryRequest);
    }

    public void acceptOrder(String orderId) {
        connect(baseUrl).acceptOrder(orderId, token);
    }

    public void backOrderItem(String orderId, Map<String, BackOrderItem> backOrderItemMap) {
        connect(baseUrl).backOrderItem(orderId, token, backOrderItemMap);
    }

    private Faire connect(final String url) {
        return Feign.builder()
                .decoder(new JacksonDecoder(objectMapper))
                .encoder(new JacksonEncoder(objectMapper))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .target(Faire.class, url);
    }


    interface Faire {
        @RequestLine("GET /api/v1/products?page={page}&brand_id={brand_id}")
        @Headers("X-FAIRE-ACCESS-TOKEN: {access_token}")
        ProductPage listProduct(@Param("page") Integer page, @Param("brand_id") String brandId, @Param("access_token") String accessToken);

        @RequestLine("GET /api/v1/orders?page={page}")
        @Headers("X-FAIRE-ACCESS-TOKEN: {access_token}")
        OrderPage listOrderByState(@Param("page") Integer page, @Param("access_token") String accessToken);

        @RequestLine("PATCH /api/v1/products/options/inventory-levels")
        @Headers("X-FAIRE-ACCESS-TOKEN: {access_token}")
        void updateInventory(@Param("access_token") String accessToken, InventoryRequest inventoryRequest);

        @RequestLine("PUT /api/v1/orders/{orderId}/processing")
        @Headers("X-FAIRE-ACCESS-TOKEN: {access_token}")
        void acceptOrder(@Param("orderId") String orderId, @Param("access_token") String accessToken);

        @RequestLine("POST /api/v1/orders/{orderId}/items/availability")
        @Headers("X-FAIRE-ACCESS-TOKEN: {access_token}")
        void backOrderItem(@Param("orderId") String orderId, @Param("access_token") String accessToken, Map<String, BackOrderItem> backOrderItemMap);
    }
}
