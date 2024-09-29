package thuan.dev.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import thuan.dev.models.brand.BrandDAO;
import thuan.dev.models.brand.BrandImple;
import thuan.dev.models.brand.Brands;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class BrandController {



    @FXML
    private TextField brandName;

    @FXML
    private TableColumn<Brands, Integer> table_brandID;

    @FXML
    private TableColumn<Brands, String> table_brandName;

    @FXML
    private TableView<Brands> table_brand;

    @FXML
    private TextField searchField;

    @FXML
    private Button close;

    private FilteredList<Brands> filteredList;
    private ObservableList<Brands> brandsObservableList;



    @FXML
    private void initialize() {
        showBrand();
        table_brand.getSelectionModel().selectedItemProperty().addListener((observable,oldValue, newValue)->{
            if (newValue != null) {
                showInputTouch(newValue);
            }
        });
        searchField.textProperty().addListener((obs, oldText, newText) -> search());
    }

    private void showBrand() {
        BrandDAO brandDAO = new BrandImple();
        List<Brands> brandsList = brandDAO.getAllBrand();

        brandsObservableList = FXCollections.observableArrayList(brandsList);

        filteredList = new FilteredList<>(brandsObservableList, b -> true);

        table_brandID.setCellValueFactory(new PropertyValueFactory<>("brandID"));
        table_brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));

        table_brand.setItems(filteredList);
    }

    private void showInputTouch(Brands brand){
        brandName.setText(brand.getBrandName());
    }

    @FXML
    private void addBrand() {
        String brandNameText = brandName.getText();
        if (brandNameText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Brand name cannot be empty!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add Brand");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to add this brand?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            BrandDAO brandDAO = new BrandImple();
            Brands newBrand = new Brands();
            newBrand.setBrandName(brandNameText);

            if (brandDAO.addBrand(newBrand)) {
                showBrand();
                brandName.clear();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Brand added successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add brand!");
            }
        }
    }

    @FXML
    private void updateBrand() {
        Brands selectedBrand = table_brand.getSelectionModel().getSelectedItem();
        if (selectedBrand == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No brand selected!");
            return;
        }

        String brandNameText = brandName.getText();
        if (brandNameText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Brand name cannot be empty!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Brand");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to update this brand?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            BrandDAO brandDAO = new BrandImple();
            selectedBrand.setBrandName(brandNameText);

            brandDAO.updateBrand(selectedBrand);
            showBrand();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Brand updated successfully!");
        }
    }

    @FXML
    private void deleteBrand() {
        Brands selectedBrand = table_brand.getSelectionModel().getSelectedItem();
        if (selectedBrand == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No brand selected!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Brand");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this brand?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            BrandDAO brandDAO = new BrandImple();
            if (brandDAO.deleteBrand(selectedBrand)) {
                showBrand();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Brand deleted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete brand!");
            }
        }
    }

    private void search() {
        String keyword = searchField.getText().toLowerCase();
        filteredList.setPredicate(brand -> {
            if (keyword.isEmpty()) {
                return true;
            }
            return brand.getBrandName().toLowerCase().contains(keyword);
        });
    }

    @FXML
    private void closeBrand(ActionEvent event) {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
