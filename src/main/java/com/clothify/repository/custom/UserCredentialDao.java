package com.clothify.repository.custom;

import com.clothify.entity.UserCredentialsEntity;
import com.clothify.repository.CrudDao;

import java.sql.SQLException;

public interface UserCredentialDao extends CrudDao<UserCredentialsEntity> {
    UserCredentialsEntity findByEmployeeId(String employeeId) throws SQLException;
    UserCredentialsEntity findByEmail(String email) throws SQLException;
}
