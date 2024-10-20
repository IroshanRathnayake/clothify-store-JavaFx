package com.clothify.repository.custom.impl;

import com.clothify.entity.ProductEntity;
import com.clothify.repository.custom.ProductDao;
import com.clothify.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDaoImpl implements ProductDao {
    @Override
    public boolean save(ProductEntity productEntity) {
        String SQL = "INSERT INTO product VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            return CrudUtil.execute(SQL,
                    productEntity.getId(),
                    productEntity.getName(),
                    productEntity.getCategory(),
                    productEntity.getSize(),
                    productEntity.getSupplierID(),
                    productEntity.getUnitPrice(),
                    productEntity.getQuantity(),
                    productEntity.getImage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(ProductEntity productEntity) {
        String SQL = "UPDATE product SET name=?, category=?, size=?, supplier_id=?, unit_price=?, qty_on_hand=?, image=? WHERE product_id=?";

        try {
            return CrudUtil.execute(SQL,
                    productEntity.getName(),
                    productEntity.getCategory(),
                    productEntity.getSize(),
                    productEntity.getSupplierID(),
                    productEntity.getUnitPrice(),
                    productEntity.getQuantity(),
                    productEntity.getImage(),
                    productEntity.getId()
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        String SQL = "DELETE FROM product WHERE product_id = ?";
        try {
            return CrudUtil.execute(SQL,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<ProductEntity> findAll() {
        ObservableList<ProductEntity> productList = FXCollections.observableArrayList();

        try {
            String SQL = "SELECT * FROM product";
            ResultSet resultSet = CrudUtil.execute(SQL);

            while (resultSet.next()) {
                productList.add(new ProductEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        Double.parseDouble(resultSet.getString(6)),
                        Integer.parseInt(resultSet.getString(7)),
                        resultSet.getString(8)
                ));
            }
            return productList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductEntity findById(String id) {
        String SQL = "SELECT * FROM product WHERE product_id=? || name=?";

        try {
            ResultSet resultSet = CrudUtil.execute(SQL, id,id);

            if(resultSet.next()) {
                return new ProductEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        Double.parseDouble(resultSet.getString(6)),
                        Integer.parseInt(resultSet.getString(7)),
                        resultSet.getString(8)
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public String findLastID() {
        String SQL = "SELECT MAX(product_id) FROM product";

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
