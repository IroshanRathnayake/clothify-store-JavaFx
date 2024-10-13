package com.clothify.controller.user;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class UserDashboardBaseFormController implements Initializable {

    @FXML
    private JFXButton btnCustomer;

    @FXML
    private JFXButton btnDashboard;

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
    private BorderPane mainBorderPane;

    //ArrayList to store buttons
    private List<JFXButton> buttonList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonList = Arrays.asList(btnDashboard, btnPlaceOrder, btnCustomer, btnOrders, btnProduct, btnInventory, btnSettings);
        changeTheButtonStyle(btnDashboard);
        loadContent("view/user/dashboard/user_dashboard_from.fxml");
    }
    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        changeTheButtonStyle(btnCustomer);
        loadContent("view/common/customer/customer_form.fxml");
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {
        changeTheButtonStyle(btnDashboard);
        loadContent("view/user/dashboard/user_dashboard_from.fxml");
    }

    @FXML
    void btnInventoryOnAction(ActionEvent event) {
        changeTheButtonStyle(btnInventory);
        loadContent("view/common/product/inventory_form.fxml");
    }

    @FXML
    void btnLogoutOnAction(ActionEvent event) {

    }

    @FXML
    void btnOrdersOnAction(ActionEvent event) {
        changeTheButtonStyle(btnOrders);
        loadContent("view/common/order/orders_form.fxml ");
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        changeTheButtonStyle(btnPlaceOrder);
        loadContent("view/common/order/place_order_form.fxml");
    }

    @FXML
    void btnProductOnAction(ActionEvent event) {
        changeTheButtonStyle(btnProduct);
        loadContent("view/common/product/product_form.fxml");
    }

    @FXML
    void btnSettingsOnAction(ActionEvent event) {
        changeTheButtonStyle(btnSettings);
    }

    @FXML
    void menubarCloseOnAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void menubarLogoutOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.getIcons().add(new Image("img/logo-round.png"));
            stage.show();
            btnDashboard.getScene().getWindow().hide();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Custom Methods
    //Change the button Style
    private void changeTheButtonStyle(JFXButton button) {
        for (JFXButton jfxButton : buttonList) {
            jfxButton.setStyle("-fx-background-color: #fff");
        }
        button.setStyle("-fx-background-color: #C4E3FF");
    }

    //Load Content
    private void loadContent(String fxmlName) {
        try {
            URL resourceUrl = getClass().getResource("/" + fxmlName);
            AnchorPane content = FXMLLoader.load(resourceUrl);
            mainBorderPane.setCenter(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
