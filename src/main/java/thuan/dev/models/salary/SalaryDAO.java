package thuan.dev.models.salary;

import java.time.LocalDateTime;
import java.util.List;

public interface SalaryDAO {
    public LocalDateTime timeStart(Integer staffID);
    public LocalDateTime timeEnd(Integer staffID);
    public void getSalary(Integer staffID);
    public void countSalary(Salary salary);
    public void countSalary2(Salary salary);
}
