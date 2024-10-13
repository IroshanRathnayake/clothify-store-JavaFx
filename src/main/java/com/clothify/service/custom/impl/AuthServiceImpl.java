package com.clothify.service.custom.impl;

import com.clothify.dto.UserCredentials;
import com.clothify.entity.UserCredentialsEntity;
import com.clothify.repository.AuthDao;
import com.clothify.repository.custom.impl.AuthDaoImpl;
import com.clothify.service.custom.AuthService;
import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;

public class AuthServiceImpl implements AuthService {

    @Override
    public UserCredentials userAuthentication(String email, String password) {
        AuthDao authDao = new AuthDaoImpl();
        UserCredentialsEntity userCredentials = authDao.getUserByEmail(email);

        if (userCredentials != null && checkPassword(password, userCredentials.getPassword())) {
            return new ModelMapper().map(userCredentials, UserCredentials.class);
        }
        return null;
    }

    private boolean checkPassword(String password, String encryptedPassword) {
        return BCrypt.checkpw(password, encryptedPassword);
    }

}
