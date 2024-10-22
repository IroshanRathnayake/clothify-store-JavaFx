package com.clothify.repository.custom;

import com.clothify.dto.Customer;
import com.clothify.entity.CustomerEntity;
import com.clothify.entity.OrderDetailEntity;
import com.clothify.repository.CrudDao;

public interface OrderDao extends CrudDao<OrderDetailEntity> {
    CustomerEntity getCustomerByPhone(String phone);
}
