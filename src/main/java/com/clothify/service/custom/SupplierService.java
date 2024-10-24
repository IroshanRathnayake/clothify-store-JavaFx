package com.clothify.service.custom;

import com.clothify.dto.Supplier;
import com.clothify.service.SuperService;
import javafx.collections.ObservableList;

public interface SupplierService extends SuperService {
    boolean addSupplier(Supplier supplier);
    boolean updateSupplier(Supplier supplier);
    boolean deleteSupplier(String id);
    Supplier searchSupplier(String value);
    String getLastSupplierId();
    ObservableList<Supplier> getAllSuppliers();
}
