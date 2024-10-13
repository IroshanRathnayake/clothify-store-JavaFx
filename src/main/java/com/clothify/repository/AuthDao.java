package com.clothify.repository;

import com.clothify.entity.UserCredentialsEntity;

public interface AuthDao {
    UserCredentialsEntity getUserByEmail(String email);
}
