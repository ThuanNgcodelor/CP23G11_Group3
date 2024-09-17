package thuan.dev.models.salary;

import java.time.LocalDateTime;

public class Salary {
    Integer salaryID;
    LocalDateTime timeStart;
    LocalDateTime timeEnd;
    Integer hours;
    Integer minutes;

    public Salary(Integer salaryID, LocalDateTime timeStart, LocalDateTime timeEnd, Integer hours, Integer minutes) {
        this.salaryID = salaryID;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.hours = hours;
        this.minutes = minutes;
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
