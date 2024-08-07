package thuan.dev.models.products;

public class Product {

    private Integer productID;
    private String productName;
    private String images;
    private Integer categoryID;
    private Integer brandID;
    private Double price;
    private String categoryName;
    private String brandName;
    private Integer stock;

    public Product(Integer productID, String productName, String images, Integer categoryID, Integer brandID, Double price, String categoryName, String brandName, Integer stock) {
        this.productID = productID;
        this.productName = productName;
        this.images = images;
        this.categoryID = categoryID;
        this.brandID = brandID;
        this.price = price;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.stock = stock;
    }

    public Product() {
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Integer getBrandID() {
        return brandID;
    }

    public void setBrandID(Integer brandID) {
        this.brandID = brandID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
