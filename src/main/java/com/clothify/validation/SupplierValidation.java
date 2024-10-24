package com.clothify.validation;

import com.clothify.dto.Supplier;

import java.util.List;

public class SupplierValidation {
    public static boolean isValid(Supplier supplier) {
        return (supplier.getId() != null
                && supplier.getCompany() != null
                && supplier.getOwner() != null
                && supplier.getAddress() != null
                && supplier.getEmail() != null
                && supplier.getPhoneNumber() != null);
    }

    public static boolean isDuplicate(List<Supplier> supplierList, String supplierID) {
        for (Supplier supplier : supplierList) {
            if (supplier.getId().equals(supplierID)) {
                return true;
            }
        }
        return false;
    }
}
