package thuan.dev.models.category;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryImple implements CategoryDAO {
    @Override
    public List<Category> searchCategory(String keyword) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category WHERE categoryName LIKE ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("categoryID"));
                category.setName(rs.getString("categoryName"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
    }


    Connection conn = MyConnection.getConnection();

    @Override
    public boolean addCategory(Category cate) {
        try {
            String query = "INSERT INTO category (categoryName) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cate.getName());
            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateCategory(Category cate) {
        try {
            String query = "UPDATE category SET categoryName = ? WHERE categoryID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, cate.getName());
            statement.setInt(2, cate.getId());
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Category updated successfully!");
            } else {
                System.out.println("No category found with the given ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteCategory(Category cate) {
        try {
            String query = "DELETE FROM category WHERE categoryID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, cate.getId());
            int rowsDeleted = statement.executeUpdate();

            return rowsDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM category");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("categoryID"));
                category.setName(rs.getString("categoryName"));
                categories.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categories;
    }
}
