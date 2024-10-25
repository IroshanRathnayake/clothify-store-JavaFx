package com.clothify.controller.common.order;

import com.clothify.dto.*;
import com.clothify.service.ServiceFactory;
import com.clothify.service.custom.CustomerService;
import com.clothify.service.custom.OrderService;
import com.clothify.service.custom.ProductService;
import com.clothify.util.CustomAlert;
import com.clothify.util.ProductType;
import com.clothify.util.ServiceType;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlaceOrderFormController implements Initializable {

    @FXML
    private JFXButton btnGents;

    @FXML
    private JFXButton btnKids;

    @FXML
    private JFXButton btnLadies;

    @FXML
    private JFXButton btnPlaceOrder;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblDiscount;

    @FXML
    private Label lblOrderID;

    @FXML
    private Label lblSubTotal;

    @FXML
    private Label lblTotal;

    @FXML
    private GridPane productGrid;

    @FXML
    private ScrollPane productScrollPane;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colProductName;

    @FXML
    private TableColumn<?, ?> colQty;

    @FXML
    private TableView<CartTM> tblCart;

    @FXML
    private TextField txtSearch;

    @Getter
    private static PlaceOrderFormController placeOrderFormController;
    private final OrderService service = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    private final CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    private List<Product> productList;
    private List<JFXButton> buttonList;
    private final ObservableList<CartTM> cartList = FXCollections.observableArrayList();
    private Customer searchedCustomer;
    private double total;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        placeOrderFormController = this;
        buttonList = Arrays.asList(btnGents, btnLadies, btnKids);
        productList = productService.getAllProducts();
        loadProducts(ProductType.GENTS);
        changeTheButtonStyle(btnGents);
        loadDateTime();
        generateNextID();

        // Add listener to search
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue);
        });
    }

    // Filter products
    private void searchProducts(String searchQuery) {
        List<Product> filteredProducts = new ArrayList<>();

        if (searchQuery == null || searchQuery.isEmpty()) {
            loadProducts(ProductType.GENTS);
        } else {
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    filteredProducts.add(product);
                }
            }
            loadProductToGridPane(filteredProducts);
        }
    }

    @FXML
    void btnGentsOnAction(ActionEvent event) {
        loadProducts(ProductType.GENTS);
        changeTheButtonStyle(btnGents);
    }

    @FXML
    void btnKidsOnAction(ActionEvent event) {
        loadProducts(ProductType.KIDS);
        changeTheButtonStyle(btnKids);
    }

    @FXML
    void btnLadiesOnAction(ActionEvent event) {
        loadProducts(ProductType.LADIES);
        changeTheButtonStyle(btnLadies);
    }

    @FXML
    void btnPlaceOrderOnAction(ActionEvent event) {
        Order order = new Order(
                lblOrderID.getText(),
                loadDateTime(),
                searchedCustomer.getId(),
                total
        );

        List<OrderDetail> orderDetails = new ArrayList<>();

        cartList.forEach(obj -> {
            orderDetails.add(
                    new OrderDetail(
                            lblOrderID.getText(),
                            obj.getProductId(),
                            obj.getQuantity(),
                            0.0)
            );
        });

        try {
            if (service.placeOrder(order, orderDetails)) {
                CustomAlert.showAlert(
                        Alert.AlertType.WARNING,
                        "Clothify Store",
                        "Order Placed Successfully",
                        "/img/icon/success-48.png"
                );
                clearAll();
                generateNextID();
            } else {
                CustomAlert.showAlert(
                        Alert.AlertType.WARNING,
                        "Clothify Store",
                        "Order Placed Failed",
                        "/img/icon/error-48.png"
                );
            }
        } catch (SQLException e) {
            CustomAlert.errorAlert("Clothify Store", e);
        }
    }

    @FXML
    void iconAddCustomerOnClick(MouseEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(
                    FXMLLoader.load(getClass().getResource("/view/common/customer/add_customer_form.fxml"))));
            stage.setTitle("Add Customer");
            stage.setResizable(false);
            stage.getIcons().add(new Image("img/logo-round.png"));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void iconFilterOnClick(MouseEvent event) {

    }

    @FXML
    void txtPhoneNumberOnAction(ActionEvent event) {
        searchedCustomer = customerService.searchCustomer(txtPhoneNumber.getText());
        if (searchedCustomer != null) {
            txtCustomerName.setText(searchedCustomer.getName());
            btnPlaceOrder.setDisable(false);
        } else {
            CustomAlert.showAlert(
                    Alert.AlertType.WARNING,
                    "Clothify Store",
                    "Customer Not Found!",
                    "/img/icon/warning-48.png"
            );
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) throws IOException {
    }

    //Load Products
    private void loadProducts(ProductType productType) {
        List<Product> categorizedProductList = new ArrayList<>();

        switch (productType) {
            case GENTS:
                for (Product product : productList) {
                    if (product.getCategory().equals("GENTS")) {
                        categorizedProductList.add(product);
                    }
                }
                break;

            case LADIES:
                for (Product product : productList) {
                    if (product.getCategory().equals("LADIES")) {
                        categorizedProductList.add(product);
                    }
                }
                break;
            case KIDS:
                for (Product product : productList) {
                    if (product.getCategory().equals("KIDS")) {
                        categorizedProductList.add(product);
                    }
                }
                break;
        }
        loadProductToGridPane(categorizedProductList);
    }

    //load product to Grid Pane
    private void loadProductToGridPane(List<Product> categorizedProductList) {
        productGrid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (Product product : categorizedProductList) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/common/order/order_product_card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                OrderProductCardController orderProductCardController = fxmlLoader.getController();
                orderProductCardController.setProduct(product);

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
    }

    //Product add to cart
    public void addToCart(Product product, Integer quantity) {
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

        if (quantity > product.getQuantity()) {
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Clothify Store",
                    "Insufficient Stock",
                    "/img/icon/error-48.png"
            );
            return;
        }

        ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/icon/delete.png")));
        JFXButton btnDelete = new JFXButton();
        btnDelete.setGraphic(deleteIcon);
        btnDelete.setCursor(Cursor.HAND);

        cartList.removeIf(oldProduct -> oldProduct.getProductId().equals(product.getId()));

        CartTM cartItem = new CartTM(
                product.getId(),
                product.getName(),
                quantity,
                product.getUnitPrice(),
                btnDelete
        );

        // Add event handler to btnDelete
        btnDelete.setOnAction(event -> {
            cartList.remove(cartItem);
            tblCart.setItems(cartList);
            setTotal();
        });

        cartList.add(cartItem);
        tblCart.setItems(cartList);
        setTotal();

    }

    //Set Total
    private void setTotal() {
        total = 0;
        for (CartTM cartTM : cartList) {
            total += cartTM.getUnitPrice() * cartTM.getQuantity();
        }

        lblSubTotal.setText(String.format("%.2f", total));
        lblTotal.setText(String.format("LKR %.2f", total));
    }

    private void changeTheButtonStyle(JFXButton button) {
        for (JFXButton jfxButton : buttonList) {
            jfxButton.setStyle("-fx-background-color: #C4E3FF");
        }
        button.setStyle("-fx-background-color: #308EDF");
    }

    //Date
    private LocalDateTime loadDateTime() {
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNow = f.format(date);

        lblDate.setText(dateNow.substring(0, 10));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(dateNow, formatter);

        return ldt;
    }

    //Generate Next ID
    private void generateNextID() {
        String base = "D";
        int id = Integer.parseInt(service.getLastOrderID());

        if (id < 10) {
            base += "00";
        } else if (id < 100) {
            base += "0";
        }
        lblOrderID.setText(base + (id + 1));
    }

    //Clear Fields
    private void clearAll(){
        tblCart.getItems().clear();
        txtSearch.setText(null);
        txtPhoneNumber.setText(null);
        txtCustomerName.setText(null);
        lblSubTotal.setText("0.00");
        lblDiscount.setText("0.00");
        lblTotal.setText("LKR 0.00");
    }

}
