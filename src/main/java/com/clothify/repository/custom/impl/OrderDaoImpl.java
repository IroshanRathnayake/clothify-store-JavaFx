package com.clothify.repository.custom.impl;

import com.clothify.dto.Customer;
import com.clothify.entity.CustomerEntity;
import com.clothify.entity.OrderDetailEntity;
import com.clothify.repository.custom.OrderDao;
import com.clothify.util.CrudUtil;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean save(OrderDetailEntity orderDetailEntity) {
        return false;
    }

    @Override
    public boolean update(OrderDetailEntity orderDetailEntity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public ObservableList<OrderDetailEntity> findAll() {
        return null;
    }

    @Override
    public OrderDetailEntity findById(String id) {
        return null;
    }

    @Override
    public String findLastID() {
        return "";
    }

    @Override
    public CustomerEntity getCustomerByPhone(String phone) {
        String SQL = "SELECT * FROM customer WHERE phone_number=?";

        try {
            ResultSet resultSet = CrudUtil.execute(SQL, phone);

            if(resultSet.next()) {
                return new CustomerEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
