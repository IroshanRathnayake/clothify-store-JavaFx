package com.clothify.service.custom;

import com.clothify.dto.Product;
import com.clothify.service.SuperService;
import javafx.collections.ObservableList;


public interface ProductService extends SuperService {
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(String id);
    ObservableList<Product> getAllProducts();
    Product searchProduct(String productID);
    String getLastProductID();
}
