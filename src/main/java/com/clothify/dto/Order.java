package com.clothify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private String id;
    private LocalDateTime date;
    private String customerId;
    private Double total;
}
