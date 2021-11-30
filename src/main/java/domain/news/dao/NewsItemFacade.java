package domain.news.dao;

import java.util.ArrayList;
import java.util.Locale;

import domain.news.model.NewsItem;

public class NewsItemFacade {
    DaoNewsItem daoNewsItem;

    public NewsItemFacade() {
        daoNewsItem = new DaoNewsItemMySQL();
    }

    public void insertNewsItem(NewsItem newsItem) {
        daoNewsItem.insertNewsItem(newsItem);
    }

    public NewsItem loadNewsItem(int newsItemId) {
        return daoNewsItem.loadNewsItem(newsItemId);
    }

    public ArrayList<NewsItem> loadAllNewsItems(Locale locale) {
        return daoNewsItem.loadAllNewsItems(locale);
    }

    public void saveNewsItem(NewsItem newsItem) {
        if (newsItem.getNewsItemId() == null || newsItem.getNewsItemId() == 0) {
            daoNewsItem.insertNewsItem(newsItem);
        } else {
            daoNewsItem.updateNewsItem(newsItem);
        }
    }

    public boolean deleteNewsItem(Integer newsItemId) {
        return daoNewsItem.deleteNewsItem(newsItemId);
    }
}
