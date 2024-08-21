package thuan.dev.models.employee;

import thuan.dev.config.MyConnection;
import thuan.dev.controller.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeImp implements EmployeeDAO {
    @Override
    public boolean updateCustomer(Employees emp) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE customers SET phone = ?, birth = ?, cccd = ?, email = ?, password = ?, fullname = ?, role = ? WHERE customerID = ?"
            );
            statement.setString(1, emp.getPhone());
            statement.setDate(2, new java.sql.Date(emp.getBirth().getTime()));
            statement.setString(3, emp.getCccd());
            statement.setString(4, emp.getEmail());
            statement.setString(5, emp.getPassword());
            statement.setString(6, emp.getFullname());
            statement.setInt(7, emp.getRole());
            statement.setInt(8, emp.getEmployeeID());

            int rowsUpdated = statement.executeUpdate();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updatePassword(String email, String oldPassword, String newPassword) {
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT * from customers WHERE email = ? AND password = ?");
            statement.setString(1,email);
            statement.setString(2,oldPassword);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                PreparedStatement updateStatement = conn.prepareStatement("UPDATE customers set password = ? where email = ?");
                updateStatement.setString(1,newPassword);
                updateStatement.setString(2,email);
                int check = updateStatement.executeUpdate();
                return check > 0;
            }else {
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Date date;

    private static final Connection conn = MyConnection.getConnection();

    @Override
    public List<Employees> show() {
        List<Employees> employees = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employees emp = new Employees();
                emp.setEmployeeID(rs.getInt("customerID"));
                emp.setPhone(rs.getString("phone"));
                emp.setBirth(rs.getDate("birth"));
                emp.setCccd(rs.getString("cccd"));
                emp.setEmail(rs.getString("email"));
                emp.setPassword(rs.getString("password"));
                emp.setFullname(rs.getString("fullname"));
                emp.setRole(rs.getInt("role"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }


    @Override
    public boolean addEmployee(Employees emp) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO customers(phone,birth,cccd,email,password,fullname,role) VALUES (?,?,?,?,?,?,?) ");
            stmt.setString(1, emp.getPhone());
            stmt.setDate(2, new java.sql.Date(emp.getBirth().getDate()));
            stmt.setString(3, emp.getCccd());
            stmt.setString(4, emp.getEmail());
            stmt.setString(5, emp.getPassword());
            stmt.setString(6,emp.getFullname());
            stmt.setInt(7,emp.getRole());
            int check = stmt.executeUpdate();
            return check > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        List<Employees> employees = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE fullname LIKE ? OR phone LIKE ? OR email LIKE ? ");
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setString(3, searchKeyword);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Employees emp = new Employees();
                emp.setPhone(rs.getString("phone"));
                emp.setBirth(new Date(rs.getDate("birth").getTime()));
                emp.setCccd(rs.getString("cccd"));
                emp.setEmail(rs.getString("email"));
                emp.setPassword(rs.getString("password"));
                emp.setFullname(rs.getString("fullname"));
                emp.setRole(rs.getInt("role"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employees;
    }


    @Override
    public void delete(Employees emp) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM customers WHERE customerID = ?");
            statement.setInt(1, emp.getEmployeeID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
