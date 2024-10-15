package com.clothify.controller.common.customer;

import com.clothify.dto.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CustomerCardController {

    @FXML
    private ImageView customerImg;

    @FXML
    private Label lblCustomerName;

    @FXML
    private Label lblCustomerPhone;

    private Customer customer;

    @FXML
    void customerCardOnClick(MouseEvent event) {
        CustomerFormController.getCustomerFormController().loadCustomerDetails(customer);
    }

    public void setData(Customer customer){
        this.customer = customer;
        lblCustomerName.setText(customer.getName());
        lblCustomerPhone.setText(customer.getPhoneNumber());

        String imgSrc = "";
        if(customer.getTitle().equals("Mr")){
            imgSrc = "/img/icon/dashboard-icon/man.png";
        }else{
            imgSrc = "/img/icon/dashboard-icon/girl.png";
        }
        Image image = new Image(getClass().getResourceAsStream(imgSrc));
        customerImg.setImage(image);
    }
}
