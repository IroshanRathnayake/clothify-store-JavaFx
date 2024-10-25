package com.clothify.controller.admin.employee;

import com.clothify.dto.*;
import com.clothify.dto.Employee;
import com.clothify.service.ServiceFactory;
import com.clothify.service.custom.EmployeeService;
import com.clothify.service.custom.UserCredentialService;
import com.clothify.util.CustomAlert;
import com.clothify.util.ServiceType;
import com.clothify.validation.EmployeeValidation;
import com.clothify.validation.UserCredentialValidation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeFormController implements Initializable {

    @FXML
    private JFXButton btnAdd;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnUpdate;

    @FXML
    private JFXCheckBox checkAccount;

    @FXML
    private ComboBox<String> cmbRole;

    @FXML
    private ComboBox<String> cmbTitle;

    @FXML
    private GridPane employeeGrid;

    @FXML
    private ImageView employeeImage;

    @FXML
    private ScrollPane employeeScrollPane;

    @FXML
    private Label lblEmployeeID;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtPhoneNumber;

    @FXML
    private TextField txtPosition;

    @FXML
    private TextField txtSearch;

    @Getter
    private static EmployeeFormController employeeFormController;

    private final EmployeeService service = ServiceFactory.getInstance().getServiceType(ServiceType.EMPLOYEE);
    private final UserCredentialService userCredentialService = ServiceFactory.getInstance().getServiceType(ServiceType.USER_CREDENTIALS);
    private List<Employee> employeeList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeFormController = this;
        loadEmployees();

        // Add listener to search
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchEmployee(newValue);
        });
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        Employee employee = new Employee(
                lblEmployeeID.getText(),
                cmbTitle.getValue(),
                txtName.getText(),
                txtPosition.getText(),
                txtAddress.getText(),
                txtPhoneNumber.getText(),
                txtEmail.getText(),
                checkAccount.isSelected()
        );

        //Validate Null Inputs
        if (EmployeeValidation.isValid(employee)) {
            //Validate Duplicate Employees
            if (!EmployeeValidation.isDuplicate(employeeList, employee.getId())) {
                addNewEmployee(employee);
            } else {
                updateEmployee(employee);
            }
        } else {
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
                "Do you want to delete " + txtName.getText() + " ?",
                "/img/icon/information-48.png"
        );

        if (result.get() == ButtonType.OK) {
            deleteEmployee();
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
    void cmbRoleOnAction(ActionEvent event) {
        loadEmployeeImageByTitle(((cmbTitle.getValue() != null) ? cmbTitle.getValue() : "Mr"), checkAccount.isSelected());
    }

    @FXML
    void cmbTitleOnAction(ActionEvent event) {
        //Set profile image
        loadEmployeeImageByTitle(((cmbTitle.getValue() != null) ? cmbTitle.getValue() : "Mr"), checkAccount.isSelected());
    }


    @FXML
    void checkAccountOnAction(ActionEvent event) {
        boolean isSelected = checkAccount.isSelected();

        if (isSelected) {
            cmbRole.setDisable(false);
            txtPassword.setDisable(false);
        } else {
            cmbRole.setDisable(true);
            txtPassword.setDisable(true);
            cmbRole.setItems(null);
        }
    }

    @FXML
    void iconAddEmployeeOnAction(MouseEvent event) {
        lblEmployeeID.setText(generateNextID());
        checkAccount.setDisable(false);
        btnAdd.setDisable(false);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        enableTextField();
        clearFields();
    }

    @FXML
    void iconFilterOnAction(MouseEvent event) {

    }

    //Add new employee
    private void addNewEmployee(Employee employee) {
        if (service.addEmployee(employee)) {

            System.out.println(employee);
            //Create an account
            if (employee.getLoginAccess()) {
                int id = Integer.parseInt(userCredentialService.getLastUserCredentialId()) + 1;
                String nextID = Integer.toString(id);
                System.out.println(nextID);
                UserCredentials userCredentials = new UserCredentials(
                        nextID,
                        employee.getId(),
                        employee.getEmail(),
                        txtPassword.getText(),
                        cmbRole.getValue(),
                        LocalDateTime.now()
                );
                if (UserCredentialValidation.isValid(userCredentials)) {
                    userCredentialService.addUserCredential(userCredentials);
                }
            }

            //After adding
            loadEmployees();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            disableTextField();
            clearFields();
            lblEmployeeID.setText("E000");
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Employee Added Successfully!",
                    "/img/icon/success-48.png"
            );
        } else {
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while adding " + employee.getName(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Update Employee
    private void updateEmployee(Employee employee) {
        if (service.updateEmployee(employee)) {
            if (checkAccount.isSelected()) {
                if (employee.getLoginAccess()) {
                    UserCredentials userCredentials = userCredentialService.getUserCredentialsByEmployeeId(employee.getId());
                    if (userCredentials != null) {
                        UserCredentials newUserCredentials = new UserCredentials(
                                userCredentials.getId(),
                                userCredentials.getEmployeeID(),
                                userCredentials.getEmail(),
                                txtPassword.getText(),
                                cmbRole.getValue(),
                                LocalDateTime.now()
                        );
                        if (UserCredentialValidation.isValid(newUserCredentials)) {
                            userCredentialService.updateUserCredentials(newUserCredentials);
                        }
                    }
                }
            }
            loadEmployees();
            btnAdd.setDisable(true);
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            lblEmployeeID.setText("E000");
            disableTextField();
            clearFields();
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Employee Updated Successfully!",
                    "/img/icon/success-48.png"
            );
        } else {
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while updating " + employee.getName(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Delete Employee
    private void deleteEmployee() {
        if (service.deleteEmployee(lblEmployeeID.getText())) {
            loadEmployees();
            btnAdd.setDisable(true);
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
            lblEmployeeID.setText("E000");
            disableTextField();
            clearFields();
            CustomAlert.showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Employee Deleted Successfully!",
                    "/img/icon/success-48.png"
            );
        } else {
            CustomAlert.showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error Occurred while deleting " + txtName.getText(),
                    "/img/icon/error-48.png"
            );
        }
    }

    //Load all Employees
    private void loadEmployees() {
        employeeList = service.getAllEmployees();
        loadEmployeesToGridPane(employeeList);

        //Set Titles
        ObservableList<String> titles = FXCollections.observableArrayList();
        titles.add("Mr");
        titles.add("Miss");
        titles.add("Mrs");
        cmbTitle.setItems(titles);

        //Set Roles
        ObservableList<String> roles = FXCollections.observableArrayList();
        roles.add("ADMIN");
        roles.add("EMPLOYEE");
        roles.add("DEMO");
        cmbRole.setItems(roles);
    }

    //Load Employees to Grid Pane
    private void loadEmployeesToGridPane(List<Employee> employees) {
        employeeGrid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (Employee employee : employees) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/view/admin/employee/employee_card.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                EmployeeCardController employeeCardController = fxmlLoader.getController();
                employeeCardController.setData(employee);

                if (column == 4) {
                    column = 0;
                    row++;
                }
                employeeGrid.add(anchorPane, column++, row);

                //Grid width
                employeeGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                employeeGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                employeeGrid.setMaxWidth(Region.USE_PREF_SIZE);

                //Grid height
                employeeGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                employeeGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                employeeGrid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Load Employee Data to Right Panel
    public void loadEmployeeDetails(Employee employee) {
        lblEmployeeID.setText(employee.getId());
        cmbTitle.setValue(employee.getTitle());
        txtName.setText(employee.getName());
        txtPosition.setText(employee.getPosition());
        txtAddress.setText(employee.getAddress());
        txtPhoneNumber.setText(employee.getPhoneNumber());
        txtEmail.setText(employee.getEmail());

        if (employee.getLoginAccess()) {
            UserCredentials userCredentials = userCredentialService.getUserCredentialsByEmployeeId(employee.getId());
            System.out.println(userCredentials);
            if (userCredentials != null) {
                cmbRole.setValue(userCredentials.getRole());
            } else {
                cmbRole.setValue("Select Role");
            }
        }

        loadEmployeeImage(employee);
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnAdd.setDisable(true);
        disableTextField();

    }

    //Load Employee Image
    private void loadEmployeeImage(Employee employee) {
        String imgSrc = "";
        if (employee.getLoginAccess()) {
            imgSrc = employee.getTitle().equals("Mr") ?
                    "/img/icon/employee/star-men.png" :
                    "/img/icon/employee/star-women.png";
        } else {
            imgSrc = employee.getTitle().equals("Mr") ?
                    "/img/icon/employee/man.png" :
                    "/img/icon/employee/women.png";
        }

        Image image = new Image(getClass().getResourceAsStream(imgSrc));
        employeeImage.setImage(image);
    }

    private void loadEmployeeImageByTitle(String title, Boolean loginAccess) {
        String imgSrc = "";
        if (loginAccess) {
            imgSrc = title.equals("Mr") ?
                    "/img/icon/employee/star-men.png" :
                    "/img/icon/employee/star-women.png";
        } else {
            imgSrc = title.equals("Mr") ?
                    "/img/icon/employee/man.png" :
                    "/img/icon/employee/women.png";
        }

        Image image = new Image(getClass().getResourceAsStream(imgSrc));
        employeeImage.setImage(image);
    }

    // Filter employees
    private void searchEmployee(String searchQuery) {
        List<Employee> filteredEmployees = new ArrayList<>();

        if (searchQuery == null || searchQuery.isEmpty()) {
            loadEmployees();
        } else {
            for (Employee supplier : employeeList) {
                if (supplier.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    filteredEmployees.add(supplier);
                }
            }
            loadEmployeesToGridPane(filteredEmployees);
        }
    }

    //Generate Next ID
    private String generateNextID() {
        String base = "E";
        int id = Integer.parseInt(service.getLastEmployeeId());

        if (id < 10) {
            base += "00";
        } else if (id < 100) {
            base += "0";
        }
        return (base + (id + 1));
    }

    //Element behaviours
    private void enableTextField() {
        txtName.setEditable(true);
        txtPosition.setEditable(true);
        txtAddress.setEditable(true);
        txtPhoneNumber.setEditable(true);
        txtEmail.setEditable(true);
    }

    private void disableTextField() {
        txtName.setEditable(false);
        txtPosition.setEditable(false);
        txtAddress.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtEmail.setEditable(false);
    }

    private void clearFields() {
        cmbTitle.setValue(null);
        txtName.setText(null);
        txtPosition.setText(null);
        txtAddress.setText(null);
        txtPhoneNumber.setText(null);
        txtEmail.setText(null);
        cmbRole.setValue(null);
        txtPassword.setText(null);
        checkAccount.setSelected(false);
        txtPassword.setDisable(true);
        cmbRole.setDisable(true);
    }

}
