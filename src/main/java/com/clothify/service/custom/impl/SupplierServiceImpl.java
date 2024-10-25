package com.clothify.service.custom.impl;

import com.clothify.dto.Supplier;
import com.clothify.entity.SupplierEntity;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.SupplierDao;
import com.clothify.service.custom.SupplierService;
import com.clothify.util.CustomAlert;
import com.clothify.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SupplierServiceImpl implements SupplierService {

    private final SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean addSupplier(Supplier supplier) {
        try {
            return supplierDao.save(modelMapper.map(supplier, SupplierEntity.class));
        } catch (SQLException e) {
            CustomAlert.errorAlert("Error Occurred", e);
        }
        return false;
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        return supplierDao.update(modelMapper.map(supplier, SupplierEntity.class));
    }

    @Override
    public boolean deleteSupplier(String id) {
        return supplierDao.delete(id);
    }

    @Override
    public Supplier searchSupplier(String value) {
        SupplierEntity supplierEntity = supplierDao.findByName(value);

        if (supplierEntity != null) {
            return modelMapper.map(supplierEntity, Supplier.class);
        }
        return null;
    }

    @Override
    public String getLastSupplierId() {
        String lastID = supplierDao.findLastID();

        //Pattern to remove unwanted characters
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(lastID);

        return (matcher.find()) ? matcher.group() : null;
    }

    @Override
    public ObservableList<Supplier> getAllSuppliers() {
        List<SupplierEntity> supplierEntities = supplierDao.findAll();

        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();

        for (SupplierEntity supplierEntity : supplierEntities) {
            suppliers.add(modelMapper.map(supplierEntity, Supplier.class));
        }
        return suppliers;
    }

}
