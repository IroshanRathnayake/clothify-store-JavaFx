package com.clothify.service.custom;

import com.clothify.dto.Customer;
import com.clothify.dto.Order;
import com.clothify.dto.OrderDetail;
import com.clothify.dto.Product;
import com.clothify.service.SuperService;

import java.sql.SQLException;
import java.util.List;

public interface OrderService extends SuperService {
    boolean placeOrder(Order order, List<OrderDetail> orderDetails) throws SQLException;
    Customer getCustomerByPhone(String phone);
    Product getProductById(String id);
    String getLastOrderID();
}
