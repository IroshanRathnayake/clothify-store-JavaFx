package com.clothify.repository.custom;

import com.clothify.entity.SupplierEntity;
import com.clothify.repository.CrudDao;

public interface SupplierDao extends CrudDao<SupplierEntity> {
    SupplierEntity findByName(String name);
}
