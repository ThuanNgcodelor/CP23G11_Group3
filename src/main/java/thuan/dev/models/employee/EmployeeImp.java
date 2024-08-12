package thuan.dev.models.employee;

import thuan.dev.config.MyConnection;
import thuan.dev.controller.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class EmployeeImp implements EmployeeDAO {

    Date date;

    private static final Connection conn = MyConnection.getConnection();

    @Override
    public List<Employees> show() {
        return List.of();
    }

    @Override
    public boolean addEmployee(Employees emp) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO customers(phone,birth,cccd,email,password,fullname) VALUES (?,?,?,?,?,?) ");
            stmt.setString(1, emp.getPhone());
            stmt.setDate(2, new java.sql.Date(emp.getBirth().getDate()));
            stmt.setString(3, emp.getCccd());
            stmt.setString(4, emp.getEmail());
            stmt.setString(5, emp.getPassword());
            stmt.setString(6,emp.getFullname());
            int check = stmt.executeUpdate();
            return check > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Employees emp) {

    }

    @Override
    public int checkLogin(String email, String password) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE email=? AND Password=?");
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Data.customerID = rs.getInt("customerID");
                Data.email = rs.getString("email");
                Data.fullname = rs.getString("fullname");
                return rs.getInt("role");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }


    @Override
    public List<Employees> search(String keyword) {
        return List.of();
    }

    @Override
    public void delete(Employees emp) {

    }
}
