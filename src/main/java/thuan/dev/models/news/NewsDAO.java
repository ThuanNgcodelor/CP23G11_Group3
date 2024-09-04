package thuan.dev.models.news;

import java.util.List;

public interface NewsDAO {
    boolean addNews(News news);
    void updateNews(News news);
    boolean deleteNews(int newsID);
    List<News> getAllNews();
    List<News> searchNews(String keyword);
}
