package thuan.dev.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import thuan.dev.models.brand.Brands;

import java.io.IOException;

public class BrandController extends Application {

    @FXML
    private TextField brandName;

    @FXML
    private TableColumn<Brands, Integer> table_brandID;

    @FXML
    private TableColumn<Brands, String> table_brandName;

    @FXML
    private TableView<Brands> table_category;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/Brand.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Nhãn hàng");
        primaryStage.show();
    }



}
