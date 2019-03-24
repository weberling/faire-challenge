package com.challenge.faire.service;

import com.challenge.faire.gateway.FaireAdapter;
import com.challenge.faire.model.Product;
import com.challenge.faire.model.ProductPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weberling on 23/03/19.
 */
@Service
public class ProductService {

    @Autowired
    private FaireAdapter faireAdapter;

    public List<Product> getAllProductsByBrand(final String brandId) {

        List<Product> products = new ArrayList<>();
        int page = 1;
        boolean paging = true;

        while (paging) {

            ProductPage productPage = faireAdapter.listProduct(page, brandId);

            if (CollectionUtils.isEmpty(productPage.getProducts())) {
                paging = false;

            } else {
                products.addAll(productPage.getProducts());
                page++;
            }
        }

        return products;

    }


}
