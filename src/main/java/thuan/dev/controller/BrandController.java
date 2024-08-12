package thuan.dev.controller;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent; // Correct import for JavaFX MouseEvent
import thuan.dev.models.brand.BrandDAO;
import thuan.dev.models.brand.BrandImple;
import thuan.dev.models.brand.Brands;

import java.io.IOException;
import java.util.List;

public class BrandController extends Application {
    private double x = 0;
    private double y = 0;

    @FXML
    private TextField brandName;

    @FXML
    private TableColumn<Brands, Integer> table_brandID;

    @FXML
    private TableColumn<Brands, String> table_brandName;

    @FXML
    private TableView<Brands> table_brand;

    private ObservableList<Brands> brandsObservableList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/Brand.fxml"));
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
        stage.setScene(scene);
        stage.setTitle("Nhãn hàng");
        stage.show();
    }

    @FXML
    private void initialize(){
        showBrand();
    }

    private void showBrand(){
        BrandDAO brandDAO = new BrandImple();
        List<Brands> brandsList = brandDAO.getAllBrand();

        brandsObservableList = FXCollections.observableArrayList(brandsList);

        table_brandID.setCellValueFactory(new PropertyValueFactory<>("brandID"));
        table_brandName.setCellValueFactory(new PropertyValueFactory<>("brandName"));

        table_brand.setItems(brandsObservableList);
    }
}
