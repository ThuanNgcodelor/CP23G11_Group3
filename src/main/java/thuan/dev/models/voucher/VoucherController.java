package thuan.dev.models.voucher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Platform;

public class VoucherController {

    @FXML
    private TextField newsName;

    @FXML
    private TextArea newsDetails;

    @FXML
    private TableView<Voucher> table_news;

    @FXML
    private TableColumn<Voucher, Integer> table_newsID;

    @FXML
    private TableColumn<Voucher, String> table_newsName;

    @FXML
    private TableColumn<Voucher, String> table_newsDetails;

    @FXML
    private TextField searchField;

    @FXML
    private Button addVoucher;

    @FXML
    private Button deleteVoucher;

    @FXML
    private Button close;

    private Connection connection;
    private ObservableList<Voucher> voucherList = FXCollections.observableArrayList();
    private ObservableList<Voucher> filteredList = FXCollections.observableArrayList();

    // Phương thức tìm kiếm
    private void filterVoucher(String searchText) {
        filteredList.clear();
        if (searchText.isEmpty()) {
            filteredList.addAll(voucherList); // Nếu ô tìm kiếm trống, hiển thị toàn bộ danh sách
        } else {
            for (Voucher voucher : voucherList) {
                if (voucher.getVoucher().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(voucher); // Thêm vào danh sách nếu khớp từ khóa tìm kiếm
                }
            }
        }
        table_news.setItems(filteredList); // Cập nhật lại bảng với danh sách lọc
    }

    @FXML
    public void initialize() {
        // Connect to database
        connectToDatabase();

        // Initialize TableView
        table_newsID.setCellValueFactory(new PropertyValueFactory<>("id"));
        table_newsName.setCellValueFactory(new PropertyValueFactory<>("voucher"));
        table_newsDetails.setCellValueFactory(new PropertyValueFactory<>("discount"));

        // Load data from database
        loadDataFromDatabase();

        // Lắng nghe thay đổi trong ô tìm kiếm và lọc dữ liệu
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterVoucher(newValue));
    }

    @FXML
    private TextField hourInput;

    // Thêm phương thức để bắt đầu quá trình xóa dữ liệu sau khi nhập số giờ
    @FXML
    private void addHours() {
        String hourText = hourInput.getText();
        try {
            int hours = Integer.parseInt(hourText);
            // Tính toán thời gian để xóa
            long millis = hours * 3600000; // Chuyển đổi giờ sang mili giây
            new Thread(() -> {
                try {
                    Thread.sleep(millis);
                    // Gọi phương thức để xóa dữ liệu
                    Platform.runLater(this::deleteExpiredVoucher);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Xử lý ngoại lệ nếu không phải là số

        }
    }

    // Phương thức để xóa dữ liệu hết hạn
    private void deleteExpiredVoucher() {
        String query = "DELETE FROM [QuanLySV].[dbo].[voucher] WHERE expiration_time < ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
            loadDataFromDatabase(); // Tải lại dữ liệu
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



//
//    @FXML
//    public void initialize() {
//        // Connect to database
//        connectToDatabase();
//
//        // Initialize TableView
//        table_newsID.setCellValueFactory(new PropertyValueFactory<>("id"));
//        table_newsName.setCellValueFactory(new PropertyValueFactory<>("voucher"));
//        table_newsDetails.setCellValueFactory(new PropertyValueFactory<>("discount"));
//
//        // Load data from database
//        loadDataFromDatabase();
//    }

    private void connectToDatabase() {
        try {
            String url = "jdbc:sqlserver://localhost;databaseName=QuanLySV;;encrypt=false;user=sa;password=1234";
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDataFromDatabase() {
        String query = "SELECT TOP (1000) [ID], [voucher], [discount], [expiration_time] FROM [QuanLySV].[dbo].[voucher] WHERE expiration_time > ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis())); // Lấy thời gian hiện tại
            ResultSet resultSet = statement.executeQuery();

            voucherList.clear();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String voucher = resultSet.getString("voucher");
                String discount = resultSet.getString("discount");
                voucherList.add(new Voucher(id, voucher, discount)); // Giả sử bạn đã sửa Voucher class để chứa expiration_time nếu cần
            }
            table_news.setItems(voucherList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addVoucher() {
        String name = newsName.getText();
        String details = newsDetails.getText();
        String hourText = hourInput.getText(); // Lấy số giờ từ ô nhập
        String query = "INSERT INTO [QuanLySV].[dbo].[voucher] (voucher, discount, expiration_time) VALUES (?, ?, ?)";

        try {
            int hours = Integer.parseInt(hourText);
            long currentTimeMillis = System.currentTimeMillis();
            long expirationTimeMillis = currentTimeMillis + (hours * 3600000); // Tính thời gian hết hạn
            java.sql.Timestamp expirationTime = new java.sql.Timestamp(expirationTimeMillis);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, details);
                statement.setTimestamp(3, expirationTime); // Thêm thời gian hết hạn vào câu lệnh
                statement.executeUpdate();
                loadDataFromDatabase(); // Tải lại dữ liệu
            }
        } catch (SQLException | NumberFormatException e) {

            e.printStackTrace();
        }
    }


    @FXML
    private void deleteVoucher() {
        Voucher selectedVoucher = table_news.getSelectionModel().getSelectedItem();
        if (selectedVoucher != null) {
            String query = "DELETE FROM [QuanLySV].[dbo].[voucher] WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, selectedVoucher.getId());
                statement.executeUpdate();
                loadDataFromDatabase(); // Reload data to reflect the deletion
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void closeNews() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
