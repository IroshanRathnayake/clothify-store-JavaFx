package com.clothify.repository.custom.impl;

import com.clothify.entity.EmployeeEntity;
import com.clothify.repository.custom.EmployeeDao;
import com.clothify.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public boolean save(EmployeeEntity employeeEntity) throws SQLException {
        String SQL = "INSERT INTO employee VALUES (?,?,?,?,?,?,?,?)";

        return CrudUtil.execute(SQL,
                    employeeEntity.getId(),
                    employeeEntity.getTitle(),
                    employeeEntity.getName(),
                    employeeEntity.getPosition(),
                    employeeEntity.getAddress(),
                    employeeEntity.getPhoneNumber(),
                    employeeEntity.getEmail(),
                    employeeEntity.getLoginAccess()
        );
    }

    @Override
    public boolean update(EmployeeEntity employeeEntity) {
        String SQL = "UPDATE employee SET title=?, name=?, position=?, address=?, phone_number=?, email=?, login_access=? WHERE employee_id=? ";

        try {
            return CrudUtil.execute(SQL,
                    employeeEntity.getTitle(),
                    employeeEntity.getName(),
                    employeeEntity.getPosition(),
                    employeeEntity.getAddress(),
                    employeeEntity.getPhoneNumber(),
                    employeeEntity.getEmail(),
                    employeeEntity.getLoginAccess(),
                    employeeEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        String SQL = "DELETE FROM employee WHERE employee_id = ?";
        try {
            return CrudUtil.execute(SQL,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<EmployeeEntity> findAll() {
        ObservableList<EmployeeEntity> employeeEntityObservableList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM employee";
            ResultSet resultSet = CrudUtil.execute(SQL);
            while (resultSet.next()) {
                EmployeeEntity employeeEntity = new EmployeeEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getBoolean(8)
                );
                employeeEntityObservableList.add(employeeEntity);
            }
            return employeeEntityObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EmployeeEntity findById(String id) {
        return null;
    }

    @Override
    public String findLastID() {
        String SQL = "SELECT MAX(employee_id) FROM employee";

        try {
            ResultSet result = CrudUtil.execute(SQL);
            if(result.next()) {
                return result.getString(1);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
