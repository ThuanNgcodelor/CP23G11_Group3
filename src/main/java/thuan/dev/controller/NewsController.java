package thuan.dev.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import thuan.dev.models.news.News;
import thuan.dev.models.news.NewsDAO;
import thuan.dev.models.news.NewsDAOImpl;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class NewsController {
    @FXML
    private TextField newsName;

    @FXML
    private TextArea newsDetails;

    @FXML
    private ImageView newsImages;

    @FXML
    private TableColumn<News, Integer> table_newsID;

    @FXML
    private TableColumn<News, String> table_newsName;

    @FXML
    private TableColumn<News, String> table_newsDetails;

    @FXML
    private TableColumn<News, String> table_newsImages;

    @FXML
    private TableView<News> table_news;

    @FXML
    private TextField searchField;

    @FXML
    private Button close;

    private FilteredList<News> filteredList;
    private ObservableList<News> newsObservableList;


    @FXML
    private void initialize() {
        showNews();
        searchField.textProperty().addListener((obs, oldText, newText) -> search());
    }

    private void showNews() {
        NewsDAO newsDAO = new NewsDAOImpl();
        List<News> newsList = newsDAO.getAllNews();

        newsObservableList = FXCollections.observableArrayList(newsList);

        filteredList = new FilteredList<>(newsObservableList, b -> true);

        table_newsID.setCellValueFactory(new PropertyValueFactory<>("newsID"));
        table_newsName.setCellValueFactory(new PropertyValueFactory<>("newsName"));
        table_newsDetails.setCellValueFactory(new PropertyValueFactory<>("newsDetails"));
        table_newsImages.setCellValueFactory(new PropertyValueFactory<>("newsImages"));

        table_news.setItems(filteredList);
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn Hình Ảnh");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            newsImages.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    private void addNews() {
        String newsNameText = newsName.getText();
        String newsDetailsText = newsDetails.getText();
        String newsImagesText = newsImages.getImage() != null ? newsImages.getImage().getUrl() : "";

        if (newsNameText.isEmpty() || newsDetailsText.isEmpty() || newsImagesText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Add News");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to add this news?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewsDAO newsDAO = new NewsDAOImpl();
            News newNews = new News(0, newsNameText, newsDetailsText, newsImagesText);

            if (newsDAO.addNews(newNews)) {
                showNews();
                newsName.clear();
                newsDetails.clear();
                newsImages.setImage(null);
                showAlert(Alert.AlertType.INFORMATION, "Success", "News added successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add news!");
            }
        }
    }

    @FXML
    private void updateNews() {
        News selectedNews = table_news.getSelectionModel().getSelectedItem();
        if (selectedNews == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No news selected!");
            return;
        }

        String newsNameText = newsName.getText();
        String newsDetailsText = newsDetails.getText();
        String newsImagesText = newsImages.getImage() != null ? newsImages.getImage().getUrl() : "";

        if (newsNameText.isEmpty() || newsDetailsText.isEmpty() || newsImagesText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields must be filled!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update News");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to update this news?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewsDAO newsDAO = new NewsDAOImpl();
            selectedNews.setNewsName(newsNameText);
            selectedNews.setNewsDetails(newsDetailsText);
            selectedNews.setNewsImages(newsImagesText);

            newsDAO.updateNews(selectedNews);
            showNews();
            showAlert(Alert.AlertType.INFORMATION, "Success", "News updated successfully!");
        }
    }

    @FXML
    private void deleteNews() {
        News selectedNews = table_news.getSelectionModel().getSelectedItem();
        if (selectedNews == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No news selected!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete News");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this news?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewsDAO newsDAO = new NewsDAOImpl();
            if (newsDAO.deleteNews(selectedNews.getNewsID())) {
                showNews();
                showAlert(Alert.AlertType.INFORMATION, "Success", "News deleted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete news!");
            }
        }
    }

    private void search() {
        String keyword = searchField.getText().toLowerCase();
        filteredList.setPredicate(news -> {
            if (keyword.isEmpty()) {
                return true;
            }
            return news.getNewsName().toLowerCase().contains(keyword);
        });
    }

    @FXML
    private void closeNews(ActionEvent event) {
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
