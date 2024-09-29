
package thuan.dev.models.historysalary;

import java.util.Date;

public class Historys {
    private Integer ID;
    private Integer salary;
    private Date datetime;

    public Historys(Integer ID, Integer salary, Date datetime) {
        this.ID = ID;
        this.salary = salary;
        this.datetime = datetime;
    }

    public Historys(){}

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
