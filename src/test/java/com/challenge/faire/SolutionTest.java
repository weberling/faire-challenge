package com.challenge.faire;

import com.challenge.faire.model.BackOrderItem;
import com.challenge.faire.model.InventoryRequest;
import com.challenge.faire.service.InventoryService;
import com.challenge.faire.service.OrderService;
import com.challenge.faire.service.ProductService;
import com.challenge.faire.service.SolutionService;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


/**
 * Created by weberling on 24/03/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SolutionTest {

    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final StringValuePattern CONTENT_TYPE_PATTERN = equalTo("application/json");
    public static final String CONTENT_TYPE = "Content-type";
    public static final String X_FAIRE_ACCESS_TOKEN = "X-FAIRE-ACCESS-TOKEN";
    public static final StringValuePattern FAIRE_TOKEN = equalTo("ANY");
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9090);
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private SolutionService solution;
    @SpyBean
    private ProductService productService;
    @SpyBean
    private OrderService orderService;
    @SpyBean
    private InventoryService inventoryService;

    @Test
    public void testConsumeAndStatistics() throws Exception {
        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, "application/json")
                        .withBodyFile("mock/productsPage1.json")));

        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("2"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader("X-FAIRE-ACCESS-TOKEN", FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/productsPage2.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("1"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/orders/accepted/ordersPage1.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("2"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/orders/accepted/ordersPage2.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("3"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/orders/ordersPageEmpty.json")));

        stubFor(patch(urlPathEqualTo("/products/options/inventory-levels"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)));

        mockMvc.perform(
                post("/orders/consume-statistics"))
                .andExpect(MockMvcResultMatchers.status().isOk());

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
    public void testConsumerWithOrderNew() throws Exception {
        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/productsPage1.json")));

        stubFor(get(urlPathEqualTo("/products"))
                .withQueryParam("page", equalTo("2"))
                .withQueryParam("brand_id", equalTo("b_d2481b88"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/productsPage2.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("1"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/orders/new/ordersPage1.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("2"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/orders/ordersPageEmpty.json")));

        stubFor(put(urlPathEqualTo("/orders/bo_3s7ei5an/processing"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)));

        stubFor(WireMock.post(urlPathEqualTo("/orders/bo_3iuhowot/items/availability"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)));

        stubFor(patch(urlPathEqualTo("/products/options/inventory-levels"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)));

        mockMvc.perform(
                post("/orders/consume"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(productService, Mockito.times(1))
                .getAllProductsByBrand(Mockito.eq("b_d2481b88"));
        Mockito.verify(orderService, Mockito.times(1))
                .acceptOrder(Mockito.eq("bo_3s7ei5an"));
        Map<String, BackOrderItem> backOrderItemMap = new HashMap() {{
            put("oi_ce3i14rx", new BackOrderItem(2));
        }};
        Mockito.verify(orderService, Mockito.times(1))
                .backOrderItem(Mockito.eq("bo_3iuhowot"), Mockito.eq(backOrderItemMap));
        Mockito.verify(orderService, Mockito.times(1))
                .getAllOrders();
        Mockito.verify(inventoryService, Mockito.times(1))
                .update(Mockito.any(InventoryRequest.class));
    }

    @Test
    public void tesStatistics() throws Exception {

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("1"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/orders/new/ordersPage1.json")));

        stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("page", equalTo("2"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)
                        .withBodyFile("mock/orders/ordersPageEmpty.json")));

        stubFor(put(urlPathEqualTo("/orders/bo_3s7ei5an/processing"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)));

        stubFor(WireMock.post(urlPathEqualTo("/orders/bo_3iuhowot/items/availability"))
                .withHeader(CONTENT_TYPE, CONTENT_TYPE_PATTERN)
                .withHeader(X_FAIRE_ACCESS_TOKEN, FAIRE_TOKEN)
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader(CONTENT_TYPE, CONTENT_TYPE_VALUE)));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/orders/statistics"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("best_selling_product_option.key", Matchers.is("po_59w6yhym")))
                .andExpect(jsonPath("best_selling_product_option.value", Matchers.is(1)))
                .andExpect(jsonPath("largest_order_by_dollar_ammount.key", Matchers.is("bo_3s7ei5an")))
                .andExpect(jsonPath("largest_order_by_dollar_ammount.value", Matchers.is(3825)))
                .andExpect(jsonPath("state_most_order.key", Matchers.is("Florida")))
                .andExpect(jsonPath("state_most_order.value", Matchers.is(1)))
                .andExpect(jsonPath("best_selling_sku.key", Matchers.is("IND0001")))
                .andExpect(jsonPath("best_selling_sku.value", Matchers.is(1)))
                .andExpect(jsonPath("state_most_amount_order.key", Matchers.is("Florida")))
                .andExpect(jsonPath("state_most_amount_order.value", Matchers.is(3825)));




        Mockito.verify(orderService, Mockito.times(1))
                .getAllOrders();
    }
}
