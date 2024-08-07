package thuan.dev.models.bill;

import javafx.scene.control.Label;
import thuan.dev.config.MyConnection;
import thuan.dev.controller.Data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BillImple implements BillDAO{
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
            PreparedStatement statement = conn.prepareStatement("INSERT INTO bill Values(?,?,?)");
            statement.setInt(1, Data.customerID);
            statement.setDate(2,new Date(bills.getDate().getTime()));
            statement.setObject(3,bills.getTotalPrice());

            int check = statement.executeUpdate();
            return check > 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
