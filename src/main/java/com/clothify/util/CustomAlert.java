package com.clothify.util;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CustomAlert{
    //Custom Alert
    public static void showAlert(Alert.AlertType alertType, String title, String content, String path) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.setHeaderText(null);
        alert.setResizable(false);

        //Set Alert Icon
        Image customIcon = new Image(path);
        ImageView imageView = new ImageView(customIcon);
        alert.getDialogPane().setGraphic(imageView);

        alert.showAndWait();
    }

    //Error Alert
    public static void errorAlert(String title, Exception error){
        System.out.println(error.getMessage());
        CustomAlert.showAlert(Alert.AlertType.ERROR,
                title,
                error.getMessage(),
                "img/icon/error-48.png");
    }
}
