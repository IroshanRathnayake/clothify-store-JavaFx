package com.clothify.controller.admin.supplier;

import com.clothify.dto.Supplier;
import com.clothify.service.ServiceFactory;
import com.clothify.service.custom.SupplierService;
import com.clothify.util.CustomAlert;
import com.clothify.util.ServiceType;
import com.clothify.validation.SupplierValidation;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private Label lblSupplierID;

    @FXML
    private GridPane supplierGrid;

    @FXML
    private ScrollPane supplierScrollPane;

    @FXML
    private ImageView supplierImage;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCompany;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtOwner;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtSearch;

    @Getter
    private static SupplierFormController supplierFormController;

    private final SupplierService service = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
    private List<Supplier> supplierList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supplierFormController = this;
        loadSuppliers();

        // Add listener to search
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchSupplier(newValue);
        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Supplier supplier = new Supplier(
                lblSupplierID.getText(),
                txtCompany.getText(),
                txtOwner.getText(),
                txtAddress.getText(),
                txtPhone.getText(),
                txtEmail.getText()
        );

        //Validate Null Inputs
        if(SupplierValidation.isValid(supplier)){
            //Validate Duplicate Supplier
            if(!SupplierValidation.isDuplicate(supplierList, supplier.getId())){
                addNewSupplier(supplier);
            }else{
                updateSupplier(supplier);
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
                "Do you want to delete "+txtCompany.getText() + " ?",
                "/img/icon/information-48.png"
        );

        if(result.get() == ButtonType.OK){
            deleteSupplier();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        enableTextField();
        btnAdd.setDisable(false);
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    void iconAddSupplierOnAction(MouseEvent event) {
        lblSupplierID.setText(generateNextID());
        btnAdd.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        enableTextField();
        clearFields();
    }

    @FXML
    void iconFilterOnAction(MouseEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    //Add new supplier
    private void addNewSupplier(Supplier supplier){
        if(service.addSupplier(supplier)){
            loadSuppliers();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            disableTextField();
            clearFields();
            lblSupplierID.setText("S000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Supplier Added Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while adding "+supplier.getCompany(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Update supplier
    private void updateSupplier(Supplier supplier){
        if(service.updateSupplier(supplier)){
            loadSuppliers();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            disableTextField();
            clearFields();
            lblSupplierID.setText("S000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Supplier Updated Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while updating "+supplier.getCompany(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Delete supplier
    private void deleteSupplier(){
        if(service.deleteSupplier(lblSupplierID.getText())){
            loadSuppliers();
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            disableTextField();
            clearFields();
            lblSupplierID.setText("S000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Supplier Deleted Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while deleting "+txtCompany.getText(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Load suppliers
    private void loadSuppliers(){
        supplierList = service.getAllSuppliers();
        loadSuppliersToGridPane(supplierList);
    }

    //Load Suppliers to Grid Pane
    private void loadSuppliersToGridPane(List<Supplier> suppliers){
        supplierGrid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (Supplier supplier : suppliers) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/admin/supplier/supplier_card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                SupplierCardController supplierCardController = fxmlLoader.getController();
                supplierCardController.setData(supplier);

                if (column == 4) {
                    column = 0;
                    row++;
                }
                supplierGrid.add(anchorPane, column++, row);

                //Grid width
                supplierGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                supplierGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                supplierGrid.setMaxWidth(Region.USE_PREF_SIZE);

                //Grid height
                supplierGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                supplierGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                supplierGrid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Load Supplier Data to Right Panel
    public void loadSupplierDetails(Supplier supplier) {
        lblSupplierID.setText(supplier.getId());
        txtCompany.setText(supplier.getCompany());
        txtOwner.setText(supplier.getOwner());
        txtAddress.setText(supplier.getAddress());
        txtPhone.setText(supplier.getPhoneNumber());
        txtEmail.setText(supplier.getEmail());
        loadSupplierImage();
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
        disableTextField();

    }

    //Load Supplier Image
    private void loadSupplierImage(){
        String imgSrc = "/img/icon/dashboard-icon/Business Building.png";
        Image image = new Image(getClass().getResourceAsStream(imgSrc));
        supplierImage.setImage(image);
    }

    // Filter suppliers
    private void searchSupplier(String searchQuery) {
        List<Supplier> filteredSuppliers = new ArrayList<>();

        if (searchQuery == null || searchQuery.isEmpty()) {
            loadSuppliers();
        } else {
            for (Supplier supplier : supplierList) {
                if (supplier.getCompany().toLowerCase().contains(searchQuery.toLowerCase())) {
                    filteredSuppliers.add(supplier);
                }
            }
            loadSuppliersToGridPane(filteredSuppliers);
        }
    }

    //Generate Next ID
    private String generateNextID(){
        String base = "S";
        int id = Integer.parseInt(service.getLastSupplierId());

        if(id < 10){
            base+="00";
        }else if(id < 100){
            base += "0";
        }
        return (base + (id + 1));
    }


    //Element behaviours
    private void enableTextField(){
        txtCompany.setEditable(true);
        txtOwner.setEditable(true);
        txtAddress.setEditable(true);
        txtPhone.setEditable(true);
        txtEmail.setEditable(true);
    }

    private void disableTextField(){
        txtCompany.setEditable(false);
        txtOwner.setEditable(false);
        txtAddress.setEditable(false);
        txtPhone.setEditable(false);
        txtEmail.setEditable(false);
    }

    private void clearFields(){
        txtCompany.setText(null);
        txtOwner.setText(null);
        txtAddress.setText(null);
        txtPhone.setText(null);
        txtEmail.setText(null);
    }
}
