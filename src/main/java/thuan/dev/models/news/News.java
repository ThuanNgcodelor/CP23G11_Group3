package thuan.dev.models.news;

public class News {
    private int newsID;
    private String newsName;
    private String newsDetails;
    private String Images;

    public News(int newsID, String newsName, String newsDetails, String Images) {
        this.newsID = newsID;
        this.newsName = newsName;
        this.newsDetails = newsDetails;
        this.Images = Images;
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
        return Images;
    }

    public void setNewsImages(String v) {
        this.Images = Images;
    }

    @Override
    public String toString() {
        return newsName;
    }
}
