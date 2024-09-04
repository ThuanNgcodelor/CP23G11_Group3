package thuan.dev.controller.dat;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        List<Product> products = dbHelper.getProducts();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int column = 0;
        int row = 0;

        for (Product product : products) {
            ProductCell productCell = new ProductCell();
            productCell.updateItem(product, false);

            gridPane.add(productCell.getGraphic(), column, row);

            column++;
            if (column == 5) {
                column = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);

        Button placeOrderButton = new Button("Đặt món");
        placeOrderButton.setOnAction(event -> {
            // Xử lý sự kiện khi đặt món
        });

        BorderPane root = new BorderPane();
        root.setTop(placeOrderButton);
        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 700, 700);
        primaryStage.setTitle("Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
