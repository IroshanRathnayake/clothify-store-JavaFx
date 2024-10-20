package com.clothify.service.custom.impl;

import com.clothify.dto.Supplier;
import com.clothify.entity.SupplierEntity;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.SupplierDao;
import com.clothify.service.custom.SupplierService;
import com.clothify.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.util.List;

public class SupplierServiceImpl implements SupplierService {

    private final SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
    private final ModelMapper modelMapper = new ModelMapper();

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
