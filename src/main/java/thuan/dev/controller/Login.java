package thuan.dev.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import thuan.dev.models.employee.EmployeeImp;

import java.io.IOException;

public class Login extends Application {

    private double x = 0;
    private double y = 0;

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(infoMessage);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Login.class.getResource("Login.fxml"));
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
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    private Button cancelButton;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private void cancelOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginOnAction(ActionEvent event) {
        if (email.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi!", "Vui lòng nhập lại email.");
            return;
        }

        if (password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi!", "Vui lòng nhập lại mật khẩu.");
            return;
        }

        EmployeeImp emp = new EmployeeImp();
        boolean isAuthenticated = emp.checkLogin(email.getText(), password.getText());

        if (isAuthenticated) {
            infoBox("Đăng nhập thành công", null, "Thành công");
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
            try {
                Admin admin = new Admin();
                Stage stage = new Stage();
                admin.start(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            infoBox("Đăng nhập thất bại", null, "Thất bại");
        }
    }
}
