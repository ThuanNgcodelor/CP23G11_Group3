package thuan.dev.models.brand;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BrandImple implements BrandDAO{
    @Override
    public List<Brands> searchBrand(String keyword) {
        List<Brands> brands = new ArrayList<>();
        try {
            String query = "SELECT * FROM brands WHERE brandName LIKE ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Brands brand = new Brands();
                brand.setBrandID(rs.getInt("brandID"));
                brand.setBrandName(rs.getString("brandName"));
                brands.add(brand);
            }
        } catch (Exception e) {
            System.out.println("Lỗi không thể tìm kiếm brand: " + e.getMessage());
        }
        return brands;
    }


    Connection conn = MyConnection.getConnection();
    @Override
    public boolean addBrand(Brands brand) {
        boolean isAdded = false;
        try {
            String query = "INSERT INTO brands (brandName) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, brand.getBrandName());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                isAdded = true;
            }
        } catch (Exception e) {
            System.out.println("Lỗi không thể thêm brand: " + e.getMessage());
        }
        return isAdded;
    }


    @Override
    public void updateBrand(Brands brand) {
        try {
            String query = "UPDATE brands SET brandName = ? WHERE brandID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, brand.getBrandName());
            statement.setInt(2, brand.getBrandID());
            statement.executeUpdate();

        } catch (Exception e) {
            System.out.println("Lỗi không thể update brand: " + e.getMessage());
        }
    }


    @Override
    public boolean deleteBrand(Brands brand) {
        try {
            String query = "DELETE FROM brands WHERE brandID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, brand.getBrandID());
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Lỗi không thể xóa brand: " + e.getMessage());
        }
        return false;
    }


    @Override
    public List<Brands> getAllBrand() {
        List<Brands> brands = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM brands");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Brands brand = new Brands();
                brand.setBrandID(rs.getInt("brandID"));
                brand.setBrandName(rs.getString("brandName"));
                brands.add(brand);
            }
        } catch (Exception e) {
            System.out.println("Lỗi không show được brand: " + e.getMessage());
        }
        return brands;
    }


}
