package com.nba.entities;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
/**
 * Sample product structure. Note that getters and setter are auto-generated by Lombok
 */
@Data
@Builder
public class Product {

  private String id;
  private String name;
  private List<PriceCompare> priceCompare;

}
