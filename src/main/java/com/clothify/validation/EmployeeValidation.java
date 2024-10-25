package com.clothify.validation;

import com.clothify.dto.Employee;

import java.util.List;

public class EmployeeValidation {
    public static boolean isValid(Employee employee) {
        return (employee.getId() != null
                && employee.getName() != null
                && employee.getTitle() != null
                && employee.getPosition() != null
                && employee.getAddress() != null
                && employee.getEmail() != null
                && employee.getPhoneNumber() != null
        ) && employee.getPhoneNumber().length() == 10;
    }

    public static boolean isDuplicate(List<Employee> employeeList, String employeeID) {
        for (Employee employee : employeeList) {
            if (employee.getId().equals(employeeID)) {
                return true;
            }
        }
        return false;
    }
}
