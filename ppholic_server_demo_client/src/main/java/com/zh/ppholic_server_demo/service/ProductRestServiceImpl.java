package com.zh.ppholic_server_demo.service;

import java.util.List;

import com.zh.ppholic_server_demo.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductRestServiceImpl implements ProductService {

    @Autowired
    private RestTemplate theRestTemplate;

    @Value("${psd.api.product.url}")
    private String PRODUCT_API_URL;

    @Override
    public Product getProduct(int theProductId) {
        
        return  theRestTemplate.getForObject(PRODUCT_API_URL + theProductId, Product.class);
    }

    @Override
    public List<Product> getSortedProducts(int theSortField, String theSearchName) {
        
        String sortedSearch = theSortField + "_" + theSearchName;
        
        return theRestTemplate.exchange(PRODUCT_API_URL + "sorted_search/" + sortedSearch,
                                        HttpMethod.GET, null,
                                        new ParameterizedTypeReference<List<Product>>() {})
                                .getBody();
    }
}
