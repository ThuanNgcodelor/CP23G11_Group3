package thuan.dev.models.bill;

import javafx.scene.control.Label;
import thuan.dev.models.orders.Order;

import java.util.List;

public interface BillDAO {

    public boolean addBill(Bills bills);
    public void totalPrice(Bills bills);
    public List<Bills> getAllBills();
    public List<Bills> getAllBillDateNow();
    public List<Order> showDetailsBill(Bills bills);
}
