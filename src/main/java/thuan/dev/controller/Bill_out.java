package thuan.dev.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import thuan.dev.config.MyConnection;
import thuan.dev.models.bill.Bills;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Bill_out implements Initializable {
    Connection conn = MyConnection.getConnection();

    @FXML
    private TableColumn<Order_out, String> name;

    @FXML
    private TableView<Order_out> ordertable;

    @FXML
    private TableColumn<Order_out, Integer> price;

    @FXML
    private TableColumn<Order_out, Integer> quantity;

    @FXML
    private Label totalprice;

    @FXML
    private TextField totalquantity;

    @FXML
    private Button placeOrderButton;  // The place order button

    private ObservableList<Order_out> order_outObservableList;

    @FXML
    private TextField voucher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOrder_out();

        // Disable the Place Order button if no orders are available
        placeOrderButton.setDisable(order_outObservableList == null || order_outObservableList.isEmpty());
    }

    public void listOrder_out() {
        List<Order_out> order_outList = showOrder_out(1);

        if (order_outList.isEmpty()) {
            System.out.println("No orders found");
        }

        order_outObservableList = FXCollections.observableArrayList(order_outList);

        name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        ordertable.setItems(order_outObservableList);
        totalPrice();
    }

    private void totalPrice() {
        double total = 0;
        int quantity = 0;

        if (order_outObservableList != null) {
            for (Order_out order : order_outObservableList) {
                if (order != null) {
                    total += order.getPrice();
                    quantity += order.getQuantity();
                }
            }
        }

        double finalTotal = total;
        int finalQuantity = quantity;

        Platform.runLater(() -> {
            totalprice.setText(String.format("%,.0f", finalTotal));
            totalquantity.setText(String.format("%d", finalQuantity));
        });
    }

    public boolean addBill(Bills bills) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            conn.setAutoCommit(false);
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO bill(date, total, status) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setTimestamp(1, new Timestamp(bills.getDate().getTime()));
            statement.setObject(2, bills.getTotalPrice());
            statement.setInt(3,0);

            int check = statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            int newBillID = 0;
            if (rs.next()) {
                newBillID = rs.getInt(1);
            }

            PreparedStatement updateStatement = conn.prepareStatement(
                    "UPDATE order_out SET billID = ? WHERE order_details = ?"
            );
            updateStatement.setInt(1, newBillID);
            updateStatement.setInt(2, 1);

            int updateCheck = updateStatement.executeUpdate();

            PreparedStatement updateStatusStatement = conn.prepareStatement(
                    "UPDATE order_out SET order_details = 0 WHERE status = 0"
            );
            int updateStatusCheck = updateStatusStatement.executeUpdate();

            conn.commit();

            return check > 0 && updateCheck > 0 && updateStatusCheck > 0;

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    public void handlePlaceOrder(ActionEvent event) {
        // Parse total price from the label (removing commas if necessary)
        double total = Double.parseDouble(totalprice.getText().replace(",", ""));

        // Get the current date and time as a Timestamp
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        Bills bill = new Bills();
        bill.setTotalPrice(total);
        bill.setDate(currentTimestamp);

        boolean result = addBill(bill);

        if (result) {
            System.out.println("Order placed successfully");
            // Optionally, clear the table and refresh orders
            listOrder_out(); // Refresh the orders
        } else {
            System.out.println("Failed to place the order");
        }
    }


    public List<Order_out> showOrder_out(int orders) {
        List<Order_out> order_outList = new ArrayList<>();

        if (conn == null) {
            System.out.println("Database connection is null.");
            return order_outList;
        }

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM order_out WHERE order_details = ?");
            statement.setInt(1, orders);

            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("No data found for order_details = " + orders);
            }

            while (rs.next()) {
                Order_out order = new Order_out();
                order.setProductName(rs.getString("productName"));
                order.setQuantity(rs.getInt("quantity"));
                order.setPrice(rs.getInt("price"));

                order_outList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order_outList;
    }
}
