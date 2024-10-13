package com.clothify.repository.custom.impl;

import com.clothify.db.DBConnection;
import com.clothify.entity.UserCredentialsEntity;
import com.clothify.repository.AuthDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AuthDaoImpl implements AuthDao {
    @Override
    public UserCredentialsEntity getUserByEmail(String email) {
        String SQl = "SELECT * FROM user_credentials WHERE email=?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement psTm = connection.prepareStatement(SQl);
            psTm.setString(1, email );

            ResultSet rs = psTm.executeQuery();

            UserCredentialsEntity userCredentials = null;
            while (rs.next()) {
                String timeDate = rs.getString(5);
                timeDate = timeDate.replace(' ', 'T');
                userCredentials = new UserCredentialsEntity(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        LocalDateTime.parse(timeDate));
            }
            return userCredentials;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
