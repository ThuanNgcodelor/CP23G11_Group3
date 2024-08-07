package thuan.dev.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import thuan.dev.models.employee.EmployeeDAO;
import thuan.dev.models.employee.EmployeeImp;
import thuan.dev.models.employee.Employees;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SignUp extends Application {

    private double x = 0;
    private double y = 0;

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUp.class.getResource("Signup.fxml"));
        Parent root = fxmlLoader.load();
        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root);
        stage.setTitle("Sign Up");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField phone;
    @FXML
    private TextField cccd;
    @FXML
    private DatePicker birthdays;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button signup;

    @FXML
    private void cancelOnAction(ActionEvent event) {
        Stage stage = (Stage) signup.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void signUpAction(ActionEvent event) {
        String phoneF = phone.getText();
        String cccdF = cccd.getText();
        LocalDate localDate = birthdays.getValue();
        String emailF = email.getText();
        String passwordF = password.getText();

        if (phoneF.isEmpty() || cccdF.isEmpty() || localDate == null || emailF.isEmpty() || passwordF.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng nhập đủ dữ liệu.");
            return;
        }
        if (!phoneF.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Số điện thoại phải là số.");
            return;
        }
        if (!emailF.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Email không hợp lệ.");
            return;
        }
        if (!cccdF.matches("\\d{10}")) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "CCCD phải là 10 số.");
            return;
        }
        if (!localDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Ngày tháng sinh phải là trước hiện tại.");
            return;
        }
        Date birthF = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Employees emp = new Employees(phoneF, cccdF, birthF, emailF, passwordF);
        EmployeeDAO employeeDAO = new EmployeeImp();

        boolean flag = employeeDAO.addEmployee(emp);
        if (flag) {
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Bạn đã đăng ký thành công!");
            Stage currentStage = (Stage) signup.getScene().getWindow();
            currentStage.close();
            try {
                Login login = new Login();
                Stage stage = new Stage();
                login.start(stage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Thất bại", "Đăng ký không thành công.");
        }
    }

}
