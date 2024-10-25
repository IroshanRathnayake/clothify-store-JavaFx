package com.clothify.repository.custom.impl;

import com.clothify.entity.CustomerEntity;
import com.clothify.repository.custom.CustomerDao;
import com.clothify.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(CustomerEntity customerEntity) {

        String SQL = "INSERT INTO customer VALUES (?,?,?,?,?,?)";

        try {
            return CrudUtil.execute(SQL,
                    customerEntity.getId(),
                    customerEntity.getTitle(),
                    customerEntity.getName(),
                    customerEntity.getAddress(),
                    customerEntity.getPhoneNumber(),
                    customerEntity.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(CustomerEntity customerEntity) {
        String SQL = "UPDATE customer SET title=?, name=?, address=?, phone_number=?, email=? WHERE customer_id=? ";

        try {
            return CrudUtil.execute(SQL,
                    customerEntity.getTitle(),
                    customerEntity.getName(),
                    customerEntity.getAddress(),
                    customerEntity.getPhoneNumber(),
                    customerEntity.getEmail(),
                    customerEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        String SQL = "DELETE FROM customer WHERE customer_id = ?";
        try {
            return CrudUtil.execute(SQL,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<CustomerEntity> findAll() {
        ObservableList<CustomerEntity> customerEntityObservableList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM customer";
            ResultSet resultSet = CrudUtil.execute(SQL);
            while (resultSet.next()) {
                CustomerEntity customerEntity = new CustomerEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                );
                customerEntityObservableList.add(customerEntity);
            }
            return customerEntityObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CustomerEntity findById(String id) {
        String SQL = "SELECT * FROM customer WHERE customer_id=?";

        try {
            ResultSet resultSet = CrudUtil.execute(SQL, id);

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

    @Override
    public CustomerEntity findByPhone(String phone) {
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

    @Override
    public String findLastID() {
        String SQL = "SELECT MAX(customer_id) FROM customer";

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
}
