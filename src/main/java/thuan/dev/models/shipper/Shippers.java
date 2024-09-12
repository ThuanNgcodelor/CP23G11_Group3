package thuan.dev.models.shipper;

import java.util.Date;

public class Shippers {
    private int shipperID;
    private String ShipperName;
    private int shipperPhone;
    private int billID;
    private int cccd;
    private String email;

    public Shippers(int shipperID, String shipperName, int shipperPhone, int billID, int cccd, String email) {
        this.shipperID = shipperID;
        this.ShipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.billID = billID;
        this.cccd = cccd;
        this.email = email;
    }

    public Shippers() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getShipperID() {
        return shipperID;
    }

    public void setShipperID(int shipperID) {
        this.shipperID = shipperID;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }

    public int getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(int shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public int getBillID() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID = billID;
    }

    public int getCccd() {
        return cccd;
    }

    public void setCccd(int cccd) {
        this.cccd = cccd;
    }
}
