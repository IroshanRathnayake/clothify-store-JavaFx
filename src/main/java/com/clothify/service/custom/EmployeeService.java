package com.clothify.service.custom;

import com.clothify.dto.Employee;
import com.clothify.service.SuperService;
import javafx.collections.ObservableList;

public interface EmployeeService extends SuperService {
    boolean addEmployee(Employee employee);
    boolean updateEmployee(Employee employee);
    boolean deleteEmployee(String id);
    ObservableList<Employee> getAllEmployees();
    Employee searchEmployee(String phone);
    String getLastEmployeeId();
}
