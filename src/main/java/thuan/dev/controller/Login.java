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
import thuan.dev.models.salary.SalaryDAO;
import thuan.dev.models.salary.SalaryImple;

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
    private void loginOnAction(ActionEvent event) throws IOException {
        if (email.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi!", "Vui lòng nhập lại email.");
            return;
        }

        if (password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi!", "Vui lòng nhập lại mật khẩu.");
            return;
        }

        EmployeeImp emp = new EmployeeImp();
        int role = emp.checkLogin(email.getText(), password.getText());

        if (role == 0) {
            infoBox("Login Success", null, "Success");
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/Admin.fxml"));
            Parent root = fxmlLoader.load();
            Stage adminStage = new Stage();
            adminStage.setScene(new Scene(root));
            adminStage.setTitle("Admin");
            adminStage.show();
            AdminController adminController = fxmlLoader.getController();
            adminController.menu();

        } else if (role == 1) {
            infoBox("Login Success", null, "Success");
            Stage currentStage = (Stage) loginButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/User.fxml"));
            Parent root = fxmlLoader.load();
            Stage userStage = new Stage();
            userStage.setScene(new Scene(root));
            userStage.show();
        } else {
            infoBox("Login fails", null, "Fails");
        }
    }

}
