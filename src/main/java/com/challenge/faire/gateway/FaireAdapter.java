package com.challenge.faire.gateway;

import com.challenge.faire.model.BackOrderItem;
import com.challenge.faire.model.InventoryRequest;
import com.challenge.faire.model.OrderPage;
import com.challenge.faire.model.ProductPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.*;
import feign.httpclient.ApacheHttpClient;
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
                .client(new ApacheHttpClient())
                .decoder(new JacksonDecoder(objectMapper))
                .encoder(new JacksonEncoder(objectMapper))
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .target(Faire.class, url);
    }


    interface Faire {
        @RequestLine("GET /products?page={page}&brand_id={brand_id}")
        @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
        ProductPage listProduct(@Param("page") Integer page, @Param("brand_id") String brandId, @Param("access_token") String accessToken);

        @RequestLine("GET /orders?page={page}")
        @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
        OrderPage listOrderByState(@Param("page") Integer page, @Param("access_token") String accessToken);

        @RequestLine("PATCH /products/options/inventory-levels")
        @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
        void updateInventory(@Param("access_token") String accessToken, InventoryRequest inventoryRequest);

        @RequestLine("PUT /orders/{orderId}/processing")
        @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
        void acceptOrder(@Param("orderId") String orderId, @Param("access_token") String accessToken);

        @RequestLine("POST /orders/{orderId}/items/availability")
        @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
        void backOrderItem(@Param("orderId") String orderId, @Param("access_token") String accessToken, Map<String, BackOrderItem> backOrderItemMap);
    }
}
