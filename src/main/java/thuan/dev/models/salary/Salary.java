package thuan.dev.models.salary;

import java.time.LocalDateTime;
import java.util.Date;

public class Salary {
    Integer salaryID;
    LocalDateTime timeStart;
    LocalDateTime timeEnd;
    Integer hours;
    Integer minutes;
    private long totalHours;
    private long totalMinutes;
    private long totalDays;
    Integer customerID;
    Date datetime;

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Salary(Integer salaryID, LocalDateTime timeStart, LocalDateTime timeEnd, Integer hours, Integer minutes, long totalHours, long totalMinutes, long totalDays) {
        this.salaryID = salaryID;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.hours = hours;
        this.minutes = minutes;
        this.totalHours = totalHours;
        this.totalMinutes = totalMinutes;
        this.totalDays = totalDays;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public long getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(long totalHours) {
        this.totalHours = totalHours;
    }

    public long getTotalMinutes() {
        return totalMinutes;
    }

    public void setTotalMinutes(long totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    public long getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(long totalDays) {
        this.totalDays = totalDays;
    }

    public Salary() {}

    public Integer getSalaryID() {
        return salaryID;
    }

    public void setSalaryID(Integer salaryID) {
        this.salaryID = salaryID;
    }

    public LocalDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

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
}
