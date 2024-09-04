package thuan.dev.models.tables;

import java.util.Date;

public class Tables {
    private int tableID;
    private String table_name;
    private String fullname;
    private String address;
    private Date ngaythang;
    private int status;

    public Tables(int tableID, String table_name, String fullname, String address, Date ngaythang,int status) {
        this.tableID = tableID;
        this.table_name = table_name;
        this.fullname = fullname;
        this.address = address;
        this.ngaythang = ngaythang;
        this.status = status;
    }

    public Tables() {
        this.fullname = null;
        this.address = null;
        this.ngaythang = null;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getNgaythang() {
        return ngaythang;
    }

    public void setNgaythang(Date ngaythang) {
        this.ngaythang = ngaythang;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}