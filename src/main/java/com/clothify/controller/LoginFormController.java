package com.clothify.controller;

import com.clothify.db.DBConnection;
import com.clothify.entity.UserCredentials;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class LoginFormController {

    @FXML
    private JFXButton btnSignIn;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    void btnSignInOnAction(ActionEvent event) {
        loginValidate();

    }

    @FXML
    void txtForgotPasswordOnAction(MouseEvent event) {

    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        loginValidate();
    }

    //Validate Login
    void loginValidate() {
        String SQl = "SELECT * FROM user_credentials WHERE email=?";

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            System.out.println(connection);
            PreparedStatement psTm = connection.prepareStatement(SQl);
            psTm.setString(1, txtEmail.getText());

            ResultSet rs = psTm.executeQuery();

            UserCredentials userCredentials = null;
            while (rs.next()) {
                String timeDate = rs.getString(5);
                timeDate = timeDate.replace(' ', 'T');
                userCredentials = new UserCredentials(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        LocalDateTime.parse(timeDate));
            }

            if (userCredentials != null && txtEmail.getText().equals(userCredentials.getEmail()) && txtPassword.getText().equals(userCredentials.getPassword()) && userCredentials.getRole().equals("ADMIN")) {
                System.out.println("Admin Login Successful");
                try {
                    Stage stage = new Stage();
                    stage.setScene(new Scene(
                            FXMLLoader.load(getClass().getResource("/view/admin/dashboard/admin_dashboard_base_form.fxml"))));
                    stage.setTitle("Admin Dashboard");
                    stage.setResizable(false);
                    stage.getIcons().add(new Image("img/logo-round.png"));
                    stage.show();
                    btnSignIn.getScene().getWindow().hide();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (userCredentials != null && txtEmail.getText().equals(userCredentials.getEmail()) && txtPassword.getText().equals(userCredentials.getPassword()) && userCredentials.getRole().equals("EMPLOYEE")) {
                System.out.println("User Login Successful");
                try {
                    Stage stage = new Stage();
                    stage.setScene(new Scene(
                            FXMLLoader.load(getClass().getResource("/view/user/dashboard/user_dashboard_base_form.fxml"))));
                    stage.setTitle("User Dashboard");
                    stage.setResizable(false);
                    stage.getIcons().add(new Image("img/logo-round.png"));
                    stage.show();
                    btnSignIn.getScene().getWindow().hide();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                CustomAlert.errorAlert("Login Error", new Exception("Login Failed"));
                System.out.println("Login Failed");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
