package thuan.dev.models.bill;

import javafx.scene.control.Label;
import thuan.dev.config.MyConnection;
import thuan.dev.controller.Data;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillImple implements BillDAO{
    @Override
    public List<Bills> getAllBillDateNow() {
        List<Bills> bills = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM bill where date = ?");
            statement.setDate(1,java.sql.Date.valueOf(today));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Bills bill = new Bills();
                bill.setBillID(rs.getInt("billID"));
                bill.setCustomerID(rs.getInt("customerID"));
                bill.setTotalPrice(rs.getDouble("total"));
                bill.setDate(new Date(rs.getDate("date").getTime()));
                bills.add(bill);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bills;
    }

    @Override
    public void totalPrice(Bills bills) {

    }

    Connection conn = MyConnection.getConnection();

    @Override
    public List<Bills> getAllBills() {
        List<Bills> bills = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM bill");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Bills bill = new Bills();
                bill.setBillID(rs.getInt("billID"));
                bill.setCustomerID(rs.getInt("customerID"));
                bill.setTotalPrice(rs.getDouble("total"));
                bill.setDate(new Date(rs.getDate("date").getTime()));
                bills.add(bill);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bills;
    }


    @Override
    public boolean addBill(Bills bills) {
        try {
            conn.setAutoCommit(false);


            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO bill(customerID, date, total) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setInt(1, Data.customerID);
            statement.setDate(2, new Date(bills.getDate().getTime()));
            statement.setObject(3, bills.getTotalPrice());

            int check = statement.executeUpdate();


            ResultSet rs = statement.getGeneratedKeys();
            int newBillID = 0;
            if (rs.next()) {
                newBillID = rs.getInt(1);
            }

            PreparedStatement updateStatement = conn.prepareStatement(
                    "UPDATE orders SET bills = ? WHERE order_detailsID = ?"
            );
            updateStatement.setInt(1, newBillID);
            updateStatement.setInt(2, 1);

            int updateCheck = updateStatement.executeUpdate();


            PreparedStatement updateStatusStatement = conn.prepareStatement(
                    "UPDATE orders SET order_detailsID = 0 WHERE status = 0"
            );
            int updateStatusCheck = updateStatusStatement.executeUpdate();

            // Hoàn tất transaction
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




}
