package com.challenge.faire;

import com.challenge.faire.service.InventoryService;
import com.challenge.faire.service.OrderService;
import com.challenge.faire.service.ProductService;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


/**
 * Created by weberling on 24/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SolutionTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig().needClientAuth(false).httpsPort(9090));

    @Autowired
    private Solution solution;

    @SpyBean
    private ProductService productService;

    @SpyBean
    private OrderService orderService;

    @SpyBean
    private InventoryService inventoryService;

    @Test
    public void exampleTest() {
        stubFor(get(urlEqualTo("/products?page=1&brand_id=b_d2481b88"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("mock/productsPage1")));
        stubFor(get(urlEqualTo("/products?page=2&brand_id=b_d2481b88"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("mock/productsPage2")));

        stubFor(get(urlEqualTo("/orders?page=1"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("mock/ordersPage1")));

        stubFor(get(urlEqualTo("/orders?page=2"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("mock/ordersPage2")));

        stubFor(get(urlEqualTo("/orders?page=3"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBodyFile("mock/ordersPage3")));

        stubFor(patch(urlEqualTo("/products/options/inventory-levels"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)));

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
    }
}
