package thuan.dev.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import thuan.dev.models.bill.BillDAO;
import thuan.dev.models.bill.BillImple;
import thuan.dev.models.bill.Bills;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class BillOrdersController {

    @FXML
    private TableColumn<Bills, Integer> bill_customers;

    @FXML
    private TableColumn<Bills, Date> bill_date;

    @FXML
    private TableColumn<Bills, Integer> bill_id;

    @FXML
    private TableColumn<Bills, Integer> bill_status;

    @FXML
    private TableColumn<Bills, BigDecimal> bill_total_price;

    @FXML
    private TableView<Bills> table_orders;

    private ObservableList<Bills> billsList;
    private Timeline timeline;


    @FXML
    void initialize() {
        showListBill();
        startAutoRefresh();
    }

    public void showListBill() {
        BillDAO billDAO = new BillImple();
        List<Bills> bills = billDAO.getAllBills2();
        billsList = FXCollections.observableArrayList(bills);

        bill_id.setCellValueFactory(new PropertyValueFactory<>("billID"));
        bill_total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        bill_customers.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        bill_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        bill_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        table_orders.setItems(billsList);
    }

    @FXML
    private void UpdateOrderBill(ActionEvent event) {
        Bills selectedBill = table_orders.getSelectionModel().getSelectedItem();
        BillImple billImple = new BillImple();

        if (selectedBill == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an order to update.");
            return;
        }
        Optional<ButtonType> result = showConfirmation("Confirmation", "Are you sure you want to update this order?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            billImple.updateOrder(selectedBill);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Order updated successfully.");
            showListBill();
        }
    }

    private void startAutoRefresh() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> showListBill()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private static Optional<ButtonType> showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private Button close;
    @FXML
    private void close(ActionEvent event){
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
