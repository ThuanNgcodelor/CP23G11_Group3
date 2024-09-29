package thuan.dev.models.historysalary;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HistoryImp implements HistoryDAO{
    Connection conn = MyConnection.getConnection();
    @Override
    public List<Historys> showHistory(int employee) {
        List<Historys> historyList = new ArrayList<>();
        try{
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM historySalary where customerID = ?");
            statement.setInt(1,employee);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Historys his = new Historys();
                his.setSalary(rs.getInt("salary"));
                his.setDatetime(rs.getDate("date"));
                historyList.add(his);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return historyList;
    }
}
