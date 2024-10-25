package com.clothify.service.custom.impl;

import com.clothify.config.AppConfig;
import com.clothify.dto.UserCredentials;
import com.clothify.entity.UserCredentialsEntity;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.UserCredentialDao;
import com.clothify.security.SecurityConfig;
import com.clothify.service.custom.UserCredentialService;
import com.clothify.util.CustomAlert;
import com.clothify.util.DaoType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;

public class UserCredentialServiceImpl implements UserCredentialService {

    private final UserCredentialDao userCredentialDao = DaoFactory.getInstance().getDaoType(DaoType.USER_CREDENTIALS);
    private final ModelMapper modelMapper = AppConfig.getModelMapper();

    @Override
    public boolean addUserCredential(UserCredentials userCredentials) {
        String hashedPassword = SecurityConfig.hashPassword(userCredentials.getPassword());
        userCredentials.setPassword(hashedPassword);

        try {
            return userCredentialDao.save(modelMapper.map(userCredentials, UserCredentialsEntity.class));
        } catch (SQLException e) {
            CustomAlert.errorAlert("Error", e);
        }
        return false;
    }

    @Override
    public boolean updateUserCredentials(UserCredentials userCredentials) {
        return userCredentialDao.update(modelMapper.map(userCredentials, UserCredentialsEntity.class));
    }

    @Override
    public boolean deleteUserCredentials(String id) {
        return userCredentialDao.delete(id);
    }

    @Override
    public ObservableList<UserCredentials> getAllUserCredentials() {
        List<UserCredentialsEntity> userCredentialEntities = userCredentialDao.findAll();

        ObservableList<UserCredentials> userCredentials = FXCollections.observableArrayList();

        for (UserCredentialsEntity userCredentialEntity : userCredentialEntities) {
            userCredentials.add(modelMapper.map(userCredentialEntity, UserCredentials.class));
        }
        return userCredentials;
    }

    @Override
    public UserCredentials searchUserCredentials(String phone) {
        return null;
    }

    @Override
    public String getLastUserCredentialId() {
        return userCredentialDao.findLastID();
    }

    @Override
    public UserCredentials getUserCredentialsByEmployeeId(String employeeId) {
        try {
            UserCredentialsEntity userCredentialEntity = userCredentialDao.findByEmployeeId(employeeId);

            if (userCredentialEntity != null) {
                return modelMapper.map(userCredentialEntity, UserCredentials.class);
            }
        } catch (SQLException e) {
            CustomAlert.errorAlert("Error Occurred", e);
        }
        return null;
    }
}
