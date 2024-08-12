package thuan.dev.models.category;

import java.util.List;

public interface CategoryDAO{
    public boolean addCategory(Category cate);
    public void updateCategory(Category cate);
    public boolean deleteCategory(Category cate);
    public List<Category> getAllCategory();
}
