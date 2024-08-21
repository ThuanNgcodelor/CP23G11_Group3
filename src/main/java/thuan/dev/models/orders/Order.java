package thuan.dev.models.orders;

import java.util.Date;

public class Order {

    private Integer orderID;
    private Integer customerID;
    private String productName;
    private Double price;
    private Integer quantity;
    private Integer total;
    private Date date;
    private Integer productID;

    public Order(String productName, Double price, Integer quantity, Integer total, Date date,Integer productID) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.date = date;
        this.productID = productID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Order() {

    }

    public Integer getOrderID() {
        return orderID;
    }

    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
