package com.clothify.repository.custom;

import com.clothify.entity.CustomerEntity;
import com.clothify.repository.CrudDao;

public interface CustomerDao extends CrudDao<CustomerEntity> {
    CustomerEntity findByPhone(String phone);
}
