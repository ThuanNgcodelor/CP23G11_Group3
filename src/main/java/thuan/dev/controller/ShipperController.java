package thuan.dev.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import thuan.dev.models.shipper.ShipperDAO;
import thuan.dev.models.shipper.ShipperImple;
import thuan.dev.models.shipper.Shippers;

import java.io.IOException;
import java.util.List;

public class ShipperController extends Application {

    private double x = 0;
    private double y = 0;

    @FXML
    private TableColumn<Shippers, Integer> id_shipper_table;

    @FXML
    private TableColumn<Shippers, String> name_shipper_table;

    @FXML
    private TableColumn<Shippers, Integer> phone_shipper_table;

    @FXML
    private TableColumn<Shippers, Integer> cccd_shipper_table;

    @FXML
    private TableColumn<Shippers, String> email_shipper_table;

    @FXML
    private TextField shipper_cccd;


    @FXML
    private TextField shipper_email;

    @FXML
    private TextField shipper_name;

    @FXML
    private TextField shipper_phone;

    @FXML
    private TableView<Shippers> table_shipper;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/Shipper.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Trang chủ");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        id_shipper_table.setCellValueFactory(new PropertyValueFactory<>("shipperID"));
        name_shipper_table.setCellValueFactory(new PropertyValueFactory<>("shipperName"));
        phone_shipper_table.setCellValueFactory(new PropertyValueFactory<>("shipperPhone"));
        cccd_shipper_table.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        email_shipper_table.setCellValueFactory(new PropertyValueFactory<>("email"));

        showTableShipper();
    }

    @FXML
    private void showTableShipper() {
        ShipperDAO shipperDAO = new ShipperImple();
        List<Shippers> shippersList = shipperDAO.showAllShipper(new Shippers());
        ObservableList<Shippers> shippersObservableList = FXCollections.observableList(shippersList);
        table_shipper.setItems(shippersObservableList);
    }

    @FXML
    private void addShipper() {
        Shippers newShipper = new Shippers();
        newShipper.setShipperName(shipper_name.getText());
        newShipper.setShipperPhone(Integer.parseInt(shipper_phone.getText()));
        newShipper.setCccd(Integer.parseInt(shipper_cccd.getText()));
        newShipper.setEmail(shipper_email.getText());

        ShipperDAO shipperDAO = new ShipperImple();
        if (shipperDAO.addShipper(newShipper)) {
            showTableShipper();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Shipper added successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add shipper.");
        }
    }

    @FXML
    private void updateShipper() {
        Shippers selectedShipper = table_shipper.getSelectionModel().getSelectedItem();
        if (selectedShipper != null) {
            selectedShipper.setShipperName(shipper_name.getText());
            selectedShipper.setShipperPhone(Integer.parseInt(shipper_phone.getText()));
            selectedShipper.setCccd(Integer.parseInt(shipper_cccd.getText()));
            selectedShipper.setEmail(shipper_email.getText());

            ShipperDAO shipperDAO = new ShipperImple();
            if (shipperDAO.updateShipper(selectedShipper)) {
                showTableShipper();
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Shipper updated successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update shipper.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No shipper selected for update.");
        }
    }

    @FXML
    private void deleteShipper() {
        Shippers selectedShipper = table_shipper.getSelectionModel().getSelectedItem();
        if (selectedShipper != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this shipper?");
            alert.setContentText("This action cannot be undone.");

            ButtonType confirmButton = new ButtonType("Delete");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(confirmButton, cancelButton);

            alert.showAndWait().ifPresent(response -> {
                if (response == confirmButton) {
                    ShipperDAO shipperDAO = new ShipperImple();
                    if (shipperDAO.deleteShipper(selectedShipper)) {
                        showTableShipper();
                        clearForm();
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Shipper deleted successfully!");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete shipper.");
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "No shipper selected for deletion.");
        }
    }

    private void clearForm() {
        shipper_name.clear();
        shipper_phone.clear();
        shipper_cccd.clear();
        shipper_email.clear();
    }

    @FXML
    private void closeManageShipper(ActionEvent event){
        Stage stage = (Stage) table_shipper.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buttonClear(ActionEvent event){
        clearForm();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
