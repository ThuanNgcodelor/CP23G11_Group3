package thuan.dev.models.orders;

import thuan.dev.config.MyConnection;
import thuan.dev.controller.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderImplements implements OrderDAO {
    @Override
    public void totalPrice() {

    }

    @Override
    public void receipt() {

    }

    Connection conn = MyConnection.getConnection();

    @Override
    public List<Order> menuOrder(Order ord) {
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM orders WHERE customerID = ?");
            statement.setInt(1, Data.customerID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setCustomerID(rs.getInt("customerID"));
                order.setProductName(rs.getString("productName"));
                order.setPrice(rs.getDouble("price"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotal(rs.getInt("total"));
                order.setDate(rs.getDate("date"));
                orders.add(order);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public List<Order> showDisplayCard() {
        List<Order> orders = new ArrayList<>();
        try {
            // Select all orders with the latest order_detailsID
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM orders WHERE order_detailsID = 1"
            );
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("orderID"));
                order.setCustomerID(rs.getInt("customerID"));
                order.setProductName(rs.getString("productName"));
                order.setPrice(rs.getDouble("price"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotal(rs.getInt("total"));
                order.setDate(rs.getDate("date"));
                orders.add(order);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public boolean removeOrder(Order order) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM orders WHERE orderID = ?");
            statement.setInt(1, order.getOrderID());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateOrder(int orderID) {
        try (PreparedStatement statement = conn.prepareStatement("UPDATE orders SET order_detailsID = 0")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addOrder(Order order) {
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO orders (customerID, productName, price, quantity, total, date,order_detailsID) VALUES (?, ?, ?, ?, ?, ?,?)")) {
            statement.setInt(1, Data.customerID);
            statement.setString(2, order.getProductName());
            statement.setDouble(3, order.getPrice());
            statement.setInt(4, order.getQuantity());
            statement.setDouble(5, order.getTotal());
            statement.setDate(6, new Date(order.getDate().getTime()));
            statement.setInt(7, 1);
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
