package com.clothify.controller.common.order;

import com.clothify.controller.common.product.ProductFormController;
import com.clothify.dto.Product;
import com.clothify.validation.ImageHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class OrderProductCardController implements Initializable {

    @FXML
    private Label lblProductName;

    @FXML
    private Label lblProductPrice;

    @FXML
    private Label lblQuantity;

    @FXML
    private ImageView productImg;

    @FXML
    private Spinner<Integer> quantitySpinner;

    private Product product;

    @FXML
    void productCardOnClick(MouseEvent event) {
        PlaceOrderFormController.getPlaceOrderFormController().addToCart(product,quantitySpinner.getValue());
    }

    public void setProduct(Product product) {
        this.product = product;
        lblProductName.setText(product.getName());
        lblProductPrice.setText(product.getUnitPrice().toString());
        lblQuantity.setText(product.getQuantity().toString());

        Image image = null;
        try {
            image = ImageHandler.decodeImageFile(product.getImage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productImg.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1);
        valueFactory.setValue(1);
        quantitySpinner.setValueFactory(valueFactory);
    }
}
