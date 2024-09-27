package thuan.dev.models.bill;

import thuan.dev.models.orders.Order;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface BillDAO {

    public boolean addBill(Bills bills);
    public List<Bills> getAllBills();
    public List<Bills> getAllBills2();
    public List<Bills> getAllBillDateNow();
    public List<Order> showDetailsBill(Bills bills);
    public boolean updateStatusBill(Bills bills);
    public void updateOrder(Bills bills);
    public Map<Timestamp, Double> sumBill();
    public Map<Timestamp, Double> sumBillDays();

}
