package com.clothify.repository.custom.impl;

import com.clothify.entity.SupplierEntity;
import com.clothify.repository.custom.SupplierDao;
import com.clothify.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplierDaoImpl implements SupplierDao {
    @Override
    public boolean save(SupplierEntity supplierEntity) {
        return false;
    }

    @Override
    public boolean update(SupplierEntity supplierEntity) {
        return false;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public ObservableList<SupplierEntity> findAll() {
        String SQL = "SELECT * FROM supplier";

        ObservableList<SupplierEntity> supplierList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = CrudUtil.execute(SQL);
            while (resultSet.next()) {
                supplierList.add(
                        new SupplierEntity(
                                resultSet.getString(1),
                                resultSet.getString(2),
                                resultSet.getString(3),
                                resultSet.getString(4),
                                resultSet.getString(5),
                                resultSet.getString(6)
                        )
                );
            }
            return supplierList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SupplierEntity findById(String id) {
        return null;
    }


    @Override
    public String findLastID() {
        return "";
    }

}
