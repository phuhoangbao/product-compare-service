package com.nba.controllers;

import com.nba.entities.PriceCompare;
import com.nba.entities.Product;
import com.nba.service.ProductService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * Serves a list of sample products
 */
@RestController
@RequestMapping("/api/v1")
public class ProductController {

  @Autowired
  private ProductService productService;

  private List<Product> products = new LinkedList<>();
  private List<PriceCompare> priceCompares = new LinkedList<>();

  private final Bucket bucket;

  public ProductController(){
    Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
    this.bucket = Bucket4j.builder()
            .addLimit(limit)
            .build();
  }
  @RequestMapping(method = RequestMethod.GET, value = "/products")
  public List<Product> getProducts () {
    if (bucket.tryConsume(1)) {
      return products;
    } else {
        throw new ResponseStatusException(
              HttpStatus.TOO_MANY_REQUESTS,"Too Many Requests in 1 Minute. Please update your pricing plan");
    }
  }


  @RequestMapping(method = RequestMethod.GET, value = "/product/{id}")
  public Product getProductById(@PathVariable(value = "id") String productId) {
    if (bucket.tryConsume(1)) {
      return productService.getProductById(products, productId);
    } else {
      throw new ResponseStatusException(
              HttpStatus.TOO_MANY_REQUESTS,"Too Many Requests in 1 Minute. Please update your pricing plan");
    }

  }

  static {
    //this code is used to by pass error "No name matching local host found. We can also this issue by creating keystore with CN = localhost
    javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
            new javax.net.ssl.HostnameVerifier(){

              public boolean verify(String hostname,
                                    javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals("localhost")) {
                  return true;
                }
                return false;
              }
            });
  }

  @RequestMapping(method = RequestMethod.GET, value = "/product-review/{id}")
  public String getProductReview(@PathVariable(value = "id") String productId) {
    if (bucket.tryConsume(1)) {
      String productReview=  new RestTemplate().getForObject("https://localhost:8444/api/v1/product-review/" + productId, String.class);
      return productReview;
    } else {
      throw new ResponseStatusException(
              HttpStatus.TOO_MANY_REQUESTS,"Too Many Requests in 1 Minute. Please update your pricing plan");
    }

  }



  @PostConstruct
  private void loadDummyData() throws MalformedURLException {
    priceCompares.add(PriceCompare.builder().price(new BigDecimal("90")).source("Shopee").discountAmount(new BigDecimal("1.5")).productImageUrl(new URL("http://product-image/shopee")).build());
    priceCompares.add(PriceCompare.builder().price(new BigDecimal("89")).source("Lazada").discountAmount(new BigDecimal("0.0")).productImageUrl(new URL("http://product-image/lazada")).build());
    priceCompares.add(PriceCompare.builder().price(new BigDecimal("88")).source("Amazon").discountAmount(new BigDecimal("0.5")).productImageUrl(new URL("http://product-image/amazon")).build());
    products.add(Product.builder().id("1").name("Scissors").priceCompare(priceCompares).build());
    products.add(Product.builder().id("2").name("Paper").priceCompare(priceCompares).build());
    products.add(Product.builder().id("3").name("Rock").priceCompare(priceCompares).build());
  }

}
