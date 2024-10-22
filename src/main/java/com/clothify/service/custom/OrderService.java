package com.clothify.service.custom;

import com.clothify.dto.Customer;
import com.clothify.dto.Product;
import com.clothify.service.SuperService;

public interface OrderService extends SuperService {
    Customer getCustomerByPhone(String phone);
    Product getProductById(String id);
}
