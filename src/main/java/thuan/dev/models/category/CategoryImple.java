package thuan.dev.models.category;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryImple implements CategoryDAO{
    Connection conn = MyConnection.getConnection();
    @Override
    public boolean addCategory(Category cate) {
    return false;
    }

    @Override
    public void updateCategory(Category cate) {

    }

    @Override
    public boolean deleteCategory(Category cate) {
        return false;
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
