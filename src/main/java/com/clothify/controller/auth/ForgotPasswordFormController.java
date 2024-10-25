package com.clothify.controller.auth;

import com.clothify.dto.UserCredentials;
import com.clothify.security.SecurityConfig;
import com.clothify.service.ServiceFactory;
import com.clothify.service.custom.UserCredentialService;
import com.clothify.util.CustomAlert;
import com.clothify.util.EmailService;
import com.clothify.util.ServiceType;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ForgotPasswordFormController implements Initializable {

    @FXML
    private JFXButton btnChangePassword;

    @FXML
    private JFXButton btnSend;

    @FXML
    private Label lblOtpSent;

    @FXML
    private Label txtMessage;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    private TextField txtEmail;

    @FXML
    private JFXPasswordField txtNewPassword;

    @FXML
    private TextField txtOTPFive;

    @FXML
    private TextField txtOTPFour;

    @FXML
    private TextField txtOTPOne;

    @FXML
    private TextField txtOTPThree;

    @FXML
    private TextField txtOTPTwo;

    private final UserCredentialService service = ServiceFactory.getInstance().getServiceType(ServiceType.USER_CREDENTIALS);
    private final EmailService emailService = new EmailService();
    private String generatedOTP;

    @FXML
    void btnChangePasswordOnAction(ActionEvent event) {
        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if (newPassword.equals(confirmPassword)) {
            String hashedPassword = SecurityConfig.hashPassword(newPassword);
            UserCredentials oldUserCredential = service.getUserCredentialsByEmail(txtEmail.getText());

            UserCredentials newUserCredential = new UserCredentials(
                    oldUserCredential.getId(),
                    oldUserCredential.getEmployeeID(),
                    oldUserCredential.getEmail(),
                    hashedPassword,
                    oldUserCredential.getRole(),
                    LocalDateTime.now()
            );

            if (service.updateUserCredentials(newUserCredential)) {
                CustomAlert.showAlert(
                        Alert.AlertType.INFORMATION,
                        "Clothify Store",
                        "Password changed successfully!",
                        "/img/icon/success-48.png"
                );
                txtNewPassword.clear();
                txtConfirmPassword.clear();
                txtNewPassword.setDisable(true);
                txtConfirmPassword.setDisable(true);
                txtEmail.clear();
                btnChangePassword.setDisable(true);
                txtOTPOne.clear();
                txtOTPThree.clear();
                txtOTPTwo.clear();
                txtOTPFive.clear();
                txtOTPFour.clear();
                txtMessage.setVisible(false);

                btnChangePassword.getScene().getWindow().hide();
            } else {
                CustomAlert.showAlert(
                        Alert.AlertType.INFORMATION,
                        "Clothify Store",
                        "Password could not be changed!",
                        "/img/icon/error-48.png"
                );
            }
        } else {
            CustomAlert.showAlert(
                    Alert.AlertType.WARNING,
                    "Clothify Store",
                    "Password does not match!",
                    "/img/icon/warning-48.png"
            );
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        lblOtpSent.setVisible(true);

        String email = txtEmail.getText();
        if (email.isEmpty()) {
            CustomAlert.showAlert(
                    Alert.AlertType.WARNING,
                    "Clothify Store",
                    "Please enter an email!",
                    "/img/icon/warning-48.png"
            );
            return;
        }

        generatedOTP = emailService.sendOTP(email);
        if (generatedOTP != null) {
            lblOtpSent.setVisible(true);
            txtMessage.setVisible(true);
        } else {
            System.out.println("OTP not sent");
        }
    }

    @FXML
    void txtOTPFiveOnAction(ActionEvent event) {
        String enteredOTP = txtOTPOne.getText() + txtOTPTwo.getText() + txtOTPThree.getText() + txtOTPFour.getText() + txtOTPFive.getText();

        if (enteredOTP.equals(generatedOTP)) {
            txtMessage.setText("OTP verified");
            txtMessage.setStyle("-fx-text-fill:  #308EDF;");
            lblOtpSent.setVisible(false);

            txtNewPassword.setDisable(false);
            txtConfirmPassword.setDisable(false);
            btnChangePassword.setDisable(false);
        } else {
            txtMessage.setText("OTP not verified");
            txtMessage.setStyle("-fx-text-fill:  #F44336;");
            lblOtpSent.setVisible(false);
        }
    }

    public void setTextFieldLimit(TextField textField, int maxLength) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > maxLength) {
                textField.setText(oldValue);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTextFieldLimit(txtOTPOne, 1);
        setTextFieldLimit(txtOTPTwo, 1);
        setTextFieldLimit(txtOTPThree, 1);
        setTextFieldLimit(txtOTPFour, 1);
        setTextFieldLimit(txtOTPFive, 1);
    }
}
