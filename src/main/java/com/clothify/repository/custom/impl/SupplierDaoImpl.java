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
        String SQL = "INSERT INTO supplier VALUES (?,?,?,?,?,?)";

        try {
            return CrudUtil.execute(SQL,
                    supplierEntity.getId(),
                    supplierEntity.getCompany(),
                    supplierEntity.getOwner(),
                    supplierEntity.getAddress(),
                    supplierEntity.getPhoneNumber(),
                    supplierEntity.getEmail());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(SupplierEntity supplierEntity) {
        String SQL = "UPDATE supplier SET company=?, owner=?, address=?, phone_number=?, email=? WHERE supplier_id=? ";

        try {
            return CrudUtil.execute(SQL,
                    supplierEntity.getCompany(),
                    supplierEntity.getOwner(),
                    supplierEntity.getAddress(),
                    supplierEntity.getPhoneNumber(),
                    supplierEntity.getEmail(),
                    supplierEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        String SQL = "DELETE FROM supplier WHERE supplier_id = ?";
        try {
            return CrudUtil.execute(SQL,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        String SQL = "SELECT MAX(supplier_id) FROM supplier";

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
    public SupplierEntity findByName(String name) {
        String SQL = "SELECT * FROM supplier WHERE company=?";

        try {
            ResultSet resultSet = CrudUtil.execute(SQL, name);

            if(resultSet.next()) {
                return new SupplierEntity(
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
