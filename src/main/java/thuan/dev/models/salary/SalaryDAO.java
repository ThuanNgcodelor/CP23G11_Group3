package thuan.dev.models.salary;

import java.time.LocalDateTime;

public interface SalaryDAO {
    public LocalDateTime timeStart(Integer staffID);
    public LocalDateTime timeEnd(Integer staffID);
    public void getSalary(Integer staffID);
    public void countSalary(Salary salary);
}
