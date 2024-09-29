package thuan.dev.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import thuan.dev.models.shipper.ShipperDAO;
import thuan.dev.models.shipper.ShipperImple;
import thuan.dev.models.shipper.Shippers;

import java.util.List;

public class ShipperController  {

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



    @FXML
    void initialize() {
        id_shipper_table.setCellValueFactory(new PropertyValueFactory<>("shipperID"));
        name_shipper_table.setCellValueFactory(new PropertyValueFactory<>("shipperName"));
        phone_shipper_table.setCellValueFactory(new PropertyValueFactory<>("shipperPhone"));
        cccd_shipper_table.setCellValueFactory(new PropertyValueFactory<>("cccd"));
        email_shipper_table.setCellValueFactory(new PropertyValueFactory<>("email"));

        showTableShipper();

        table_shipper.getSelectionModel().selectedItemProperty().addListener((observable,oldValue, newValue)->{
            if (newValue != null) {
                showInputTouch(newValue);
            }
        });
    }

    @FXML
    private void showTableShipper() {
        ShipperDAO shipperDAO = new ShipperImple();
        List<Shippers> shippersList = shipperDAO.showAllShipper(new Shippers());
        ObservableList<Shippers> shippersObservableList = FXCollections.observableList(shippersList);
        table_shipper.setItems(shippersObservableList);
    }

    private void showInputTouch(Shippers shippers) {
        shipper_name.setText(shippers.getShipperName());
        shipper_phone.setText(String.valueOf(shippers.getShipperPhone()));
        shipper_cccd.setText(String.valueOf(shippers.getCccd()));
        shipper_email.setText(shippers.getEmail());
    }


    @FXML
    private void addShipper() {
        if (validateForm()) {
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
    }

    @FXML
    private void updateShipper() {
        Shippers selectedShipper = table_shipper.getSelectionModel().getSelectedItem();
        if (selectedShipper != null && validateForm()) {
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

    // Validation method
    private boolean validateForm() {
        if (shipper_name.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Name is required.");
            return false;
        }

        if (!validatePhone(shipper_phone.getText())) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid phone number.");
            return false;
        }

        if (!validateCCCD(shipper_cccd.getText())) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid CCCD.");
            return false;
        }

        if (!validateEmail(shipper_email.getText())) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Invalid email.");
            return false;
        }

        return true;
    }

    // Validate phone number (e.g., 10-digit number)
    private boolean validatePhone(String phone) {
        return phone.matches("\\d{10}");
    }

    // Validate CCCD (e.g., 12-digit number)
    private boolean validateCCCD(String cccd) {
        return cccd.matches("\\d{12}");
    }

    // Validate email using regex
    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
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
