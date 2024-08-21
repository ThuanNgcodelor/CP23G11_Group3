package thuan.dev.models.orders;

import thuan.dev.config.MyConnection;
import thuan.dev.controller.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderImplements implements OrderDAO {
    @Override
    public void update1(int productID, int quantity, int total) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE orders SET quantity = ?, total = ? WHERE productID = ? AND order_detailsID = 1"
            );
            statement.setInt(1, quantity);
            statement.setInt(2, total);
            statement.setInt(3, productID);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(int productID, int quantity, int total, int orderID) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE orders SET quantity = ?, total = ? WHERE productID = ? AND orderID = ?"
            );
            statement.setInt(1, quantity);
            statement.setInt(2, total);
            statement.setInt(3, productID);
            statement.setInt(4, orderID);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order getOrder( int productID) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM orders WHERE productID = ? and order_detailsID = ? "
            );
            statement.setInt(1, productID);
            statement.setInt(2,1);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return new Order(
                        rs.getString("productName"),
                        rs.getDouble("price"),
                        rs.getInt("quantity"),
                        rs.getInt("total"),
                        rs.getDate("date"),
                        rs.getInt("productID")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT * FROM orders WHERE order_detailsID = 1 and status = 1"
            );
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderID(rs.getInt("orderID"));
                order.setProductName(rs.getString("productName"));
                order.setPrice(rs.getDouble("price"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotal(rs.getInt("total"));
                order.setDate(rs.getDate("date"));
                order.setProductID(rs.getInt("productID"));
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
        String sql = "UPDATE orders SET status = ? WHERE order_detailsID = ?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, 0);
            statement.setInt(2, 1);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean addOrder(Order order) {
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO orders (productName, price, quantity, total, date,order_detailsID,status,productID) VALUES ( ?, ?, ?, ?, ?,?,?,?)")) {
            statement.setString(1, order.getProductName());
            statement.setDouble(2, order.getPrice());
            statement.setInt(3, order.getQuantity());
            statement.setDouble(4, order.getTotal());
            statement.setDate(5, new Date(order.getDate().getTime()));
            statement.setInt(6, 1);
            statement.setInt(7,1);
            statement.setInt(8,order.getProductID());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
