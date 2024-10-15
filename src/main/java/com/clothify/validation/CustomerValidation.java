package com.clothify.validation;

import com.clothify.dto.Customer;

import java.util.List;

public class CustomerValidation {
    public static boolean isValid(Customer customer) {
        return (customer.getId() != null
                && customer.getName() != null
                && customer.getTitle() != null
                && customer.getAddress() != null
                && customer.getEmail() != null
                && customer.getPhoneNumber() != null);
    }

    public static boolean isDuplicate(List<Customer> customerList, String customerID) {
        for (Customer customer : customerList) {
            if (customer.getId().equals(customerID)) {
                return true;
            }
        }
        return false;
    }
}
