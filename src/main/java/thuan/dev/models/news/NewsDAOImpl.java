package thuan.dev.models.news;

import thuan.dev.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.File;

public class NewsDAOImpl implements NewsDAO {
    Connection conn = MyConnection.getConnection();



    @Override
    public boolean addNews(News news) {
        boolean isAdded = false;
        try {
            String query = "INSERT INTO news (newsName, newsDetails, newsImages) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, news.getNewsName());
            statement.setString(2, news.getNewsDetails());
            statement.setString(3, news.getNewsImages());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                isAdded = true;
            }
        } catch (Exception e) {
            System.out.println("Lỗi không thể thêm news: " + e.getMessage());
        }
        return isAdded;
    }


//    @Override
//    public boolean addNews(News news) {
//        boolean isAdded = false;
//        try {
//            String query = "INSERT INTO news (newsName, newsDetails, newsImages) VALUES (?, ?, ?)";
//            PreparedStatement statement = conn.prepareStatement(query);
//            statement.setString(1, news.getNewsName());
//            statement.setString(2, news.getNewsDetails());
//            statement.setString(3, news.getNewsImages());
//
//            int rowsInserted = statement.executeUpdate();
//            if (rowsInserted > 0) {
//                isAdded = true;
//            }
//        } catch (Exception e) {
//            System.out.println("Lỗi không thể thêm news: " + e.getMessage());
//        }
//        return isAdded;
//    }

    @Override
    public void updateNews(News news) {
        try {
            String query = "UPDATE news SET newsName = ?, newsDetails = ?, newsImages = ? WHERE newsID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, news.getNewsName());
            statement.setString(2, news.getNewsDetails());
            statement.setString(3, news.getNewsImages());
            statement.setInt(4, news.getNewsID());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi không thể update news: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteNews(int newsID) {
        try {
            String query = "DELETE FROM news WHERE newsID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, newsID);
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        } catch (Exception e) {
            System.out.println("Lỗi không thể xóa news: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<News> getAllNews() {
        List<News> newsList = new ArrayList<>();
        try {
            String query = "SELECT * FROM news";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                News news = new News();
                news.setNewsID(rs.getInt("newsID"));
                news.setNewsName(rs.getString("newsName"));
                news.setNewsDetails(rs.getString("newsDetails"));
                news.setNewsImages(rs.getString("newsImages"));
                newsList.add(news);
            }
        } catch (Exception e) {
            System.out.println("Lỗi không show được news: " + e.getMessage());
        }
        return newsList;
    }

    @Override
    public List<News> searchNews(String keyword) {
        List<News> newsList = new ArrayList<>();
        try {
            String query = "SELECT * FROM news WHERE newsName LIKE ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, "%" + keyword + "%");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                News news = new News();
                news.setNewsID(rs.getInt("newsID"));
                news.setNewsName(rs.getString("newsName"));
                news.setNewsDetails(rs.getString("newsDetails"));
                news.setNewsImages(rs.getString("newsImages"));
                newsList.add(news);
            }
        } catch (Exception e) {
            System.out.println("Lỗi không thể tìm kiếm news: " + e.getMessage());
        }
        return newsList;
    }
}
