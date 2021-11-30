package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class to reuse repetitive MySQL tasks. It is a singleton class so
 * database.properties is only loaded once.
 * 
 * @author aperez
 */
public class MySQLConfig {
    /*
     * This is not the best policy to create connections, but is used to simplify
     * it. More info at: https://www.baeldung.com/java-connection-pooling
     */

    private static MySQLConfig myconfig;

    private String username;
    private String password;
    private String connectionString;

    /**
     * Singleton pattern. getInstance() must be static, otherwise it cannot be
     * called (as constructor is private).
     */
    public static MySQLConfig getInstance() {
        if (myconfig == null) {
            myconfig = new MySQLConfig();
        }
        return myconfig;
    }

    /**
     * Private constructor. If someone wants the instance, it should call
     * MySQLConfig.getInstance() That way the constructor is called only once.
     */
    private MySQLConfig() {
        Properties prop = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            prop.load(classLoader.getResourceAsStream("db.properties"));
            String serverName = prop.getProperty("serverName");
            String port = prop.getProperty("port");
            String dataBaseName = prop.getProperty("dataBaseName");
            String url = prop.getProperty("url");
            String timezone = prop.getProperty("timezone");
            username = prop.getProperty("username");
            password = prop.getProperty("password");
            connectionString = url + serverName + ":" + port + "/" + dataBaseName + "?serverTimezone=" + timezone;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a new MySQL Connection.
     */
    public Connection connect() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Connection Driver Error");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could Not Connect to DB ");
        }
        return connection;
    }

    /**
     * Close a MySQL Connection.
     */
    public void disconnect(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.clearWarnings();
                statement.close();
            }

            if (connection != null) {
                connection.clearWarnings();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error disconnecting");
        }
    }
}
