package thuan.dev.controller.dat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

    // Chuỗi kết nối với tham số encrypt=false để vô hiệu hóa SSL
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLySV;encrypt=false;";
    private static final String USER = "sa";
    private static final String PASSWORD = "1234";

    public static List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT TOP (1000) [productID], [productName], [categoryID], [price], [images], [stock], [mota], [brandID] FROM [QuanLySV].[dbo].[product]";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            // Lấy dữ liệu từ ResultSet và thêm vào danh sách sản phẩm
            while (rs.next()) {
                int productID = rs.getInt("productID");
                String productName = rs.getString("productName");
                int categoryID = rs.getInt("categoryID");
                double price = rs.getDouble("price");
                String images = rs.getString("images");
                int stock = rs.getInt("stock");
                String mota = rs.getString("mota");
                int brandID = rs.getInt("brandID");

                // Thêm sản phẩm vào danh sách
                products.add(new Product(productID, productName, categoryID, price, images, stock, mota, brandID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }
}
