package thuan.dev.controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import thuan.dev.config.MyConnection;
import thuan.dev.models.bill.BillDAO;
import thuan.dev.models.bill.BillImple;
import thuan.dev.models.bill.Bills;
import thuan.dev.models.employee.EmployeeDAO;
import thuan.dev.models.employee.EmployeeImp;
import thuan.dev.models.employee.Employees;
import thuan.dev.models.orders.Order;
import thuan.dev.models.orders.OrderDAO;
import thuan.dev.models.orders.OrderImplements;
import thuan.dev.models.products.Product;
import thuan.dev.models.products.ProductDAO;
import thuan.dev.models.products.ProductImple;
import thuan.dev.models.salary.Salary;
import thuan.dev.models.salary.SalaryDAO;
import thuan.dev.models.salary.SalaryImple;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class UserController extends AdminController {

    @FXML
    private Button show;

    @FXML
    private TextField password_show;

    @FXML
    private Button showDetailsButton;

    @FXML
    private Button hideDetailsButton;

    @FXML
    private TableView<Order> orderDetailsTable;

    @FXML
    private TableColumn<Bills, Integer> bill_customers;

    @FXML
    private TableColumn<Bills, Date> bill_date;

    @FXML
    private TableColumn<Bills, Integer> bill_id;

    @FXML
    private TableColumn<Bills, BigDecimal> bill_total_price;

    @FXML
    private TableView<Order> card_display_table;

    @FXML
    private Label card_total;

    @FXML
    private Label cart_quantity;

    @FXML
    private AnchorPane home;

    @FXML
    private Button homeButton;

    @FXML
    private Button billButton;

    @FXML
    private AnchorPane bills;

    @FXML
    private Button logout;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private AnchorPane order;

    @FXML
    private Button orderButton;

    @FXML
    private TableColumn<Product, String> showcard_order;

    @FXML
    private TableColumn<Product, Double> showcard_price;

    @FXML
    private TableColumn<Product, Integer> showcard_quantity;

    @FXML
    private TableView<Bills> table_orders;

    @FXML
    private Label total_bill;

    @FXML
    private Label total_price;

    @FXML
    private Label username;

    ObservableList<Product> productList;

    private ObservableList<Order> ordersList;

    ObservableList<Bills> billsList;

    @FXML
    private TableColumn<Order, String> bill_details_name;

    @FXML
    private TableColumn<Order, Double> bill_details_price;

    @FXML
    private TableColumn<Order, Integer> bill_details_quantity;

    @FXML
    private TextField table_search_product;

    private FilteredList<Product> filteredList;

    @FXML
    private TextField sale_code;

    @FXML
    private TextField cccd;

    @FXML
    private TextField email;

    @FXML
    private TextField fullname;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private DatePicker birthdays;

    @FXML
    private Label days;

    @FXML
    private Label salaryStaff;


    @FXML
    private void checkIn(ActionEvent event){
        SalaryDAO salaryDAO = new SalaryImple();
        salaryDAO.getSalary();
        showAlert(Alert.AlertType.INFORMATION,"Thanks","Check in successfully");
    }

    @FXML
    private void checkOut(ActionEvent event){
        SalaryDAO salaryDAO = new SalaryImple();
        salaryDAO.timeEnd();
        showAlert(Alert.AlertType.INFORMATION,"Thanks","Check out successfully");
    }

    private void displayProfile() {
        EmployeeDAO employeeDAO = new EmployeeImp();
        List<Employees> employees = employeeDAO.selectProfile();

        if (!employees.isEmpty()) {
            Employees emp = employees.get(0);

            phone.setText(emp.getPhone());
            cccd.setText(emp.getCccd());
            email.setText(emp.getEmail());
            password.setText(emp.getPassword());
            fullname.setText(emp.getFullname());
            password_show.setText(emp.getPassword());
            Bindings.bindBidirectional(password.textProperty(), password_show.textProperty());


            if (emp.getBirth() != null) {
                birthdays.setValue(LocalDate.parse(emp.getBirth().toString()));
            }
            SalaryDAO salaryDAO = new SalaryImple();
            Salary salary = new Salary();
            salaryDAO.countSalary(salary);
            days.setText(String.valueOf(salary.getTotalDays()));
            //HIển thị số ngày

            long house = salary.getTotalHours();
            long minutes = salary.getTotalMinutes();

            int oneHouse = 30000;
            //tạo ra 1 giờ
            double oneMinutes = oneHouse / 60.0;

            double totalSalary = (house * oneHouse) + (minutes * oneMinutes);
            //Tính số tiền

            salaryStaff.setText(String.valueOf(totalSalary + " VNĐ "));

        }
    }



    @FXML
    private void updateProfile() {
        String phoneF = phone.getText();
        String cccdF = cccd.getText();
        LocalDate localDate = birthdays.getValue();
        String emailF = email.getText();
        String passwordF = password.getText();
        String fullnameF = fullname.getText();

        if (phoneF.isEmpty() || cccdF.isEmpty() || localDate == null || emailF.isEmpty() || passwordF.isEmpty() || fullnameF.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter complete data");
            return;
        }
        if (!phoneF.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Phone number must be numeric");
            return;
        }
        if (!emailF.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format");
            return;
        }
        if (!cccdF.matches("\\d{10}")) {
            showAlert(Alert.AlertType.ERROR, "Error", "CCCD must be 10 digits");
            return;
        }
        if (!localDate.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Date of birth must be before the current date");
            return;
        }

        Date birthF = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Update");
        confirmationAlert.setHeaderText("Do you want to update this employee?");
        confirmationAlert.setContentText("Employee: " + fullnameF);

        ButtonType buttonYes = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {

            Employees updatedEmployee = new Employees();
            updatedEmployee.setPhone(phoneF);
            updatedEmployee.setBirth(birthF);
            updatedEmployee.setCccd(cccdF);
            updatedEmployee.setEmail(emailF);
            updatedEmployee.setPassword(passwordF);
            updatedEmployee.setFullname(fullnameF);

            EmployeeDAO employeeDAO = new EmployeeImp();
            boolean success = employeeDAO.updateProfile(updatedEmployee);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Employee updated successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee.");
            }
        }
    }


    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.close();
            Stage loginStage = new Stage();
            Login login = new Login();
            login.start(loginStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleDeleteOrder(ActionEvent event) {
        Order selectedOrder = card_display_table.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an order to delete!");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this order?");
        confirmationAlert.setContentText("Order ID: " + selectedOrder.getOrderID());

        ButtonType buttonYes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
            int quantity = selectedOrder.getQuantity();
            int productID = selectedOrder.getProductID();

            ProductDAO productDAO = new ProductImple();
            int stock = productDAO.getProductStock(productID);
            int newStock = stock + quantity;
            productDAO.updateProductStock(productID, newStock);

            OrderDAO orderDAO = new OrderImplements();
            boolean success = orderDAO.removeOrder(selectedOrder);
            if (success) {
                card_display_table.getItems().remove(selectedOrder);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully!");
                showDisplayCard();
                totalPrice();

                //tạo thông báo cho lieu thu 2
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the order!");
            }
        }
        menuDisplayCard();
    }

//--------------------------------------------BILLS--------------------------------------------------------------------------------------------
    public void showListBill() {
        BillDAO billDAO = new BillImple();
        List<Bills> bills = billDAO.getAllBillDateNow();
        billsList = FXCollections.observableArrayList(bills);

        bill_id.setCellValueFactory(new PropertyValueFactory<>("billID"));
        bill_total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        bill_customers.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        bill_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        table_orders.setItems(billsList);
    }

    @FXML
    private void handleExportPDF(ActionEvent event) {
        try {
            Connection conn = MyConnection.getConnection();

            String maxBillIdQuery = "SELECT MAX(billID) AS maxBillID FROM bill";
            //gọi bill max
            PreparedStatement maxBillIdStmt = conn.prepareStatement(maxBillIdQuery);
            ResultSet maxBillIdResult = maxBillIdStmt.executeQuery();

            int billID = -1;
            // nho hon 0 -> bao loi
            if (maxBillIdResult.next()) {
                billID = maxBillIdResult.getInt("maxBillID");
            }

            if (billID != -1) {
                String fileName = "bill_" + billID + ".txt";
                File outputFile = new File("C:\\Users\\kkk\\Desktop\\Group_3\\src\\main\\resources\\thuan\\dev\\images\\bills\\" + fileName);

                try (FileWriter writer = new FileWriter(outputFile)) {
                    writer.write("\n3k Chicken Shop\n");
                    writer.write("====================\n\n");

                    String query1 = "SELECT * FROM orders WHERE bills = ?";
                    PreparedStatement statement1 = conn.prepareStatement(query1);
                    statement1.setInt(1, billID);
                    ResultSet resultSet1 = statement1.executeQuery();

                    writer.write("Products:\n");

                    while (resultSet1.next()) {
                        String productName = resultSet1.getString("productName");
                        int quantity = resultSet1.getInt("quantity");

                        String productDetails = String.format("- %s: %d \n", productName, quantity);
                        writer.write(productDetails);
                    }

                    String query2 = "SELECT customerID, date, total, status FROM bill WHERE billID = ?";
                    PreparedStatement statement2 = conn.prepareStatement(query2);
                    statement2.setInt(1, billID);
                    ResultSet resultSet2 = statement2.executeQuery();

                    if (resultSet2.next()) {
                        int customerID = resultSet2.getInt("customerID");
                        Date date = resultSet2.getDate("date");
                        double total = resultSet2.getDouble("total");
                        int status = resultSet2.getInt("status");

                        String billDetails = String.format("\nCustomerID: %d\nDate: %s\nTotal: %.2f\nStatus: %d\n\n",
                                customerID, date, total, status);
                        writer.write(billDetails);
                    }

                    writer.write("====================\n");
                    writer.write("\nThank you\n");

                    resultSet1.close();
                    statement1.close();
                    resultSet2.close();
                    statement2.close();
                    conn.close();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Printed bills successfully");
                    alert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Export Failed");
                    alert.setHeaderText(null);
                    alert.setContentText("An error occurred while exporting data.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Bills Found");
                alert.setHeaderText(null);
                alert.setContentText("No bills were found in the database.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while retrieving the maximum bill ID.");
            alert.showAndWait();
        }
    }


//--------------------------------------------BILLS--------------------------------------------------------------------------------------------
//--------------------------------------------ORDER--------------------------------------------------------------------------------------------
//--------------------------------------------ORDER--------------------------------------------------------------------------------------------
    public void menuRestart() {
        card_display_table.getSelectionModel().getSelectedItem();
        card_total.setText("0.0");
        cart_quantity.setText("");
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void updateCart(ActionEvent event) {

        OrderDAO orderDAO = new OrderImplements();
        Order selectedOrder = card_display_table.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an order to update!");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Order");
        confirmationAlert.setHeaderText("Are you sure you want to update this order?");

        ButtonType buttonYes = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmationAlert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == buttonYes) {
                orderDAO.updateOrder(selectedOrder.getOrderID());

            Bills bills = new Bills();

            String totalPriceString = card_total.getText().replaceAll("[^\\d.]", "").trim();

            bills.setTotalPrice(Double.parseDouble(totalPriceString));
            bills.setCustomerID(Data.customerID);
            bills.setDate(new Date());

            BillDAO billDAO = new BillImple();
            boolean selectBill = billDAO.addBill(bills);
            if (selectBill) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Order Successfully!");
                showDisplayCard();
                menuRestart();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Order failed!");
            }
        }
    }
    //Button Đặt hàng tại đây

    @FXML
    private void increase(ActionEvent event) {
        updateQuantity(1);
    }

    @FXML
    private void reduce(ActionEvent event) {
        updateQuantity(-1);
    }

    private void updateQuantity(int ktr) {
        OrderDAO orderDAO = new OrderImplements();
        ProductDAO productDAO = new ProductImple();
        Order selectedOrder = card_display_table.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select an order to update!");
            return;
        }

        int selectedIndex = card_display_table.getSelectionModel().getSelectedIndex();
        int quantity = selectedOrder.getQuantity();
        int productID = selectedOrder.getProductID();
        double pricePerUnit = selectedOrder.getPrice();
        int stock = productDAO.getProductStock(productID);

        int newQuantity = quantity + ktr;
        if (newQuantity < 1) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Quantity cannot be less than 1!");
            return;
        }

        if (ktr > 0 && stock <= 0) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Not enough stock to increase quantity!");
            return;
        }

        double newPrice = pricePerUnit * newQuantity;

        // Update order quantity and price
        orderDAO.update(productID, newQuantity, (int) newPrice,selectedOrder.getOrderID());

        // Update stock
        int newStock = stock - ktr;
        productDAO.updateProductStock(productID, newStock);

        selectedOrder.setQuantity(newQuantity);
        selectedOrder.setPrice(newPrice);
        card_display_table.refresh();
        showDisplayCard();
        totalPrice();
        card_display_table.getSelectionModel().select(selectedIndex);
        menuDisplayCard();
    }
    //Giảm so lượng

    //Show sản phẩm ra menu
    public void showDisplayCard() {
        OrderDAO orderDAO = new OrderImplements();
        List<Order> orders = orderDAO.showDisplayCard();
        ordersList = FXCollections.observableArrayList(orders);

        showcard_order.setCellValueFactory(new PropertyValueFactory<>("productName"));
        showcard_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        showcard_price.setCellValueFactory(new PropertyValueFactory<>("total"));

        card_display_table.setItems(ordersList);
        totalPrice();
    }
    //Show ra giỏ hàng

    public void totalPrice() {
        double total = 0;
        int quantity = 0;

        if (ordersList != null) {
            for (Order order : ordersList) {
                if (order != null) {
                    total += order.getTotal();
                    quantity += order.getQuantity();
                }
            }
        }

        double finalTotal = total;
        int finalQuantity = quantity;

        Platform.runLater(() -> {
            card_total.setText(String.format("%,.0f", finalTotal));
            cart_quantity.setText(String.format("%d", finalQuantity));
        });
    }
    //Tính toán số tiền và hiển thị quantity


    @FXML
    private void initialize() {
        displayProfile();
        table_search_product.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return product.getProductName().toLowerCase().contains(lowerCaseFilter);
            });
            displayFilteredProducts();
        });

        showDetailsButton.setOnAction(event -> {
            Bills selectedBill = table_orders.getSelectionModel().getSelectedItem();
            if (selectedBill != null) {
                showOrderDetails(selectedBill);
            }
        });
        // bam nut de hien thi don hang
        hideDetailsButton.setOnAction(event -> hideOrderDetails());

        AppService.getInstance().setAdminController(this);
    }

    private void hideOrderDetails() {
        orderDetailsTable.getItems().clear();

    }
    private void showOrderDetails(Bills selectedBill) {
        BillDAO billDAO = new BillImple();
        List<Order> orderDetails = billDAO.showDetailsBill(selectedBill);
        ObservableList<Order> orderDetailsList = FXCollections.observableArrayList(orderDetails);

        bill_details_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        bill_details_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        bill_details_price.setCellValueFactory(new PropertyValueFactory<>("total"));

        orderDetailsTable.setItems(orderDetailsList);

    }
    // hiển thị chi tiet don hong

    public void menuDisplayCard() {
        ProductDAO productDAO = new ProductImple();
        List<Product> products = productDAO.show();

        if (products != null && !products.isEmpty()) {
            productList = FXCollections.observableArrayList(products);
            filteredList = new FilteredList<>(productList, p -> true);
        }
        displayFilteredProducts();
    }

    private void displayFilteredProducts() {
        int row = 0;
        int column = 0;
        menu_gridPane.getChildren().clear();

        for (Product product : filteredList) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/thuan/dev/controller/cardProduct.fxml"));
                AnchorPane pane = fxmlLoader.load();

                CartController controller = fxmlLoader.getController();
                controller.setData(product);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                GridPane.setMargin(pane, new Insets(15));
                menu_gridPane.add(pane, column++, row);

            } catch (IOException e) {
                System.out.println("Error: " + e);
            }
        }
    }

//--------------------------------------------ORDER--------------------------------------------------------------------------------------------
//--------------------------------------------ORDER--------------------------------------------------------------------------------------------
    public void displayUsername() {
        username.setText(String.valueOf(Data.fullname));
    }

    public void totalCustomers() {
        BillDAO billDAO = new BillImple();
        List<Bills> selectedBills = billDAO.getAllBills();
        int totalPrice = 0;
        int customerID = 0;

        if (selectedBills != null) {
            for (Bills bills : selectedBills) {
                if (bills != null) {
                    customerID++;
                    totalPrice += (int) bills.getTotalPrice();
                }
            }
        }
        double finalTotal = totalPrice;
        Platform.runLater(() -> total_price.setText(String.format("$%.2f", finalTotal)));
        total_bill.setText(String.valueOf(customerID));
    }

    @FXML
    private void buttonNews(ActionEvent event){
        try{
            ShowNews showNews = new ShowNews();
            Stage stage = new Stage();
            showNews.start(stage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void buttonBillOrders(ActionEvent event) {
        try {
            BillOrdersController billOrdersController = new BillOrdersController();
            Stage stage = new Stage();
            billOrdersController.start(stage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void passwordShow(ActionEvent event){
        if (password.isVisible()) {
            password_show.setText(password.getText());
            password_show.setVisible(true);
            password.setVisible(false);
        } else {
            password.setText(password_show.getText());
            password_show.setVisible(false);
            password.setVisible(true);
        }
    }


    @FXML
    private void switchForm(ActionEvent event) {
        if (event.getSource() == homeButton) {
            home.setVisible(true);
            order.setVisible(false);
            bills.setVisible(false);
            displayUsername();
            displayProfile();
        } else if (event.getSource() == orderButton) {
            home.setVisible(false);
            order.setVisible(true);
            bills.setVisible(false);
            menuDisplayCard();
            showDisplayCard();
            totalPrice();

        } else if (event.getSource() == billButton) {
            home.setVisible(false);
            order.setVisible(false);
            bills.setVisible(true);
            showListBill();
        }
    }
}
