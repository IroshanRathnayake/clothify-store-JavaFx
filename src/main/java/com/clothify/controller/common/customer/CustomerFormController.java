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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {
    @FXML
    private GridPane customerGrid;

    @FXML
    private ScrollPane customerScrollPane;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

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

    @FXML
    private TextField txtSearchCustomer;

    @Getter
    private static CustomerFormController customerFormController;

    private final CustomerService service = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    private List<Customer> customerList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerFormController = this;
        loadCustomers();
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
            //Validate Duplicate Customers
            if(!CustomerValidation.isDuplicate(customerList, customer.getId())){
                addNewCustomer(customer);
            }else{
                updateCustomer(customer);
            }
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
    void btnDeleteOnAction(ActionEvent event) {

        Optional<ButtonType> result = CustomAlert.confirmationAlert(
                "Confirmation",
                "Do you want to delete "+txtName.getText() + " ?",
                "/img/icon/information-48.png"
        );

        if(result.get() == ButtonType.OK){
            deleteCustomer();
        }
    }

    @FXML
    void filterOnAction(MouseEvent event) {

    }

    @FXML
    void cmbTitleOnAction(ActionEvent event) {
        //Set profile image
        loadCustomerImage((cmbTitle.getValue() != null) ? cmbTitle.getValue() : "Mr");
    }

    @FXML
    void iconNewCustomerOnAction(MouseEvent event) {
        System.out.println(generateNextID());
        lblCustomerID.setText(generateNextID());
        btnAdd.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        enableTextField();
        clearFields();
    }

    @FXML
    void txtSearchCustomerOnAction(ActionEvent event) {
        Customer customer = service.searchCustomer(txtSearchCustomer.getText());
        if(customer != null) {
            loadCustomerDetails(customer);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.WARNING,
                    "Clothify Store",
                    "Customer Not Found!",
                    "/img/icon/warning-48.png"
            );
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        enableTextField();
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    //Add new customer
    private void addNewCustomer(Customer customer){
        if(service.addCustomer(customer)){
            loadCustomers();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            disableTextField();
            clearFields();
            lblCustomerID.setText("C000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Customer Added Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while adding "+customer.getName(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Update Customer
    private void updateCustomer(Customer customer){
        if(service.updateCustomer(customer)){
            loadCustomers();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            disableTextField();
            clearFields();
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Customer Updated Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while updating "+customer.getName(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Delete Customer
    private void deleteCustomer(){
        if(service.deleteCustomer(lblCustomerID.getText())){
            loadCustomers();
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            disableTextField();
            clearFields();
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Customer Deleted Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while deleting "+txtName.getText(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Load all customers
    private void loadCustomers(){
        customerList = service.getAllCustomers();

        int column = 0;
        int row = 1;
        try {
            for (Customer customer : customerList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/common/customer/customer_card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                CustomerCardController customerCardController = fxmlLoader.getController();
                customerCardController.setData(customer);

                if (column == 4) {
                    column = 0;
                    row++;
                }
                customerGrid.add(anchorPane, column++, row);

                //Grid width
                customerGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                customerGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                customerGrid.setMaxWidth(Region.USE_PREF_SIZE);

                //Grid height
                customerGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                customerGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                customerGrid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Set Titles
        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("Mr");
        titles.add("Miss");
        titles.add("Mrs");
        cmbTitle.setItems(titles);
    }

    //Load Customer Data to Right Panel
    public void loadCustomerDetails(Customer customer) {
        lblCustomerID.setText(customer.getId());
        cmbTitle.setValue(customer.getTitle());
        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtPhoneNumber.setText(customer.getPhoneNumber());
        txtEmail.setText(customer.getEmail());
        loadCustomerImage(customer.getTitle());
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
        disableTextField();

    }

    //Load Customer Image
    private void loadCustomerImage(String title){
        String imgSrc = title.equals("Mr") ? "/img/icon/dashboard-icon/man.png" : "/img/icon/dashboard-icon/girl.png";
        Image image = new Image(getClass().getResourceAsStream(imgSrc));
        customerImg.setImage(image);
    }

    //Generate Next ID
    private String generateNextID(){
        String base = "C";
        int id = Integer.parseInt(service.getLastCustomerId());

        if(id < 10){
            base+="00";
        }else if(id < 100){
            base += "0";
        }
        return (base + (id + 1));
    }

    //Element behaviours
    private void enableTextField(){
        txtName.setEditable(true);
        txtAddress.setEditable(true);
        txtPhoneNumber.setEditable(true);
        txtEmail.setEditable(true);
    }

    private void disableTextField(){
        txtName.setEditable(false);
        txtAddress.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtEmail.setEditable(false);
    }

    private void clearFields(){
        cmbTitle.setValue(null);
        txtName.setText(null);
        txtAddress.setText(null);
        txtPhoneNumber.setText(null);
        txtEmail.setText(null);
    }

}
