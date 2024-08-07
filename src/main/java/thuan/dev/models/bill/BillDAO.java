package thuan.dev.models.bill;

import javafx.scene.control.Label;

import java.util.List;

public interface BillDAO {

    public boolean addBill(Bills bills);
    public void totalPrice(Bills bills);
    public List<Bills> getAllBills();
}
