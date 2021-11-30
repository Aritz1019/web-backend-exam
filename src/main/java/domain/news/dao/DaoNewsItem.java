package domain.news.dao;
import java.util.ArrayList;
import java.util.Locale;

import domain.news.model.NewsItem;

public interface DaoNewsItem {
	public void insertNewsItem(NewsItem newsItem);
	public NewsItem loadNewsItem(int newsId);
	public ArrayList<NewsItem> loadAllNewsItems(Locale locale);
	public void updateNewsItem(NewsItem newsItem);
	public boolean deleteNewsItem(Integer newsItemId);
}
