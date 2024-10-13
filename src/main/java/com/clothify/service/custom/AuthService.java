package com.clothify.service.custom;

import com.clothify.dto.UserCredentials;
import com.clothify.service.SuperService;

public interface AuthService extends SuperService {
    UserCredentials userAuthentication(String email, String password);
}
