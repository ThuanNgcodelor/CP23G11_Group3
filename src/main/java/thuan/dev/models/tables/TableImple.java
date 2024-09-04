package thuan.dev.models.tables;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TableImple implements TableDAO {
    Connection conn = MyConnection.getConnection();

    @Override
    public List<Tables> showAllTable() {
        List<Tables> tablesList = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM tables");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Tables table = new Tables(
                        rs.getInt("tableID"),
                        rs.getString("tableName"),
                        rs.getString("fullname"),
                        rs.getString("address"),
                        rs.getDate("ngaythang"),
                        rs.getInt("status")
                );
                tablesList.add(table);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tablesList;
    }
}
