package com.clothify.service.custom;

import com.clothify.dto.*;
import com.clothify.service.SuperService;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface OrderService extends SuperService {
    boolean placeOrder(Order order, List<OrderDetail> orderDetails) throws SQLException;
    ObservableList<Order> getAllOrders();
    ObservableList<OrderDetail> getOrderDetails(String orderId);
    Order getOrderById(String id);
    String getLastOrderID();
    boolean deleteOrder(String orderId);
}
