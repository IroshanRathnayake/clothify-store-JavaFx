package com.clothify.repository.custom.impl;

import com.clothify.entity.UserCredentialsEntity;
import com.clothify.repository.custom.UserCredentialDao;
import com.clothify.util.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class UserCredentialDaoImpl implements UserCredentialDao {
    @Override
    public boolean save(UserCredentialsEntity userCredentialsEntity) throws SQLException {
        String SQL = "INSERT INTO user_credentials VALUES (?,?,?,?,?,?)";

        return CrudUtil.execute(
                SQL,
                userCredentialsEntity.getId(),
                userCredentialsEntity.getEmployeeID(),
                userCredentialsEntity.getEmail(),
                userCredentialsEntity.getPassword(),
                userCredentialsEntity.getRole(),
                userCredentialsEntity.getCreatedAt()
        );
    }

    @Override
    public boolean update(UserCredentialsEntity userCredentialsEntity) {
        String SQL = "UPDATE user_credentials SET employee_id=?, email=?, password=?, role=?, created_at=? WHERE credential_id=? ";

        try {
            return CrudUtil.execute(SQL,
                    userCredentialsEntity.getEmployeeID(),
                    userCredentialsEntity.getEmail(),
                    userCredentialsEntity.getPassword(),
                    userCredentialsEntity.getRole(),
                    userCredentialsEntity.getCreatedAt(),
                    userCredentialsEntity.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        String SQL = "DELETE FROM user_credentials WHERE credential_id = ?";
        try {
            return CrudUtil.execute(SQL,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<UserCredentialsEntity> findAll() {
        ObservableList<UserCredentialsEntity> userCredentialEntityObservableList = FXCollections.observableArrayList();
        try {
            String SQL = "SELECT * FROM user_credentials";
            ResultSet resultSet = CrudUtil.execute(SQL);
            while (resultSet.next()) {
                String timeDate = resultSet.getString(6);
                timeDate = timeDate.replace(' ', 'T');
                UserCredentialsEntity customerEntity = new UserCredentialsEntity(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        LocalDateTime.parse(timeDate)
                );
                userCredentialEntityObservableList.add(customerEntity);
            }
            return userCredentialEntityObservableList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserCredentialsEntity findById(String id) {
        return null;
    }

    @Override
    public String findLastID() {
        String SQL = "SELECT MAX(credential_id) FROM user_credentials";

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

    @Override
    public UserCredentialsEntity findByEmployeeId(String employeeId) throws SQLException {
        String SQL = "SELECT * FROM user_credentials WHERE employee_id = ?";

        ResultSet resultSet = CrudUtil.execute(SQL, employeeId);

        if(resultSet.next()) {
            String timeDate = resultSet.getString(6);
            timeDate = timeDate.replace(' ', 'T');
            return new UserCredentialsEntity(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    LocalDateTime.parse(timeDate)
            );
        }
        return null;
    }
}
