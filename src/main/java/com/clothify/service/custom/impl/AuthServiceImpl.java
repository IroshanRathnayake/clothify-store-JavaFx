package com.clothify.service.custom.impl;

import com.clothify.dto.UserCredentials;
import com.clothify.entity.UserCredentialsEntity;
import com.clothify.repository.AuthDao;
import com.clothify.repository.custom.impl.AuthDaoImpl;
import com.clothify.service.custom.AuthService;
import org.modelmapper.ModelMapper;

public class AuthServiceImpl implements AuthService {

    @Override
    public UserCredentials userAuthentication(String email, String password) {
        AuthDao authDao = new AuthDaoImpl();
        UserCredentialsEntity userCredentials = authDao.authenticateLogin(email, password);
        if (userCredentials != null) {
            return new ModelMapper().map(userCredentials, UserCredentials.class);
        }
        return null;
    }
}
