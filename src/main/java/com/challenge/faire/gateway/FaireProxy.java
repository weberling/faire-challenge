package com.challenge.faire.gateway;

import com.challenge.faire.model.BackOrderItem;
import com.challenge.faire.model.InventoryRequest;
import com.challenge.faire.model.OrderPage;
import com.challenge.faire.model.ProductPage;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.Map;

/**
 * Created by weberling on 24/03/19.
 */
public interface FaireProxy {
    @RequestLine("GET /products?page={page}&brand_id={brand_id}")
    @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
    ProductPage listProduct(@Param("page") Integer page, @Param("brand_id") String brandId, @Param("access_token") String accessToken);

    @RequestLine("GET /products?page={page}&brand_id={brand_id}")
    @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
    Object test(@Param("page") Integer page, @Param("brand_id") String brandId, @Param("access_token") String accessToken);

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
