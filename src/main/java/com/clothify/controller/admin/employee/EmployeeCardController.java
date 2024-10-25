package com.clothify.controller.admin.employee;

import com.clothify.dto.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EmployeeCardController {

    @FXML
    private ImageView employeeImage;

    @FXML
    private Label lblEmployeeName;

    @FXML
    private Label lblEmployeePhone;

    private Employee employee;

    @FXML
    void customerCardOnClick(MouseEvent event) {
        EmployeeFormController.getEmployeeFormController().loadEmployeeDetails(employee);
    }

    public void setData(Employee employee) {
        this.employee = employee;
        lblEmployeeName.setText(employee.getName());
        lblEmployeePhone.setText(employee.getPhoneNumber());

        String imgSrc = "";
        if(employee.getLoginAccess()){
            imgSrc = employee.getTitle().equals("Mr") ?
                    "/img/icon/employee/star-men.png" :
                    "/img/icon/employee/star-women.png";
        }else{
            imgSrc = employee.getTitle().equals("Mr") ?
                    "/img/icon/employee/man.png" :
                    "/img/icon/employee/women.png";
        }

        Image image = new Image(getClass().getResourceAsStream(imgSrc));
        employeeImage.setImage(image);
    }

}
