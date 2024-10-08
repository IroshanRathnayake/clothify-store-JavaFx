package com.clothify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Starter extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login_form.fxml"))));
        stage.getIcons().add(new Image("img/logo-round.png"));
        stage.setTitle("Login");
        stage.setResizable(false);
        stage.show();
    }
}