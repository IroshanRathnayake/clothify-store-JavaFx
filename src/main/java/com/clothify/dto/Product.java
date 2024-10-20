package com.clothify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String id;
    private String name;
    private String category;
    private String size;
    private String supplierID;
    private Double unitPrice;
    private Integer quantity;
    private String image;
}
