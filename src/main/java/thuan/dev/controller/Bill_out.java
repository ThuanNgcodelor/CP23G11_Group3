package thuan.dev.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import thuan.dev.config.MyConnection;
import thuan.dev.models.bill.Bills;
import thuan.dev.models.orders.Order;
import thuan.dev.models.orders.OrderDAO;
import thuan.dev.models.orders.OrderImplements;
import thuan.dev.models.products.ProductDAO;
import thuan.dev.models.products.ProductImple;

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

    @FXML
    private void hanhDeleteProduct(ActionEvent event){
        Order_out orderOut = ordertable.getSelectionModel().getSelectedItem();
        int quantity = orderOut.getQuantity();
        int productID = orderOut.getProductID();

        ProductDAO productDAO = new ProductImple();
        int stock = productDAO.getProductStock(productID);
        int newStock = stock + quantity;
        productDAO.updateProductStock(productID,newStock);
        boolean success = removeOrder_out(orderOut);
        if (success){
            showAlert(Alert.AlertType.INFORMATION, "Success", "Order deleted successfully!");

        }
        listOrder_out();
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

    public boolean removeOrder_out(Order_out order_out){
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM order_out WHERE order_outID = ?");
            statement.setInt(1,order_out.getOrder_id());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addBill(Bills bills) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        try {
            conn.setAutoCommit(false);

            // Insert into bill table
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO bill(date, total, status) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setTimestamp(1, new Timestamp(bills.getDate().getTime()));
            statement.setDouble(2, bills.getTotalPrice());
            statement.setInt(3, 0);

            int check = statement.executeUpdate();
            if (check == 0) {
                System.out.println("Failed to insert into bill table");
                conn.rollback();
                return false;
            }

            // Get the generated bill ID
            ResultSet rs = statement.getGeneratedKeys();
            int newBillID = 0;
            if (rs.next()) {
                newBillID = rs.getInt(1);
            } else {
                System.out.println("No bill ID returned. Rolling back.");
                conn.rollback();
                return false;
            }

            // Update order_out with the new billID
            PreparedStatement updateStatement = conn.prepareStatement(
                    "UPDATE order_out SET billID = ? WHERE order_details = ?"
            );
            updateStatement.setInt(1, newBillID);
            updateStatement.setInt(2, 1); // Assuming '1' represents orders needing a bill ID update

            int updateCheck = updateStatement.executeUpdate();
            if (updateCheck == 0) {
                System.out.println("Failed to update order_out with bill ID. Rolling back.");
                conn.rollback();
                return false;
            }

            PreparedStatement updateStatusStatement = conn.prepareStatement(
                    "UPDATE order_out SET order_details = 0 WHERE status = 0"
            );
            int updateStatusCheck = updateStatusStatement.executeUpdate();
            if (updateStatusCheck == 0) {
                System.out.println("No orders updated to reset order details. Rolling back.");
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateOrder_out(int orderID) {
        String sql = "UPDATE order_out SET status = ? WHERE order_details = ?";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, 0); // Set status to 0
            statement.setInt(2, 1);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Order status updated successfully for order ID: " + orderID);
            } else {
                System.out.println("No order found with the specified ID: " + orderID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating order status", e);
        }
    }


    @FXML
    public void handlePlaceOrder(ActionEvent event) {
        Order_out orderOut = ordertable.getSelectionModel().getSelectedItem();
        updateOrder_out(orderOut.getOrder_id());
        double total = Double.parseDouble(totalprice.getText().replace(",", ""));

        // Get the current date and time as a Timestamp
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        Bills bill = new Bills();
        bill.setTotalPrice(total);
        bill.setDate(currentTimestamp);

        boolean result = addBill(bill);

        if (result) {
            System.out.println("Order placed successfully");
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
                order.setProductID(rs.getInt("productID"));

                order_outList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order_outList;
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
