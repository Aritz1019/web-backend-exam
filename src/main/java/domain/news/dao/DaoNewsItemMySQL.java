package domain.news.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import config.MySQLConfig;
import domain.news.model.NewsItem;
import domain.user.dao.DaoUserMySQL;
import domain.user.model.User;

public class DaoNewsItemMySQL implements DaoNewsItem {

    private MySQLConfig mysqlConfig;

    public DaoNewsItemMySQL() {
        mysqlConfig = MySQLConfig.getInstance();
    }

    @Override
    public void insertNewsItem(NewsItem newsItem) {
        String sqlInsert = "INSERT INTO news_item (title,body,lang,authorId) VALUES(?,?,?,?)";

        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, newsItem.getTitle());
            stm.setString(2, newsItem.getBody());
            stm.setString(3, newsItem.getLang().getLanguage());
            stm.setInt(4, newsItem.getAuthor().getUserId());
            System.out.println(stm);
            if (stm.executeUpdate() > 0) {
                // Get the ID
                try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newsItem.setNewsItemId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating News Item failed, no ID obtained.");
                    }
                }
            } else {
                throw new SQLException("Creating News Item failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mysqlConfig.disconnect(connection, stm);
    }

    @Override
    public NewsItem loadNewsItem(int newsItemId) {
        String sqlQuery = "SELECT * FROM news_item WHERE newsItemId=?";
        NewsItem newsItem = null;
        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlQuery);
            stm.setInt(1, newsItemId);
            System.out.println(stm);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int authorId;
                User author;
                newsItem = new NewsItem();

                newsItem.setNewsItemId(rs.getInt("newsItemId"));
                newsItem.setTitle(rs.getString("title"));
                newsItem.setBody(rs.getString("body"));

                Timestamp ts = rs.getTimestamp("date");
                Date date = new Date(ts.getTime());
                newsItem.setDate(date);

                String langStr = rs.getString("lang");
                Locale lang = Locale.forLanguageTag(langStr);
                newsItem.setLang(lang);

                authorId = rs.getInt("authorId");
                author = new DaoUserMySQL().loadUser(authorId);
                newsItem.setAuthor(author);
            }
        } catch (SQLException e) {
            System.out.println(sqlQuery);
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql loadNewsItem");
        }
        mysqlConfig.disconnect(connection, stm);
        return newsItem;
    }

    @Override
    public ArrayList<NewsItem> loadAllNewsItems(Locale locale) {
        ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
        Connection connection = mysqlConfig.connect();
        String sqlQuery = "SELECT * FROM news_item WHERE lang=?";
        String languageTag = locale.getLanguage();
        System.out.println("Display Language:" + locale.getDisplayLanguage());
        System.out.println("Language:" + locale.getLanguage());
        System.out.println("LanguageTag:" + locale.toLanguageTag());

        ResultSet rs = null;
        PreparedStatement stm = null;
        NewsItem newsItem = null;
        try {
            stm = connection.prepareStatement(sqlQuery);
            stm.setString(1, languageTag);
            rs = stm.executeQuery();
            while (rs.next()) {

                newsItem = new NewsItem();

                newsItem.setNewsItemId(rs.getInt("newsItemId"));
                newsItem.setTitle(rs.getString("title"));
                newsItem.setBody(rs.getString("body"));

                Timestamp ts = rs.getTimestamp("date");
                Date date = new Date(ts.getTime());
                newsItem.setDate(date);

                String langStr = rs.getString("lang");
                Locale lang = Locale.forLanguageTag(langStr);
                newsItem.setLang(lang);

                int authorId = rs.getInt("authorId");
                User author = new DaoUserMySQL().loadUser(authorId);
                newsItem.setAuthor(author);

                newsItems.add(newsItem);
            }
        } catch (SQLException e) {
            System.out.println(sqlQuery);
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql loadAllNewsItems");
        }
        mysqlConfig.disconnect(connection, stm);
        return newsItems;
    }

    @Override
    public void updateNewsItem(NewsItem newsItem) {
        String sqlUpdate = "UPDATE news_item SET title=?, body=?, lang=? WHERE newsItemId=?";

        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlUpdate);
            stm.setString(1, newsItem.getTitle());
            stm.setString(2, newsItem.getBody());
            stm.setString(3, newsItem.getLang().toLanguageTag());
            stm.setInt(4, newsItem.getNewsItemId());

            if (stm.executeUpdate() < 1) {
                newsItem.setNewsItemId(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql updateNewsItem " + newsItem.getNewsItemId());
        }
        mysqlConfig.disconnect(connection, stm);
    }

    @Override
    public boolean deleteNewsItem(Integer newsItemId) {

        boolean ret = false;
        String sqlDelete = "DELETE FROM news_item WHERE newsItemId=?";
        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlDelete);
            stm.setInt(1, newsItemId);

            if (stm.executeUpdate() > 0) {
                ret = true;
            } else {
                System.out.println("Could not delete newsItem " + newsItemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql deleteNewsItem " + newsItemId);

        }
        mysqlConfig.disconnect(connection, stm);
        return ret;
    }

}
