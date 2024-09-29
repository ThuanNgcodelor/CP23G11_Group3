package thuan.dev.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import thuan.dev.models.historysalary.HistoryDAO;
import thuan.dev.models.historysalary.HistoryImp;
import thuan.dev.models.historysalary.Historys;
import thuan.dev.models.logintime.LoginTImeImple;
import thuan.dev.models.logintime.LoginTime;
import thuan.dev.models.logintime.LoginTimeDAO;
import thuan.dev.models.salary.Salary;

import java.util.Date;
import java.util.List;

public class History {
    @FXML
    private TableColumn<LoginTime, Integer> hours;

    @FXML
    private TableColumn<LoginTime, Integer> minutes;

    @FXML
    private TableColumn<LoginTime, Integer> status;

    @FXML
    private TableView<LoginTime> salaryTable;

    @FXML
    private TableColumn<Salary, Date> date;

    @FXML
    private TableColumn<Historys, Integer> salary;

    @FXML
    private TableView<Historys> historySalary;

    @FXML
    private TableColumn<Historys, Date> historydate;



    public void showLoginTime(int employeeID) {
        LoginTimeDAO loginTimeDAO = new LoginTImeImple();
        List<LoginTime> loginTimeList = loginTimeDAO.showHistory(employeeID);

        hours.setCellValueFactory(new PropertyValueFactory<>("hours"));
        minutes.setCellValueFactory(new PropertyValueFactory<>("minutes"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setCellFactory(column -> new TableCell<LoginTime, Integer>() {
            @Override
            protected void updateItem(Integer status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                } else {
                    setText(status == 0 ? "Unpaid" : "Paid");
                }
            }
        });

        salaryTable.setItems(FXCollections.observableArrayList(loginTimeList));
    }

    public void showHistorySalary(int employeeID) {
        HistoryDAO historyDAO = new HistoryImp();
        List<Historys> his = historyDAO.showHistory(employeeID);

        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        historydate.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        historySalary.setItems(FXCollections.observableArrayList(his));

    }

}

