package domain.advertisment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;

import config.MySQLConfig;
import domain.advertisment.model.Advertisment;

public class DaoAdvertismentMySQL implements DaoAdvertisment {

    private MySQLConfig mysqlConfig;

    public DaoAdvertismentMySQL() {
        mysqlConfig = MySQLConfig.getInstance();
    }

    @Override
    public void insertAdvertisment(Advertisment advertisment) {
        String sqlInsert = "INSERT INTO advertisement (title,url,src,lang) VALUES(?,?,?,?)";

        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, advertisment.getTitle());
            stm.setString(2, advertisment.getUrl());
            stm.setString(3, advertisment.getSrc());
            stm.setString(4, advertisment.getLocale().getLanguage());
            System.out.println(stm);
            if (stm.executeUpdate() > 0) {
                // Get the ID
                try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        advertisment.setAdvertismentId(generatedKeys.getInt(1));
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
    public Advertisment loaAdvertisment(int advertismentId) {
        String sqlQuery = "SELECT * FROM advertisement WHERE advertisementId=?";
        Advertisment advertisment = null;
        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlQuery);
            stm.setInt(1, advertismentId);
            System.out.println(stm);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                advertisment = new Advertisment();

                advertisment.setAdvertismentId(rs.getInt("advertisementId"));
                advertisment.setTitle(rs.getString("title"));
                advertisment.setUrl(rs.getString("url"));
                advertisment.setSrc(rs.getString("src"));


                String langStr = rs.getString("lang");
                Locale lang = Locale.forLanguageTag(langStr);
                advertisment.setLocale(lang);
            }
        } catch (SQLException e) {
            System.out.println(sqlQuery);
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql loadNewsItem");
        }
        mysqlConfig.disconnect(connection, stm);
        return advertisment;
    }

    @Override
    public ArrayList<Advertisment> loadAllAdvertisments() {
        ArrayList<Advertisment> allAdvertisements = new ArrayList<Advertisment>();
        Connection connection = mysqlConfig.connect();

        String sqlQuery = "SELECT * FROM advertisement";

        ResultSet rs = null;
        PreparedStatement stm = null;
        Advertisment advertisment = null;
        try {
            stm = connection.prepareStatement(sqlQuery);
            rs = stm.executeQuery();
            while (rs.next()) {

                advertisment = new Advertisment();

                advertisment.setAdvertismentId(rs.getInt("advertisementId"));
                advertisment.setTitle(rs.getString("title"));
                advertisment.setUrl(rs.getString("url"));
                advertisment.setSrc(rs.getString("src"));


                String langStr = rs.getString("lang");
                Locale lang = Locale.forLanguageTag(langStr);
                advertisment.setLocale(lang);


                allAdvertisements.add(advertisment);
            }
        } catch (SQLException e) {
            System.out.println(sqlQuery);
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql loadAllNewsItems");
        }
        mysqlConfig.disconnect(connection, stm);
        return allAdvertisements;
    }

    @Override
    public void updateAdvertisment(Advertisment advertisment) {
        String sqlUpdate = "UPDATE advertisement SET title=?, url=?, src=?, lang=? WHERE advertisementId=?";

        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlUpdate);
            stm.setString(1, advertisment.getTitle());
            stm.setString(2, advertisment.getUrl());
            stm.setString(3, advertisment.getSrc());
            stm.setString(4, advertisment.getLocale().getLanguage());
            stm.setInt(5, advertisment.getAdvertismentId());

            if (stm.executeUpdate() < 1) {
                advertisment.setAdvertismentId(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql updateNewsItem " + advertisment.getAdvertismentId());
        }
        mysqlConfig.disconnect(connection, stm);        
    }

    @Override
    public boolean deleteAdvertisment(int advertismentId) {
        boolean ret = false;
        String sqlDelete = "DELETE FROM advertisement WHERE advertisementId=?";
        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlDelete);
            stm.setInt(1, advertismentId);

            if (stm.executeUpdate() > 0) {
                ret = true;
            } else {
                System.out.println("Could not delete newsItem " + advertismentId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoNewsItemMysql deleteNewsItem " + advertismentId);

        }
        mysqlConfig.disconnect(connection, stm);
        return ret;
    }
    
}
