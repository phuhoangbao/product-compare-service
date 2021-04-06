package com.nba.service;

import com.nba.entities.Product;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Cacheable("product")
    public Product getProductById(List<Product> products, String productId)
    {
        try
        {
            for (Product product : products) {
                if (product.getId().equals(productId)) {
                    System.out.println("Simulate a longgggggg backend call.");
                    Thread.sleep(1000*3);
                    return product;
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
