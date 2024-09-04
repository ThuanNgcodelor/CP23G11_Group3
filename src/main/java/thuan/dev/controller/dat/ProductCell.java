package thuan.dev.controller.dat;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class ProductCell extends ListCell<Product> {

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);

        if (empty || product == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Tạo ImageView cho hình ảnh sản phẩm
            ImageView imageView = new ImageView();
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);

            try {
                String imagePath = product.getImages();
                if (!imagePath.startsWith("http") && !imagePath.startsWith("https")) {
                    imagePath = "file:/" + imagePath;
                }
                Image image = new Image(imagePath);
                imageView.setImage(image);
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                imageView.setImage(new Image("file:/path/to/default/image.jpg"));
            }

            // Tạo Label cho tên sản phẩm và giá
            Label nameLabel = new Label(product.getProductName());
            Label priceLabel = new Label("Giá: " + product.getPrice() + " VND");

            // Nút "Thêm món"
            Button addButton = new Button("Thêm món");

            // Tạo VBox để chứa các thành phần của sản phẩm
            VBox vbox = new VBox(10, imageView, nameLabel, priceLabel, addButton);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color: #000000; -fx-border-radius: 10px; -fx-background-color: #e5dede;");
            vbox.setPadding(new javafx.geometry.Insets(10));

            setGraphic(vbox);
        }
    }
}
