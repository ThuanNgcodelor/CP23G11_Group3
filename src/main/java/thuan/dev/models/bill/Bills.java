package thuan.dev.models.bill;

import java.util.Date;

public class Bills {
    private int billID;
    private double totalPrice;
    private int customerID;
    private Date date;

    public Bills(int billID, double totalPrice, int customerID, Date date) {
        this.billID = billID;
        this.totalPrice = totalPrice;
        this.customerID = customerID;
        this.date = date;
    }

    public Bills() {
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
