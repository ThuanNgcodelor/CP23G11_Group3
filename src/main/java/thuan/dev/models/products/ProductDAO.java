package thuan.dev.models.products;

import java.util.List;

public interface ProductDAO {

    public List<Product> show();

    public boolean addProduct(Product pro);

    public void updateProduct(Product pro);

    public List<Product> search(String keyword);

    public boolean deleteProduct(Integer productID);

    public int getProductStock(int productID);

    public void updateProductStock(int productID, int stock);

}
