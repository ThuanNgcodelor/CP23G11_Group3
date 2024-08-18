package thuan.dev.models.brand;

import java.util.List;

public interface BrandDAO {
    public boolean addBrand(Brands brands);
    public void updateBrand(Brands brands);
    public boolean deleteBrand(Brands brands);
    public List<Brands> getAllBrand();
    public List<Brands> searchBrand(String keyword);
}
