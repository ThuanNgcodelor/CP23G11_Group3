package thuan.dev.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.stage.Stage;
import thuan.dev.models.employee.EmployeeDAO;
import thuan.dev.models.employee.EmployeeImp;
import thuan.dev.models.employee.Employees;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SignUp {

    private double x = 0;
    private double y = 0;

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    private TextField fullname;

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
        String fullnameF = fullname.getText();

        if (phoneF.isEmpty() || cccdF.isEmpty() || localDate == null || emailF.isEmpty() || passwordF.isEmpty() || fullnameF.isEmpty()) {
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

        Employees emp = new Employees(phoneF, cccdF, birthF, emailF, passwordF,fullnameF);
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
