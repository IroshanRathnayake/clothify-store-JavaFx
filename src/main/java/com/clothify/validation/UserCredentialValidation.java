package com.clothify.validation;

import com.clothify.dto.UserCredentials;

import java.util.List;

public class UserCredentialValidation {
    public static boolean isValid(UserCredentials userCredential) {
        return (userCredential.getId() != null
                && userCredential.getEmployeeID() != null
                && userCredential.getEmail() != null
                && userCredential.getPassword() != null
                && userCredential.getRole() != null
                && userCredential.getCreatedAt() != null);
    }

    public static boolean isDuplicate(List<UserCredentials> userCredentialsList, String credentialID) {
        for (UserCredentials userCredential : userCredentialsList) {
            if (userCredential.getId().equals(credentialID)) {
                return true;
            }
        }
        return false;
    }
}
