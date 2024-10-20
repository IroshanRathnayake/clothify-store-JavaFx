package com.clothify.service;

import com.clothify.service.custom.impl.CustomerServiceImpl;
import com.clothify.service.custom.impl.ProductServiceImpl;
import com.clothify.service.custom.impl.SupplierServiceImpl;
import com.clothify.util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        return instance==null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType serviceType) {
        switch (serviceType) {
            case CUSTOMER:return (T) new CustomerServiceImpl();
            case PRODUCT:return (T) new ProductServiceImpl();
            case SUPPLIER:return (T) new SupplierServiceImpl();
        }
        return null;
    }
}
