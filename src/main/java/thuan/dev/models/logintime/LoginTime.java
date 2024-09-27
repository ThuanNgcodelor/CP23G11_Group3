package thuan.dev.models.logintime;

import java.util.Date;

public class LoginTime {
    private Integer hours;
    private Integer minutes;
    private Date date;

    public LoginTime(Integer hours, Integer minutes, Date date) {
        this.hours = hours;
        this.minutes = minutes;
        this.date = date;
    }

    public LoginTime() {}

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
