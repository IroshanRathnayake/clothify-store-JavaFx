package com.clothify.controller;

import com.clothify.dto.UserCredentials;
import com.clothify.service.custom.AuthService;
import com.clothify.service.custom.impl.AuthServiceImpl;
import com.clothify.util.CustomAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    @FXML
    private JFXButton btnSignIn;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private Label heading;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    void btnSignInOnAction(ActionEvent event) {
        loginValidate();
    }

    @FXML
    void txtForgotPasswordOnAction(MouseEvent event) {
        try {
            Stage stage = new Stage();
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/view/auth/forgot_password.fxml"))));
            stage.setTitle("Forgot Password");
            stage.setResizable(false);
            stage.getIcons().add(new Image("img/logo-round.png"));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void txtPasswordOnAction(ActionEvent event) {
        loginValidate();
    }

    //Validate Login
    void loginValidate() {
        AuthService authService = new AuthServiceImpl();
        UserCredentials userCredentials = authService.userAuthentication(txtEmail.getText(), txtPassword.getText());

        if (userCredentials != null && userCredentials.getRole().equals("ADMIN")){
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
        } else if (userCredentials != null && userCredentials.getRole().equals("EMPLOYEE")) {
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
        }else {
            CustomAlert.errorAlert("Login Error", new Exception("Login Failed"));
            System.out.println("Login Failed");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Font customFont = Font.loadFont(getClass().getResource("/fonts/Poppins-Medium.ttf").toExternalForm(), 40);
        //heading.setFont(customFont);
    }
}
