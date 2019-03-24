package com.challenge.faire;

import com.challenge.faire.model.InventoryRequest;
import com.challenge.faire.service.InventoryService;
import com.challenge.faire.service.OrderService;
import com.challenge.faire.service.ProductService;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


/**
 * Created by weberling on 24/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackageClasses = FaireChallengeApplication.class)
public class SolutionTest {

    @Autowired
    private Solution solution;

    @SpyBean
    private ProductService productService;

    @SpyBean
    private OrderService orderService;

    @SpyBean
    private InventoryService inventoryService;

    @Before
    public void setup(){
        WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(9090));
        WireMock.configureFor(9090);
        wireMockServer.start();
        System.out.print(1);
    }

    @Test
    public void defaultTest() {
        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/productsPage1.json")));

        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("2"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/productsPage2.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("1"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/ordersPage1.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("2"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/ordersPage2.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("3"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/ordersPage3.json")));

        stubFor(patch(urlPathEqualTo("/products/options/inventory-levels"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")));

        solution.execute();

        /**
         @RequestLine("PUT /orders/{orderId}/processing")
         @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
         void acceptOrder(@Param("orderId") String orderId, @Param("access_token") String accessToken);

         @RequestLine("POST /orders/{orderId}/items/availability")
         @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
         void backOrderItem(@Param("orderId") String orderId, @Param("access_token") String accessToken, Map<String, BackOrderItem> backOrderItemMap);
         */

        Mockito.verify(productService, Mockito.times(1))
                .getAllProductsByBrand(Mockito.eq("b_d2481b88"));
        Mockito.verify(orderService, Mockito.times(0))
                .acceptOrder(Mockito.anyString());
        Mockito.verify(orderService, Mockito.times(0))
                .backOrderItem(Mockito.anyString(), Mockito.anyMap());
        Mockito.verify(orderService, Mockito.times(1))
                .getAllOrders();
        Mockito.verify(inventoryService, Mockito.times(1))
                .update(Mockito.any(InventoryRequest.class));
    }

    @Test
    public void testWithOrderNew() {
        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/productsPage1.json")));

        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("2"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/productsPage2.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("1"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/ordersPage1.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("2"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/ordersPage2.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("3"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("mock/ordersPage3.json")));

        stubFor(patch(urlPathEqualTo("/products/options/inventory-levels"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withHeader("X-FAIRE-ACCESS-TOKEN", equalTo("ANY"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")));

        solution.execute();

        /**
         @RequestLine("PUT /orders/{orderId}/processing")
         @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
         void acceptOrder(@Param("orderId") String orderId, @Param("access_token") String accessToken);

         @RequestLine("POST /orders/{orderId}/items/availability")
         @Headers({"X-FAIRE-ACCESS-TOKEN: {access_token}", "Content-Type: application/json"})
         void backOrderItem(@Param("orderId") String orderId, @Param("access_token") String accessToken, Map<String, BackOrderItem> backOrderItemMap);
         */

        Mockito.verify(productService, Mockito.times(1))
                .getAllProductsByBrand(Mockito.eq("b_d2481b88"));
        Mockito.verify(orderService, Mockito.times(0))
                .acceptOrder(Mockito.anyString());
        Mockito.verify(orderService, Mockito.times(0))
                .backOrderItem(Mockito.anyString(), Mockito.anyMap());
        Mockito.verify(orderService, Mockito.times(1))
                .getAllOrders();
        Mockito.verify(inventoryService, Mockito.times(1))
                .update(Mockito.any(InventoryRequest.class));
    }
}
