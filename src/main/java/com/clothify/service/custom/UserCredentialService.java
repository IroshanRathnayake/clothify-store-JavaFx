package com.clothify.service.custom;

import com.clothify.dto.UserCredentials;
import com.clothify.dto.UserCredentials;
import com.clothify.service.SuperService;
import javafx.collections.ObservableList;

public interface UserCredentialService extends SuperService {
    boolean addUserCredential(UserCredentials userCredentials);
    boolean updateUserCredentials(UserCredentials customer);
    boolean deleteUserCredentials(String id);
    ObservableList<UserCredentials> getAllUserCredentials();
    UserCredentials searchUserCredentials(String phone);
    UserCredentials getUserCredentialsByEmail(String email);
    String getLastUserCredentialId();
    UserCredentials getUserCredentialsByEmployeeId(String employeeId);
}
