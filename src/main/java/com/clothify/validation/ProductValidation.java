package com.clothify.validation;

import com.clothify.dto.Product;

import java.util.List;

public class ProductValidation {
    public static boolean isValid(Product product) {
        return (product.getId() != null
                && product.getName() != null
                && product.getCategory() != null
                && product.getSize() != null
                && product.getSupplierID() != null
                && product.getUnitPrice() != null
                && product.getQuantity() != null
                && product.getImage() != null
        );
    }

    public static boolean isDuplicate(List<Product> productList, String productID) {
        for (Product product : productList) {
            if (product.getId().equals(productID)) {
                return true;
            }
        }
        return false;
    }
}
