package thuan.dev.models.employee;

import java.util.List;

public interface EmployeeDAO {

    public List<Employees> show();

    public boolean addEmployee(Employees emp);

    public void update(Employees emp);

    public boolean checkLogin(String email, String password);

    public List<Employees> search(String keyword);

    public void delete(Employees emp);

}
