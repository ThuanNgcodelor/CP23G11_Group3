package thuan.dev.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import thuan.dev.config.MyConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Bill_out implements Initializable {
    Connection conn = MyConnection.getConnection();

    @FXML
    private Label giaorderout;

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

    private ObservableList<Order_out> order_outObservableList;

    @FXML
    private TextField voucher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listOrder_out();
    }

    public void listOrder_out(){
        List<Order_out> order_outList = showOrder_out(0);

        if (order_outList.isEmpty()) {
            System.out.println("No orders found");
        }

        order_outObservableList = FXCollections.observableArrayList(order_outList);

        name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        ordertable.setItems(order_outObservableList);
    }

    public List<Order_out> showOrder_out(int orders) {
        List<Order_out> order_outList = new ArrayList<>();

        // Check for valid connection
        if (conn == null) {
            System.out.println("Database connection is null.");
            return order_outList;
        }

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM order_out WHERE order_details = ?");
            statement.setInt(1, orders);

            ResultSet rs = statement.executeQuery();

            if (!rs.isBeforeFirst()) {  // No data in the result set
                System.out.println("No data found for order_details = " + orders);
            }

            while (rs.next()) {
                Order_out order = new Order_out();
                order.setProductName(rs.getString("productName"));
                order.setQuantity(rs.getInt("quantity"));
                order.setPrice(rs.getInt("price"));

                // Debug output

                order_outList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order_outList;
    }
}
