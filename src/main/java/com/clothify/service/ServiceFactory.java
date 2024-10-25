package com.clothify.service;

import com.clothify.service.custom.impl.*;
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
            case ORDER:return (T) new OrderServiceImpl();
            case EMPLOYEE:return (T) new EmployeeServiceImpl();
            case USER_CREDENTIALS:return (T) new UserCredentialServiceImpl();
        }
        return null;
    }
}
