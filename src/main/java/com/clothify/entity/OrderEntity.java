package com.clothify.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "orders")
public class OrderEntity {
    @Id
    private String id;
    private LocalDateTime date;
    private String customerId;
    private Double total;
}
