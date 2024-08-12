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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import thuan.dev.models.category.Category;
import thuan.dev.models.category.CategoryDAO;
import thuan.dev.models.category.CategoryImple;

import java.io.IOException;
import java.util.List;

public class CategoryController extends Application {
    private double x = 0;
    private double y = 0;

    @FXML
    private TextField categoryName;

    @FXML
    private TableView<Category> table_category;

    @FXML
    private TableColumn<Category, Integer> table_categoryID;

    @FXML
    private TableColumn<Category, String> table_categoryName;

    private ObservableList<Category> categoriesList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/Category.fxml"));
        Parent root = fxmlLoader.load();
        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Loại sản phẩm");
        primaryStage.show();
    }

    @FXML
    private void initialize() {
        showCategory();
    }

    private void showCategory() {

        CategoryDAO categoryDAO = new CategoryImple();
        List<Category> categoryList = categoryDAO.getAllCategory();
        categoriesList = FXCollections.observableArrayList(categoryList);

        table_categoryID.setCellValueFactory(new PropertyValueFactory<>("id"));
        table_categoryName.setCellValueFactory(new PropertyValueFactory<>("name"));

        table_category.setItems(categoriesList);

    }
}
