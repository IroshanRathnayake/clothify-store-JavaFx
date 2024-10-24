package com.clothify.controller.admin.supplier;

import com.clothify.dto.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SupplierCardController {

    @FXML
    private Label lblCompany;

    @FXML
    private Label lblPhoneNumber;

    @FXML
    private ImageView supplierImg;

    private Supplier supplier;

    @FXML
    void customerCardOnClick(MouseEvent event) {
        SupplierFormController.getSupplierFormController().loadSupplierDetails(supplier);
    }

    public void setData(Supplier supplier) {
        this.supplier = supplier;
        lblCompany.setText(supplier.getCompany());
        lblPhoneNumber.setText(supplier.getPhoneNumber());
        Image image = new Image(getClass().getResourceAsStream("/img/icon/dashboard-icon/Business Building.png"));
        supplierImg.setImage(image);
    }
}
