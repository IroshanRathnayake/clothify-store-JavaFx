package com.clothify.controller.common.order;

import com.clothify.dto.*;
import com.clothify.service.ServiceFactory;
import com.clothify.service.custom.CustomerService;
import com.clothify.service.custom.OrderService;
import com.clothify.service.custom.ProductService;
import com.clothify.util.CustomAlert;
import com.clothify.util.PDFGenerator;
import com.clothify.util.ServiceType;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class OrdersFormController implements Initializable {

    @FXML
    private JFXButton btnPrint;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private TableColumn<?, ?> colCustomerID;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colOrderID;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private Label lblCustomerID;

    @FXML
    private Label lblOrderID;

    @FXML
    private TableColumn<?, ?> colProductName;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colDiscount;

    @FXML
    private TableView<OrderDetailTM> tblProductList;

    @FXML
    private TableView<OrderTM> tblOrder;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtTotal;

    private final OrderService service = ServiceFactory.getInstance().getServiceType(ServiceType.ORDER);
    private final CustomerService customerService = ServiceFactory.getInstance().getServiceType(ServiceType.CUSTOMER);
    private final ProductService productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);
    private ObservableList<Order> allOrders;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();

        tblOrder.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            setValuesToFields(newValue);
        }));
    }

    private void setValuesToFields(OrderTM newValue) {
        lblOrderID.setText(newValue.getId());
        lblCustomerID.setText(newValue.getCustomerId());
        txtDate.setText(newValue.getDate().toString());
        txtTotal.setText(newValue.getTotal().toString());

        //Get Customer Name
        String customerName = customerService.getCustomerById(newValue.getCustomerId()).getName();
        txtCustomerName.setText(customerName);

        tblProductList.getItems().clear();
        //Get ProductList
        ObservableList<OrderDetail> orderDetails = service.getOrderDetails(newValue.getId());
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        ObservableList<OrderDetailTM> orderDetailTMS = FXCollections.observableArrayList();
        for (OrderDetail orderDetail : orderDetails) {
            Product product = productService.searchProduct(orderDetail.getProductId());
            orderDetailTMS.add(new OrderDetailTM(
                    product.getName(),
                    orderDetail.getQuantity(),
                    orderDetail.getDiscount()
            ));
        }
        tblProductList.setItems(orderDetailTMS);

    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {
        PDFGenerator pdfGenerator = new PDFGenerator();
        pdfGenerator.downloadPdf((Stage) btnPrint.getScene().getWindow(), allOrders);
    }

    @FXML
    void iconFilterOnAction(MouseEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {

    }

    //Load product to table
    private void loadTable() {
        //Initialize the table columns
        colOrderID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnDelete"));

        allOrders = service.getAllOrders();
        ObservableList<OrderTM> orderTMs = FXCollections.observableArrayList();

        for (Order order : allOrders) {
            ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/img/icon/delete.png")));
            JFXButton deleteButton = new JFXButton();
            deleteButton.setGraphic(deleteIcon);
            deleteButton.setCursor(Cursor.HAND);

            OrderTM orderTM = new OrderTM(
                    order.getId(),
                    order.getDate(),
                    order.getCustomerId(),
                    order.getTotal(),
                    deleteButton
            );

            // Add event handler to btnDelete
            deleteButton.setOnAction(event -> {
                btnDeleteOnAction(orderTM);
            });
            orderTMs.add(orderTM);
        }

        tblOrder.setItems(orderTMs);
    }

    private void btnDeleteOnAction(OrderTM orderTM) {
        Optional<ButtonType> result = CustomAlert.confirmationAlert(
                "Confirmation",
                "Do you want to delete ?",
                "/img/icon/information-48.png"
        );

        if (result.get() == ButtonType.OK) {
            System.out.println(orderTM.getId());
            if (service.deleteOrder(orderTM.getId())) {
                loadTable();
                CustomAlert.showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Customer Deleted Successfully!",
                        "/img/icon/success-48.png"
                );
            } else {
                CustomAlert.showAlert(
                        Alert.AlertType.ERROR,
                        "Error",
                        "Error Occurred while deleting " + orderTM.getId(),
                        "/img/icon/error-48.png"
                );
            }
        }
    }

}
