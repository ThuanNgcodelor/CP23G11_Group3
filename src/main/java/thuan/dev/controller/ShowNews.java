// show hình ảnh quảng cao , ở phần nhân vien set 1 nút nhỏ để mở phần này


package thuan.dev.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue; //hieu ung
import javafx.animation.Timeline;
import javafx.application.Application; // thoi gian chuyen anh
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ShowNews extends Application {

    private ImageView imageView1;
    private ImageView imageView2;
    private List<String> imageUrls = new ArrayList<>();
    private int currentIndex = 0;
    private Timeline sliderTimeline;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        imageView1 = new ImageView();
        imageView2 = new ImageView();
        imageView1.setFitWidth(800);
        imageView1.setFitHeight(600);
        imageView2.setFitWidth(800);
        imageView2.setFitHeight(600);
        imageView1.setPreserveRatio(true);
        imageView2.setPreserveRatio(true);
        root.getChildren().addAll(imageView1, imageView2);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Image Viewer");
        primaryStage.show();

        loadImagesFromDatabase();

        if (!imageUrls.isEmpty()) {
            showNextImage();
            startImageSlider();
        }
    }

    private void loadImagesFromDatabase() {
        String url = "jdbc:sqlserver://localhost;databaseName=QuanLySV;user=sa;password=1234;encrypt=false;";
        String query = "SELECT newsImages FROM news";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                imageUrls.add(rs.getString("newsImages"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startImageSlider() {
        sliderTimeline = new Timeline(new KeyFrame(Duration.seconds(6), event -> showNextImage()));
        sliderTimeline.setCycleCount(Timeline.INDEFINITE);
        sliderTimeline.play();
    }

    private void showNextImage() {
        if (!imageUrls.isEmpty()) {
            String currentImageUrl = imageUrls.get(currentIndex);
            String nextImageUrl = imageUrls.get((currentIndex + 1) % imageUrls.size());

            // Set image for imageView1
            imageView1.setImage(new Image(currentImageUrl, true));
            // Set image for imageView2 (next image)
            imageView2.setImage(new Image(nextImageUrl, true));

            // Reset positions
            imageView1.setTranslateX(0);
            imageView2.setTranslateX(800); // Start off-screen

            // Create transitions
            Timeline slideOut = new Timeline(
                    new KeyFrame(Duration.seconds(1), new KeyValue(imageView1.translateXProperty(), -800))
            );

            Timeline slideIn = new Timeline(
                    new KeyFrame(Duration.seconds(1), new KeyValue(imageView2.translateXProperty(), 0))
            );

            // Play transitions
            slideOut.play();
            slideIn.play();

            // Switch imageViews
            slideOut.setOnFinished(event -> {
                imageView1.setImage(new Image(nextImageUrl, true));
                imageView1.setTranslateX(0);
            });

            currentIndex = (currentIndex + 1) % imageUrls.size();

        }
    }
}
