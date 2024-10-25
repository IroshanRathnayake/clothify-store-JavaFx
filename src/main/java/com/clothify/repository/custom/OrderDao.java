package com.clothify.repository.custom;

import com.clothify.entity.OrderDetailEntity;
import com.clothify.entity.OrderEntity;
import com.clothify.repository.CrudDao;
import javafx.collections.ObservableList;

import java.util.List;

public interface OrderDao extends CrudDao<OrderEntity> {
    boolean saveOrderDetail(List<OrderDetailEntity> orderDetails);
    ObservableList<OrderDetailEntity> getOrderDetails(String orderId);
}
