package com.clothify.repository.custom;

import com.clothify.entity.CustomerEntity;
import com.clothify.entity.OrderDetailEntity;
import com.clothify.entity.OrderEntity;
import com.clothify.repository.CrudDao;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends CrudDao<OrderEntity> {
    boolean saveOrderDetail(List<OrderDetailEntity> orderDetails);
}
