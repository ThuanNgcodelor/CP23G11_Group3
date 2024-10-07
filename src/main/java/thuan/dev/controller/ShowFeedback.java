package thuan.dev.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ShowFeedback  {

    private Connection conn = MyConnection.getConnection();

    @FXML
    private TableView<Feedback> table_news;

    @FXML
    private TableColumn<Feedback, Integer> table_newsID;

    @FXML
    private TableColumn<Feedback, String> table_newsName;

    @FXML
    private TableColumn<Feedback, String> table_newsDetails;

    @FXML
    private TextField searchField;

    private ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();
    private ObservableList<Feedback> filteredList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Setup table columns
        table_newsID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        table_newsName.setCellValueFactory(new PropertyValueFactory<>("feedback"));
        table_newsDetails.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Load feedback data from database
        loadFeedbackData();

        // Setup search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterFeedback(newValue);
        });
    }

    private void loadFeedbackData() {
        String query = "SELECT TOP (1000) [ID], [feedback], [date] FROM [QuanLySV].[dbo].[feedback]";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("ID");
                String feedback = getFeedbackText(rs.getInt("feedback"));
                String date = rs.getString("date");

                Feedback feedbackItem = new Feedback(id, feedback, date);
                feedbackList.add(feedbackItem);
            }

            table_news.setItems(feedbackList);
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterFeedback(String searchText) {
        filteredList.clear();
        if (searchText.isEmpty()) {
            filteredList.addAll(feedbackList);
        } else {
            for (Feedback feedback : feedbackList) {
                if (feedback.getFeedback().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(feedback);
                }
            }
        }
        table_news.setItems(filteredList);
    }

    private String getFeedbackText(int feedback) {
        switch (feedback) {
            case 1:
                return "Excellent: Exceeded expectations.";
            case 2:
                return "Good: Satisfactory, would recommend.";
            case 3:
                return "Average: Okay, needs improvement.";
            case 4:
                return "Poor: Disappointing, below expectations.";
            case 5:
                return "Terrible: Unacceptable, would not recommend.";
            default:
                return "Unknown feedback";
        }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) table_news.getScene().getWindow();
        stage.close();
    }

    // Inner class to represent Feedback data
    public class Feedback {
        private int ID;
        private String feedback;
        private String date;

        public Feedback(int ID, String feedback, String date) {
            this.ID = ID;
            this.feedback = feedback;
            this.date = date;
        }

        public int getID() {
            return ID;
        }

        public String getFeedback() {
            return feedback;
        }

        public String getDate() {
            return date;
        }
    }
}
