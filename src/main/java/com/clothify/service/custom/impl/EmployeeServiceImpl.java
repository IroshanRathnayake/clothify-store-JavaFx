package com.clothify.service.custom.impl;

import com.clothify.config.AppConfig;
import com.clothify.dto.Employee;
import com.clothify.entity.EmployeeEntity;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.EmployeeDao;
import com.clothify.service.custom.EmployeeService;
import com.clothify.util.CustomAlert;
import com.clothify.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
    private final ModelMapper modelMapper = AppConfig.getModelMapper();

    @Override
    public boolean addEmployee(Employee employee) {
        try {
            return employeeDao.save(modelMapper.map(employee, EmployeeEntity.class));
        } catch (SQLException e) {
            CustomAlert.errorAlert("Error Occurred", e);
        }
        return false;
    }


    @Override
    public boolean updateEmployee(Employee employee) {
        return employeeDao.update(modelMapper.map(employee, EmployeeEntity.class));
    }

    @Override
    public boolean deleteEmployee(String id) {
        return employeeDao.delete(id);
    }

    @Override
    public ObservableList<Employee> getAllEmployees() {
        List<EmployeeEntity> employeeEntityDetails = employeeDao.findAll();

        ObservableList<Employee> employees = FXCollections.observableArrayList();
        for (EmployeeEntity entity : employeeEntityDetails) {
            Employee customer = modelMapper.map(entity, Employee.class);
            employees.add(customer);
        }
        return employees;
    }

    @Override
    public Employee searchEmployee(String phone) {
        return null;
    }

    @Override
    public String getLastEmployeeId() {
        String lastID = employeeDao.findLastID();

        //Pattern to remove unwanted characters
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(lastID);

        return (matcher.find()) ? matcher.group() : null;
    }
}
