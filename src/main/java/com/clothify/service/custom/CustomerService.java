package com.clothify.service.custom;

import com.clothify.dto.Customer;
import com.clothify.service.SuperService;
import javafx.collections.ObservableList;

public interface CustomerService extends SuperService {
    boolean addCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(String id);
    ObservableList<Customer> getAllCustomers();
    Customer searchCustomer(String phone);
    Customer getCustomerById(String id);
    String getLastCustomerId();
}
