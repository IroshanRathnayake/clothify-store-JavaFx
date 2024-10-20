package com.clothify.service.custom;

import com.clothify.dto.Supplier;
import com.clothify.service.SuperService;
import javafx.collections.ObservableList;

public interface SupplierService extends SuperService {
    ObservableList<Supplier> getAllSuppliers();
}
