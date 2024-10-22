package com.clothify.dto;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartTM {
    private String productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private JFXButton btnDelete;

}
