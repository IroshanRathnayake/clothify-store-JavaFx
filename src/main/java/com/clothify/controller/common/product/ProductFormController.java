package com.clothify.controller.common.product;

import com.clothify.dto.Product;
import com.clothify.dto.Supplier;
import com.clothify.service.ServiceFactory;
import com.clothify.service.custom.ProductService;
import com.clothify.service.custom.SupplierService;
import com.clothify.util.CustomAlert;
import com.clothify.util.ServiceType;
import com.clothify.validation.ProductValidation;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import lombok.Getter;

import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    @FXML
    private ScrollPane productScrollPane;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private ComboBox<String> cmbProductCategory;

    @FXML
    private ComboBox<String> cmbProductSize;

    @FXML
    private ComboBox<String> cmbSupplier;

    @FXML
    private ImageView iconAddProduct;

    @FXML
    private Label lblProductID;

    @FXML
    private GridPane productGrid;

    @FXML
    private ImageView productImage;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtProductPrice;

    @FXML
    private TextField txtProductQuantity;

    @FXML
    private TextField txtSearch;

    @FXML
    private JFXButton btnImageChooser;

    private final ProductService service = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    private final SupplierService supplierService = ServiceFactory.getInstance().getServiceType(ServiceType.SUPPLIER);
    private String encodedImage;
    private ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    private List<Product> productList;
    @Getter
    public static ProductFormController productFormController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productFormController = this;
       loadProducts();

        //Set Supplier Names
        supplierList.addAll(supplierService.getAllSuppliers());
        ObservableList<String> companyList = FXCollections.observableArrayList();
        for (Supplier supplier : supplierList) {
            companyList.add(supplier.getCompany());
        }
        cmbSupplier.setItems(companyList);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        double unitPrice=0.0;
        int quantity=0;

        try{
            unitPrice=Double.parseDouble(txtProductPrice.getText());
            quantity=Integer.parseInt(txtProductQuantity.getText());
        }catch (Exception e){
            CustomAlert.errorAlert("Error",e);
        }
        Product product = new Product(
                lblProductID.getText(),
                txtProductName.getText(),
                cmbProductCategory.getValue(),
                cmbProductSize.getValue(),
                getSupplierID(cmbSupplier.getValue()),
                unitPrice,
                quantity,
                encodedImage
        );

        //Validate Null Inputs
        if(ProductValidation.isValid(product)){
            //Validate Duplicate products
            if(!ProductValidation.isDuplicate(productList, product.getId())){
                addNewProduct(product);
            }else{
                updateProduct(product);
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
                "Do you want to delete "+txtProductName.getText() + " ?",
                "/img/icon/information-48.png"
        );

        if(result.get() == ButtonType.OK){
            deleteProduct();
            loadProducts();
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
    void iconAddProductOnAction(MouseEvent event) {
        System.out.println(generateNextID());
        lblProductID.setText(generateNextID());
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
        Product product = service.searchProduct(txtSearch.getText());
        if(product != null) {
            loadProductDetails(product);
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.WARNING,
                    "Clothify Store",
                    "Product Not Found!",
                    "/img/icon/warning-48.png"
            );
        }
    }

    @FXML
    void btnImageChooserOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        //Get Current Window
        Window window = ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile != null) {
            try {
                encodedImage = encodeImageFile(selectedFile);
                Image image = decodeImageFile(encodedImage);
                productImage.setImage(image);

            } catch (IOException e) {
                CustomAlert.errorAlert("Error",e);
            }
        }
    }

    //Add New Product
    private void addNewProduct(Product product) {
        if(service.addProduct(product)){
            loadProducts();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            disableTextField();
            clearFields();
            lblProductID.setText("P000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Product Added Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while adding "+product.getName(),
                    "/img/icon/error-48.png"
            );
        }
    }
    //Update Product
    private void updateProduct(Product product){
        if(service.updateProduct(product)){
            loadProducts();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            disableTextField();
            clearFields();
            lblProductID.setText("P000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Product Updated Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while updating "+product.getName(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Delete Product
    private void deleteProduct(){
        if(service.deleteProduct(lblProductID.getText())){
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            disableTextField();
            clearFields();
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Product Deleted Successfully!",
                    "/img/icon/success-48.png"
            );
        }else{
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while deleting "+txtProductName.getText(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Encode Image using base64
    private String encodeImageFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] imageBytes = new byte[(int) file.length()];
        fileInputStream.read(imageBytes);
        fileInputStream.close();

        return Base64.getEncoder().encodeToString(imageBytes);
    }

    //Decode Image
    public Image decodeImageFile(String encodedImage) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(encodedImage);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        return new Image(inputStream);
    }

    //Load Products
    private void loadProducts(){
        productList = service.getAllProducts();
        int column = 0;
        int row = 1;
        try {
            for (Product product : productList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/common/product/product_card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ProductCardController productCardController = fxmlLoader.getController();
                productCardController.setProduct(product);

                if (column == 4) {
                    column = 0;
                    row++;
                }
                productGrid.add(anchorPane, column++, row);

                //Grid width
                productGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                productGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                productGrid.setMaxWidth(Region.USE_PREF_SIZE);

                //Grid height
                productGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                productGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                productGrid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Set Product Category
        ObservableList<String> productCategory = FXCollections.observableArrayList();
        productCategory.add("GENTS");
        productCategory.add("LADIES");
        productCategory.add("KIDS");
        cmbProductCategory.setItems(productCategory);

        //Set Product Category
        ObservableList<String> productSize = FXCollections.observableArrayList();
        productSize.add("XXL");
        productSize.add("XL");
        productSize.add("L");
        productSize.add("M");
        productSize.add("S");
        productSize.add("XS");
        productSize.add("PRE");
        cmbProductSize.setItems(productSize);

    }

    //Load Product Data to Right Panel
    public void loadProductDetails(Product product) {
        lblProductID.setText(product.getId());
        txtProductName.setText(product.getName());
        cmbProductCategory.setValue(product.getCategory());
        cmbProductSize.setValue(product.getSize());
        cmbSupplier.setValue(getSupplierName(product.getSupplierID()));
        txtProductPrice.setText(product.getUnitPrice().toString());
        txtProductQuantity.setText(product.getQuantity().toString());
        try {
            productImage.setImage(decodeImageFile(product.getImage()));
        } catch (IOException e) {
            CustomAlert.errorAlert("Clothify Store",e);
        }
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);

    }

    //Find Supplier ID
    private String getSupplierID(String company){
        for (Supplier supplier : supplierList) {
            if(supplier.getCompany().equals(company)){
                return supplier.getId();
            }
        }
        return null;
    }

    //Find Supplier Name
    private String getSupplierName(String id){
        for (Supplier supplier : supplierList) {
            if(supplier.getId().equals(id)){
                return supplier.getCompany();
            }
        }
        return null;
    }

    //Generate Next ID
    private String generateNextID(){
        String base = "P";
        int id = Integer.parseInt(service.getLastProductID());
        System.out.println(id);

        if(id < 9){
            base+="00";
        }else if(id < 99){
            base += "0";
        }
        return (base + (id + 1));
    }

    //Element behaviours
    private void enableTextField(){
        txtProductName.setEditable(true);
        txtProductPrice.setEditable(true);
        txtProductQuantity.setEditable(true);
    }

    private void disableTextField(){
        txtProductName.setEditable(false);
        txtProductPrice.setEditable(false);
        txtProductQuantity.setEditable(false);
    }

    private void clearFields(){
        cmbSupplier.setValue(null);
        cmbProductSize.setValue(null);
        cmbProductCategory.setValue(null);
        txtProductName.setText(null);
        txtProductPrice.setText(null);
        txtProductQuantity.setText(null);
        productImage.setImage(new Image(getClass().getResourceAsStream("/img/product/no-image.png")));
    }


}
