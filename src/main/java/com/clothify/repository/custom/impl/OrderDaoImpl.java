package com.clothify.repository.custom.impl;

import com.clothify.entity.OrderDetailEntity;
import com.clothify.entity.OrderEntity;
import com.clothify.repository.custom.OrderDao;

import com.clothify.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;


public class OrderDaoImpl implements OrderDao {

    @Override
    public boolean save(OrderEntity orderEntity) {
        String SQL = "INSERT INTO orders VALUES (?,?,?,?)";

        try {
            return CrudUtil.execute(SQL,
                    orderEntity.getId(),
                    orderEntity.getDate(),
                    orderEntity.getCustomerId(),
                    orderEntity.getTotal()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        String SQL = "DELETE FROM orders WHERE order_id = ?";

        try {
            return CrudUtil.execute(SQL,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<OrderEntity> findAll() {
        ObservableList<OrderEntity> orderDetailList = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT * FROM orders";
            ResultSet resultSet = CrudUtil.execute(SQL);

            while (resultSet.next()) {
                String timeDate = resultSet.getString(2);
                timeDate = timeDate.replace(' ', 'T');
                orderDetailList.add(new OrderEntity(
                        resultSet.getString(1),
                        LocalDateTime.parse(timeDate),
                        resultSet.getString(3),
                        resultSet.getDouble(4)
                ));
            }
            return orderDetailList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrderEntity findById(String id) {
        return null;
    }

    @Override
    public String findLastID() {
        String SQL = "SELECT MAX(order_id) FROM orders";

        try {
            ResultSet result = CrudUtil.execute(SQL);
            if(result.next()) {
                return result.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public boolean saveOrderDetail(List<OrderDetailEntity> orderDetails) {
        String SQL = "INSERT INTO order_detail VALUES (?,?,?,?)";

        for (OrderDetailEntity orderDetailEntity : orderDetails) {
            try {
                CrudUtil.execute(SQL,
                        orderDetailEntity.getId(),
                        orderDetailEntity.getProductId(),
                        orderDetailEntity.getQuantity(),
                        orderDetailEntity.getDiscount()
                );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    @Override
    public ObservableList<OrderDetailEntity> getOrderDetails(String orderId) {
        ObservableList<OrderDetailEntity> orderDetailList = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT * FROM order_detail WHERE order_id = ?";
            ResultSet resultSet = CrudUtil.execute(SQL,orderId);

            while (resultSet.next()) {
                orderDetailList.add(new OrderDetailEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getInt(3),
                        resultSet.getDouble(4)
                ));
            }
            return orderDetailList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
