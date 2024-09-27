package thuan.dev.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import thuan.dev.models.logintime.LoginTImeImple;
import thuan.dev.models.logintime.LoginTime;
import thuan.dev.models.logintime.LoginTimeDAO;
import thuan.dev.models.salary.Salary;
import thuan.dev.models.salary.SalaryDAO;
import thuan.dev.models.salary.SalaryImple;

import java.net.URL;
import java.text.NumberFormat;
import java.util.*;

public class SalaryProfile implements Initializable {
    @FXML
    private Label name;

    @FXML
    private Label totaldays;

    @FXML
    private Label totalhouse;

    @FXML
    private Label totalminues;

    @FXML
    private Label totalsalary;


    @FXML
    private TableColumn<Salary, Date> date;

    @FXML
    private Label end;

    @FXML
    private Label start;

    @FXML
    private TableColumn<LoginTime, Integer> hours;

    @FXML
    private TableColumn<LoginTime, Integer> minutes;

    @FXML
    private TableView<LoginTime> salaryTable;
    private int employeeID;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void showSalaryForEmployee(int employeeID, String employeeName) {
        this.employeeID = employeeID;
        //Gán dữ liệu cho employeeID
        SalaryDAO salaryDAO = new SalaryImple();
        LoginTimeDAO loginTimeDAO = new LoginTImeImple();
        Salary salary = new Salary();
        salary.setCustomerID(employeeID);
        // lấy customerID của cái kia gán vào

        salaryDAO.countSalary2(salary);
        // gọi lại hàm tính lương thứ 2 của admin

        Date[] minMaxDates = loginTimeDAO.getMinMaxDates();
        if (minMaxDates[0] != null) {
            start.setText(minMaxDates[0].toString());
        }
        if (minMaxDates[1] != null) {
            end.setText(minMaxDates[1].toString());
        }

        name.setText(employeeName);
        totalhouse.setText(String.valueOf(salary.getTotalHours() + "h "));
        totaldays.setText(String.valueOf(salary.getTotalDays() + " day "));
        totalminues.setText(String.valueOf(salary.getTotalMinutes()+ "' "));

        long totalSalary = calculateTotalSalary(salary.getTotalHours(), salary.getTotalMinutes());
        totalsalary.setText(formatCurrency(totalSalary));
        showLoginTime(employeeID);
        //Gán vào showLogin để hiển thị ra theo employeeID
    }

    private long calculateTotalSalary(long hours, long minutes) {
        int oneHour = 30000;
        double oneMinute = oneHour / 60.0;
        double totalSalary = (hours * oneHour) + (minutes * oneMinute);
        return (long) totalSalary;
    }

    //Lấy dữ liệu từ bên trên xuống để show ra
    private void showLoginTime(int employeeID) {
        LoginTimeDAO loginTimeDAO = new LoginTImeImple();
        List<LoginTime> loginTimeList = loginTimeDAO.showAll(employeeID);

        // Thiết lập các cột trong bảng
        hours.setCellValueFactory(new PropertyValueFactory<>("hours"));
        minutes.setCellValueFactory(new PropertyValueFactory<>("minutes"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        salaryTable.setItems(FXCollections.observableArrayList(loginTimeList));
    }

    // Chuển qua việt nam đồng
    private String formatCurrency(long amount) {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(amount);
    }

    @FXML
    public void handleUpdateStatus(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Update");
        alert.setHeaderText("Are you sure you want to update the login time status?");
        alert.setContentText("Please choose your action.");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            LoginTimeDAO loginTimeDAO = new LoginTImeImple();
            loginTimeDAO.updateLoginTimeStatus(employeeID);

            showLoginTime(employeeID);
            System.out.println("Updated login time status for employee ID: " + employeeID);
        } else {
            System.out.println("Update cancelled for employee ID: " + employeeID);
        }
    }
}
