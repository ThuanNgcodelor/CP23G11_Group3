package thuan.dev.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import thuan.dev.models.logintime.LoginTImeImple;
import thuan.dev.models.logintime.LoginTime;
import thuan.dev.models.logintime.LoginTimeDAO;
import thuan.dev.models.salary.Salary;
import thuan.dev.models.salary.SalaryDAO;
import thuan.dev.models.salary.SalaryImple;

import java.io.IOException;
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
    private Label totalsalary;

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

    @FXML
    private TableColumn<Salary, Date> date;

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

        Date[] minMaxDates = loginTimeDAO.getMinMaxDates(employeeID);
        if (minMaxDates[0] != null) {
            start.setText(minMaxDates[0].toString());
        }
        if (minMaxDates[1] != null) {
            end.setText(minMaxDates[1].toString());
        }

        name.setText(employeeName);
        String totalHoursAndMinutes = salary.getTotalHours() + "h " + salary.getTotalMinutes() + "'";
        totalhouse.setText(totalHoursAndMinutes);
        totaldays.setText(String.valueOf(salary.getTotalDays() + " day "));

        long totalSalary = calculateTotalSalary(salary.getTotalHours(), salary.getTotalMinutes());
        totalsalary.setText(formatCurrency(totalSalary));
        showLoginTime(employeeID);
        //Gán vào showLogin để hiển thị ra theo employeeID
    }

    @FXML
    public void showHistory(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/history.fxml"));
        AnchorPane pane = fxmlLoader.load();

        History history = fxmlLoader.getController();
        history.showLoginTime(employeeID);
        history.showHistorySalary(employeeID);

        Stage  stage = new Stage();
        stage.setTitle("History");
        stage.setScene(new Scene(pane));
        stage.show();
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
            SalaryDAO salaryDAO = new SalaryImple();
            Salary salary = new Salary();
            salary.setCustomerID(employeeID);
            salaryDAO.countSalary2(salary);


            long totalHours = salary.getTotalHours();
            //Lấy lại số giờ
            long totalMinutes = salary.getTotalMinutes();
            //Lấy lại số '
            long totalSalary = calculateTotalSalary(totalHours, totalMinutes);
            //CAll function tính salary

            salaryDAO.RollPay((int) totalSalary, employeeID);
            //Save vào sơ sở dữ liệu

            LoginTimeDAO loginTimeDAO = new LoginTImeImple();
            loginTimeDAO.updateLoginTimeStatus(employeeID);
            //Dùng để update status khi tính lương xong

            showLoginTime(employeeID);
            //Show lại loginTime

            System.out.println("Updated login time status and rolled pay for employee ID: " + employeeID);
        } else {
            System.out.println("Update cancelled for employee ID: " + employeeID);
        }
    }
}
