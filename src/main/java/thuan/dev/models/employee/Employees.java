package thuan.dev.models.employee;

import java.util.Date;

public class Employees {

    private int employeeID;
    private String phone;
    private String cccd;
    private Date birth;
    private String email;
    private String password;

    public Employees(String phone, String cccd, Date birth, String email, String password) {
        this.phone = phone;
        this.cccd = cccd;
        this.birth = birth;
        this.email = email;
        this.password = password;
    }

    public Employees() {

    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }
}
