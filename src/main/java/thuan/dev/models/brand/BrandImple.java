package thuan.dev.models.brand;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BrandImple implements BrandDAO{
    Connection conn = MyConnection.getConnection();
    @Override
    public boolean addBrand(Brands brands) {
        return false;
    }

    @Override
    public void updateBrand(Brands brands) {

    }

    @Override
    public boolean deleteBrand(Brands brands) {
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
