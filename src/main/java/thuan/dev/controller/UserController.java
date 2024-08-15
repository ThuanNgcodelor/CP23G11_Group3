package thuan.dev.controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import thuan.dev.models.bill.BillDAO;
import thuan.dev.models.bill.BillImple;
import thuan.dev.models.bill.Bills;
import thuan.dev.models.category.Category;
import thuan.dev.models.orders.Order;
import thuan.dev.models.orders.OrderDAO;
import thuan.dev.models.orders.OrderImplements;
import thuan.dev.models.products.Product;
import thuan.dev.models.products.ProductDAO;
import thuan.dev.models.products.ProductImple;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class UserController {

    @FXML
    private AnchorPane add;

    @FXML
    private TableColumn<?, ?> bill_customers;

    @FXML
    private TableColumn<?, ?> bill_date;

    @FXML
    private TableColumn<?, ?> bill_id;

    @FXML
    private Button bill_order;

    @FXML
    private TableColumn<?, ?> bill_total_price;

    @FXML
    private TableView<Order> card_display_table;

    @FXML
    private Button card_remove;

    @FXML
    private Label card_total;

    @FXML
    private Button cardpay_btn;

    @FXML
    private Label cart_quantity;

    @FXML
    private AnchorPane home;

    @FXML
    private Button homeButton;

    @FXML
    private Button inbienlai;

    @FXML
    private Button logout;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private AnchorPane order;

    @FXML
    private Button orderButton;

    @FXML
    private TableColumn<?, ?> price;

    @FXML
    private TableColumn<?, ?> productID;

    @FXML
    private TableColumn<?, ?> productName;

    @FXML
    private Button product_add;

    @FXML
    private Button product_delete;

    @FXML
    private Button product_update;

    @FXML
    private Button salarybutton;

    @FXML
    private TextField search_products;

    @FXML
    private TableColumn<Product, String> showcard_order;

    @FXML
    private TableColumn<Product, Double> showcard_price;

    @FXML
    private TableColumn<Product, Integer> showcard_quantity;

    @FXML
    private TableView<?> table;

    @FXML
    private TableView<?> table_orders;

    @FXML
    private Label total_bill;

    @FXML
    private Label total_price;

    @FXML
    private Label username;

    private ObservableList<Product> productList;

    private ObservableList<Order> ordersList;

    @FXML
    void handleLogout(ActionEvent event) {

    }

    @FXML
    void handleUpdateProduct(ActionEvent event) {

    }

    @FXML
    void search(KeyEvent event) {

    }

    @FXML
    void selectAllProducts(ActionEvent event) {

    }

    public void menuRestart() {
        card_display_table.getSelectionModel().getSelectedItem();
        card_total.setText("0.0 ");
        cart_quantity.setText("");
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void updateCart(ActionEvent event) {

        OrderDAO orderDAO = new OrderImplements();
        Order selectedOrder = card_display_table.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            showAlert(Alert.AlertType.ERROR, "Vui lòng chọn đơn hàng để cập nhật!");
            return;
        }
        orderDAO.updateOrder(selectedOrder.getOrderID());

        Bills bills = new Bills();

        // Loại bỏ ký tự không phải là số và khoảng trắng trong chuỗi giá tiền
        String totalPriceString = card_total.getText().replaceAll("[^\\d.]", "").trim();

        bills.setTotalPrice(Double.parseDouble(totalPriceString));
        bills.setCustomerID(Data.customerID);
        bills.setDate(new Date());

        BillDAO billDAO = new BillImple();
        boolean selectBill = billDAO.addBill(bills);
        showAlert(Alert.AlertType.INFORMATION, "Cập nhật đơn hàng thành công!");
        showDisplayCard();
        menuRestart();
    }

    public void menuDisplayCard() {
        ProductDAO productDAO = new ProductImple();

        if (productList == null) {
            productList = FXCollections.observableArrayList();
        }

        productList.clear();
        List<Product> products = productDAO.show();

        if (products != null && !products.isEmpty()) {
            productList.addAll(products);
            int row = 0;
            int column = 0;
            menu_gridPane.getChildren().clear();

            for (Product product : productList) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/thuan/dev/controller/cardProduct.fxml"));
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
                    e.printStackTrace();
                }
            }
        }
    }

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

    public void totalCustomers() {
        BillDAO billDAO = new BillImple();
        List<Bills> selectedBills = billDAO.getAllBills();
        int totalPrice = 0;
        int customerID = 0;

        if (selectedBills != null) {
            for (Bills bills : selectedBills) {
                if (bills != null) {
                    customerID++;
                    totalPrice += bills.getTotalPrice();
                }
            }
        }
        double finalTotal = totalPrice;
        Platform.runLater(() -> total_price.setText(String.format("$%.2f", finalTotal)));
        total_bill.setText(String.valueOf(customerID));
    }

    public void displayUsername() {
        username.setText(String.valueOf(Data.customerID));
    }

    @FXML
    private void initialize(){
        AppService.getInstance().setAdminController(this);
    }

    @FXML
    private void switchForm(ActionEvent event) {
        if (event.getSource() == homeButton) {
            home.setVisible(true);
            order.setVisible(false);
            displayUsername();
        } else if (event.getSource() == orderButton) {
            home.setVisible(false);
            order.setVisible(true);
            menuDisplayCard();
            displayUsername();
            showDisplayCard();
            totalCustomers();
        }

    }


}