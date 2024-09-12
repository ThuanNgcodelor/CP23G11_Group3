package thuan.dev.models.shipper;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShipperImple implements ShipperDAO {
    Connection conn = MyConnection.getConnection();

    @Override
    public void getShipper(Shippers ship) {
        try {
            String query = "SELECT * FROM shipper WHERE shipperID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, ship.getShipperID());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ship.setShipperName(resultSet.getString("shipperName"));
                ship.setShipperPhone(resultSet.getInt("shipperPhone"));
                ship.setCccd(resultSet.getInt("cccd"));
                ship.setEmail(resultSet.getString("email"));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Shippers> showAllShipper(Shippers ship) {
        List<Shippers> shippersList = new ArrayList<>();
        try {
            String query = "SELECT * FROM shipper";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Shippers s = new Shippers();
                s.setShipperID(resultSet.getInt("shipperID"));
                s.setShipperName(resultSet.getString("shipperName"));
                s.setShipperPhone(resultSet.getInt("shipperPhone"));
                s.setCccd(resultSet.getInt("cccd"));
                s.setEmail(resultSet.getString("email"));
                shippersList.add(s);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return shippersList;
    }

    @Override
    public boolean deleteShipper(Shippers ship) {
        boolean isDeleted = false;
        try {
            String query = "DELETE FROM shipper WHERE shipperID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, ship.getShipperID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                isDeleted = true;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    @Override
    public boolean addShipper(Shippers ship) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO shipper(shipperName, shipperPhone, cccd, email) VALUES (?, ?, ?, ?)"
            );
            statement.setString(1, ship.getShipperName());
            statement.setInt(2, ship.getShipperPhone());
            statement.setInt(3, ship.getCccd());
            statement.setString(4, ship.getEmail());

            int rowsInserted = statement.executeUpdate();
            statement.close();
            return rowsInserted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateShipper(Shippers ship) {
        boolean isUpdated = false;
        try {
            String query = "UPDATE shipper SET shipperName = ?, shipperPhone = ?, cccd = ?, email = ? WHERE shipperID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, ship.getShipperName());
            statement.setInt(2, ship.getShipperPhone());
            statement.setInt(3, ship.getCccd());
            statement.setString(4, ship.getEmail());
            statement.setInt(5, ship.getShipperID());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                isUpdated = true;
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

}
