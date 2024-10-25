package com.clothify.service.custom.impl;

import com.clothify.dto.Product;
import com.clothify.entity.ProductEntity;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.ProductDao;
import com.clothify.service.custom.ProductService;
import com.clothify.util.CustomAlert;
import com.clothify.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao = DaoFactory.getInstance().getDaoType(DaoType.PRODUCT);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean addProduct(Product product) {
        try {
            return  productDao.save(modelMapper.map(product, ProductEntity.class));
        } catch (SQLException e) {
            CustomAlert.errorAlert("Error Occurred", e);
        }
        return false;
    }

    @Override
    public boolean updateProduct(Product product) {
        return productDao.update(modelMapper.map(product, ProductEntity.class));
    }

    @Override
    public boolean deleteProduct(String id) {
        return productDao.delete(id);
    }

    @Override
    public ObservableList<Product> getAllProducts() {
        List<ProductEntity> productEntities = productDao.findAll();

        ObservableList<Product> products = FXCollections.observableArrayList();

        for (ProductEntity productEntity : productEntities) {
            products.add(modelMapper.map(productEntity, Product.class));
        }
        return products;
    }

    @Override
    public Product searchProduct(String productID) {
        ProductEntity productEntity = productDao.findById(productID);

        if (productEntity != null) {
            return modelMapper.map(productEntity, Product.class);
        }
        return null;
    }

    @Override
    public String getLastProductID() {
        String lastID = productDao.findLastID();

        //Pattern to remove unwanted characters
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(lastID);

        return (matcher.find()) ? matcher.group() : null;
    }
}
