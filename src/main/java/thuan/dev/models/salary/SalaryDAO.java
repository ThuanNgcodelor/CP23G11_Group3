package thuan.dev.models.salary;

import java.time.LocalDateTime;

public interface SalaryDAO {
    public LocalDateTime timeStart();
    public LocalDateTime timeEnd();
    public void getSalary();
    public void countSalary(Salary salary);
}
