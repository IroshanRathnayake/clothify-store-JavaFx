package com.clothify.controller.common.product;

import com.clothify.dto.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ProductCardController {

    @FXML
    private Label lblProductName;

    @FXML
    private Label lblProductPrice;

    @FXML
    private ImageView productImg;

    private Product product;

    @FXML
    void productCardOnClick(MouseEvent event) {
        ProductFormController.getProductFormController().loadProductDetails(product);
    }

    public void setProduct(Product product) {
        this.product = product;
        lblProductName.setText(product.getName());
        lblProductPrice.setText(product.getUnitPrice().toString());

        Image image = null;
        try {
            image = ProductFormController.getProductFormController().decodeImageFile(product.getImage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        productImg.setImage(image);
    }

}
