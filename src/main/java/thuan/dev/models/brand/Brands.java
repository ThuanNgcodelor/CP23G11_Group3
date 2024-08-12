package thuan.dev.models.brand;

public class Brands {
    private int brandID;
    private String brandName;

    public Brands(int brandID, String brandName) {
        this.brandID = brandID;
        this.brandName = brandName;
    }

    public Brands() {
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    @Override
    public String toString() {
        return brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
