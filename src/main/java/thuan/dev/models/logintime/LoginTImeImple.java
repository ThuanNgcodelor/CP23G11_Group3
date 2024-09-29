package thuan.dev.models.logintime;

import thuan.dev.config.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginTImeImple implements LoginTimeDAO{
    @Override
    public List<LoginTime> showHistory(int employeeID) {
        List<LoginTime> loginTimeList = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * from loginTime where customerID = ? ORDER BY datetime DESC");
            statement.setInt(1,employeeID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                LoginTime lgT = new LoginTime();
                lgT.setHours(rs.getInt("hours"));
                lgT.setMinutes(rs.getInt("minutes"));
                lgT.setDate(rs.getDate("datetime"));
                lgT.setStatus(rs.getInt("status"));
                loginTimeList.add(lgT);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return loginTimeList;
    }
    @Override
    public void updateLoginTimeStatus(int employeeID) {
        try {
            String query = "UPDATE loginTime SET status = ? WHERE status = ? AND customerID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, 1);
            statement.setInt(2, 0);
            statement.setInt(3, employeeID);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dateStartEnd(Date start, Date end) {
    }

    public Date[] getMinMaxDates(int employeeID) {
        Date[] minMaxDates = new Date[2];
        try {
            String query = "SELECT MIN(datetime) as startDate, MAX(datetime) as endDate FROM loginTime WHERE status = ? and customerID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, 0);
            statement.setInt(2,employeeID);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                minMaxDates[0] = rs.getDate("startDate");
                minMaxDates[1] = rs.getDate("endDate");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return minMaxDates;
    }


    Connection conn = MyConnection.getConnection();

    @Override
    public List<LoginTime> showAll(int employeeID) {
        List<LoginTime> loginTimeList = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * from loginTime where status = ? and customerID = ? ORDER BY datetime DESC");
            statement.setInt(1,0);
            statement.setInt(2,employeeID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                LoginTime lgT = new LoginTime();
                lgT.setHours(rs.getInt("hours"));
                lgT.setMinutes(rs.getInt("minutes"));
                lgT.setDate(rs.getDate("datetime"));
                lgT.setStatus(rs.getInt("status"));
                loginTimeList.add(lgT);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return loginTimeList;
    }
}
