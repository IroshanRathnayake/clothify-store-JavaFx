package com.clothify.controller.common.customer;

import com.clothify.dto.Customer;
import com.clothify.service.ServiceFactory;
import com.clothify.service.custom.CustomerService;
import com.clothify.util.CustomAlert;
import com.clothify.util.ServiceType;
import com.clothify.validation.CustomerValidation;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCustomerFormController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private ComboBox<String> cmbTitle;

    @FXML
    private ImageView customerImg;

    @FXML
    private Label lblCustomerID;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPhoneNumber;

    private final CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateNextID();

        //Set Titles
        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("Mr");
        titles.add("Miss");
        titles.add("Mrs");
        cmbTitle.setItems(titles);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Customer customer = new Customer(
                lblCustomerID.getText(),
                cmbTitle.getValue(),
                txtName.getText(),
                txtAddress.getText(),
                txtPhoneNumber.getText(),
                txtEmail.getText()
        );

        //Validate Null Inputs
        if(CustomerValidation.isValid(customer)){
            addNewCustomer(customer);
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.WARNING,
                    "Warning",
                    "Please fill all the fields",
                    "/img/icon/warning-48.png"
            );
        }
    }

    @FXML
    void cmbTitleOnAction(ActionEvent event) {
        //Set profile image
        loadCustomerImage((cmbTitle.getValue() != null) ? cmbTitle.getValue() : "Mr");
    }

    //Add new customer
    private void addNewCustomer(Customer customer){
        if(service.addCustomer(customer)){
            lblCustomerID.setText("C000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Customer Added Successfully!",
                    "/img/icon/success-48.png"
            );
            btnAdd.getScene().getWindow().hide();
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while adding "+customer.getName(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Load Customer Image
    private void loadCustomerImage(String title){
        String imgSrc = title.equals("Mr") ? "/img/icon/dashboard-icon/man.png" : "/img/icon/dashboard-icon/girl.png";
        Image image = new Image(getClass().getResourceAsStream(imgSrc));
        customerImg.setImage(image);
    }

    //Generate Next ID
    private void generateNextID(){
        String base = "C";
        int id = Integer.parseInt(service.getLastCustomerId());

        if(id < 10){
            base+="00";
        }else if(id < 100){
            base += "0";
        }
        lblCustomerID.setText(base + (id + 1));
    }
}
