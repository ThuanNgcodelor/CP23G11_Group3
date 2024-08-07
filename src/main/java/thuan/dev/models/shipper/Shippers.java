package thuan.dev.models.shipper;

public class Shippers {
    private int shipperID;
    private String ShipperName;
    private int shipperPhone;
    private int billID;
    private int cccd;
    private int age;

    public Shippers(int shipperID, String shipperName, int shipperPhone, int billID, int cccd, int age) {
        this.shipperID = shipperID;
        ShipperName = shipperName;
        this.shipperPhone = shipperPhone;
        this.billID = billID;
        this.cccd = cccd;
        this.age = age;
    }

    public Shippers() {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
