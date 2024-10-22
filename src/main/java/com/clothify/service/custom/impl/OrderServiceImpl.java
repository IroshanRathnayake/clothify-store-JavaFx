package com.clothify.service.custom.impl;

import com.clothify.dto.Customer;
import com.clothify.dto.Product;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.OrderDao;
import com.clothify.service.custom.OrderService;
import com.clothify.util.DaoType;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);

    @Override
    public Customer getCustomerByPhone(String phone) {
        return null;
    }

    @Override
    public Product getProductById(String id) {
        return null;
    }
}
