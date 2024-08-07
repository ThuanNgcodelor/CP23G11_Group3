package thuan.dev.models.news;

public class News {
    private int newsID;
    private String newsName;
    private String newsDetails;
    private String newsImages;


    public News(int newsID, String newsName, String newsDetails, String newsImages) {
        this.newsID = newsID;
        this.newsName = newsName;
        this.newsDetails = newsDetails;
        this.newsImages = newsImages;

    }

    public News() {
    }

    public int getNewsID() {
        return newsID;
    }

    public void setNewsID(int newsID) {
        this.newsID = newsID;
    }

    public String getNewsName() {
        return newsName;
    }

    public void setNewsName(String newsName) {
        this.newsName = newsName;
    }

    public String getNewsDetails() {
        return newsDetails;
    }

    public void setNewsDetails(String newsDetails) {
        this.newsDetails = newsDetails;
    }

    public String getNewsImages() {
        return newsImages;
    }

    public void setNewsImages(String newsImages) {
        this.newsImages = newsImages;
    }
}
