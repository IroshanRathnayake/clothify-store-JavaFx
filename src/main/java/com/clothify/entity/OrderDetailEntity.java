package com.clothify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity (name = "order_detail")
public class OrderDetailEntity {
    @Id
    private String id;
    private String productId;
    private Integer quantity;
    private Double discount;
}
