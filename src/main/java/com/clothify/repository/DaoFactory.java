package com.clothify.repository;

import com.clothify.repository.custom.impl.CustomerDaoImpl;
import com.clothify.repository.custom.impl.OrderDaoImpl;
import com.clothify.repository.custom.impl.ProductDaoImpl;
import com.clothify.repository.custom.impl.SupplierDaoImpl;
import com.clothify.util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;

    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return instance==null?instance=new DaoFactory():instance;
    }

    public <T extends SuperDao> T getDaoType(DaoType daoType){
        switch (daoType){
            case CUSTOMER:return (T) new CustomerDaoImpl();
            case PRODUCT:return (T) new ProductDaoImpl();
            case SUPPLIER:return (T) new SupplierDaoImpl();
            case ORDER: return (T) new OrderDaoImpl();
        }
        return null;
    }
}
