package com.clothify.service.custom.impl;

import com.clothify.dto.Customer;
import com.clothify.entity.CustomerEntity;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.CustomerDao;
import com.clothify.service.custom.CustomerService;
import com.clothify.util.CustomAlert;
import com.clothify.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao = DaoFactory.getInstance().getDaoType(DaoType.CUSTOMER);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean addCustomer(Customer customer) {
        try {
            return customerDao.save(modelMapper.map(customer, CustomerEntity.class));
        } catch (SQLException e) {
            CustomAlert.errorAlert("Error Occurred", e);
        }
        return false;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        return customerDao.update(modelMapper.map(customer, CustomerEntity.class));
    }

    @Override
    public boolean deleteCustomer(String id) {
        return customerDao.delete(id);
    }

    @Override
    public ObservableList<Customer> getAllCustomers() {

        List<CustomerEntity> customerEntityEntities = customerDao.findAll();

        ObservableList<Customer> customers = FXCollections.observableArrayList();
        for (CustomerEntity entity : customerEntityEntities) {
            Customer customer = modelMapper.map(entity, Customer.class);
            customers.add(customer);
        }
        return customers;
    }

    @Override
    public Customer searchCustomer(String phone) {
        CustomerEntity customerEntity = customerDao.findByPhone(phone);

        if (customerEntity != null) {
            return modelMapper.map(customerEntity, Customer.class);
        }
        return null;
    }

    @Override
    public Customer getCustomerById(String id) {
        CustomerEntity customerEntity = customerDao.findById(id);

        if (customerEntity != null) {
            return modelMapper.map(customerEntity, Customer.class);
        }
        return null;
    }

    @Override
    public String getLastCustomerId() {
        String lastID = customerDao.findLastID();

        //Pattern to remove unwanted characters
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(lastID);

        return (matcher.find()) ? matcher.group() : null;
    }
}
