package com.nba.controllers;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductService {

    @RequestMapping(method = RequestMethod.GET, value = "/product-review/{id}")
    public String getProductReview(@PathVariable(value = "id") int productId) {
        if(productId == 1){
            return "This product is good";
        } else if (productId == 2){
            return "This product is not good";
        } else {
            return "No review for this product";
        }
    }
}
