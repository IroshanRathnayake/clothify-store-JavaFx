package com.clothify.controller.admin.dashboard;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class AdminDashboardBaseFormController implements Initializable {

    @FXML
    private JFXButton btnCustomer;

    @FXML
    private JFXButton btnDashboard;

    @FXML
    private JFXButton btnEmployee;

    @FXML
    private JFXButton btnInventory;

    @FXML
    private JFXButton btnLogout;

    @FXML
    private JFXButton btnOrders;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private JFXButton btnProduct;

    @FXML
    private JFXButton btnSettings;

    @FXML
    private JFXButton btnSupplier;

    @FXML
    private BorderPane mainBorderPane;


    private List<JFXButton> buttonList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonList = Arrays.asList(btnDashboard, btnPlaceOrder, btnOrders, btnProduct, btnSupplier, btnEmployee, btnInventory, btnCustomer, btnSettings);
        changeTheButtonStyle(btnDashboard);
        loadContent("/view/admin/dashboard/admin_dashboard_form.fxml");
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        changeTheButtonStyle(btnDashboard);
        loadContent("/view/admin/dashboard/admin_dashboard_form.fxml");

    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        changeTheButtonStyle(btnPlaceOrder);
        loadContent("/view/common/order/place_order_form.fxml");
    }

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        changeTheButtonStyle(btnCustomer);
    }


    @FXML
    void btnEmployeeOnAction(ActionEvent event) {
        changeTheButtonStyle(btnEmployee);
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        changeTheButtonStyle(btnInventory);
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {
        logout();
    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        changeTheButtonStyle(btnOrders);
    }

    @FXML
    void btnProductOnAction(ActionEvent event) {
        changeTheButtonStyle(btnProduct);
    }

    @FXML
    void btnSettingsOnAction(ActionEvent event) {
        changeTheButtonStyle(btnSettings);
    }

    @FXML
    void btnSupplierOnAction(ActionEvent event) {
        changeTheButtonStyle(btnSupplier);
    }
    @FXML
    void menubarCloseOnAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void menubarLogoutOnAction(ActionEvent event) {
        logout();
    }

    private void changeTheButtonStyle(JFXButton button) {
        for (JFXButton jfxButton : buttonList) {
            jfxButton.setStyle("-fx-background-color: #fff");
        }
        button.setStyle("-fx-background-color: #C4E3FF");
    }

    //Load Content
    private void loadContent(String fxmlName) {
        try {
            AnchorPane content = FXMLLoader.load(getClass().getResource(fxmlName));
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //Logout Action
    private void logout(){

        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Cothify Store");
        logoutAlert.setContentText("Do you want to logout?");
        Optional<ButtonType> buttonType = logoutAlert.showAndWait();
        if(buttonType.isPresent() && buttonType.get().equals(ButtonType.OK)) {
            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(
                        FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
                stage.setTitle("Dashboard");
                stage.setResizable(false);
                stage.getIcons().add(new Image("img/logo-round.png"));
                stage.show();
                btnDashboard.getScene().getWindow().hide();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

