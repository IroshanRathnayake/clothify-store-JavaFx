package com.clothify.controller.admin.dashboard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDashboardFormController implements Initializable {

    @FXML
    private AreaChart<String, Number> monthlySalesChart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Sales (LKR)");

        monthlySalesChart = new AreaChart<>(xAxis, yAxis);
        monthlySalesChart.setTitle("Monthly Sales Performance");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("2024");

        series.getData().add(new XYChart.Data<>("January", 45000));
        series.getData().add(new XYChart.Data<>("February", 38000));
        series.getData().add(new XYChart.Data<>("March", 52000));
        series.getData().add(new XYChart.Data<>("April", 61000));
        series.getData().add(new XYChart.Data<>("May", 70000));
        series.getData().add(new XYChart.Data<>("June", 63000));
        series.getData().add(new XYChart.Data<>("July", 72000));
        series.getData().add(new XYChart.Data<>("August", 68000));
        series.getData().add(new XYChart.Data<>("September", 54000));
        series.getData().add(new XYChart.Data<>("October", 75000));
        series.getData().add(new XYChart.Data<>("November", 82000));
        series.getData().add(new XYChart.Data<>("December", 90000));

        monthlySalesChart.getData().add(series);

        monthlySalesChart.setLegendVisible(true);
        monthlySalesChart.setAnimated(true);
        monthlySalesChart.setStyle("-fx-stroke-width: 1.5;");

    }
}
