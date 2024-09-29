package thuan.dev.models.logintime;

import java.util.Date;
import java.util.List;

public interface LoginTimeDAO {
    public List<LoginTime> showAll(int EmployeeID);
    public List<LoginTime> showHistory(int EmployeeID);
    public void dateStartEnd(Date start, Date end);
    public Date[] getMinMaxDates(int employeeI);
    public void updateLoginTimeStatus(int employeeID);
}
