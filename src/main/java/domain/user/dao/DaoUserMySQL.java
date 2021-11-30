package domain.user.dao;

import java.util.ArrayList;

import config.MySQLConfig;
import domain.user.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoUserMySQL implements DaoUser {

    private MySQLConfig mysqlConfig;

    public DaoUserMySQL() {
        mysqlConfig = MySQLConfig.getInstance();
    }

    @Override
    public void insertUser(User user) {
        String sqlInsert = "INSERT INTO user(username,password,first_name,second_name,email) VALUES(?,?,?,?,?)";

        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getFirstName());
            stm.setString(4, user.getSecondName());
            stm.setString(5, user.getEmail());
            System.out.println(stm);
            if (stm.executeUpdate() > 0) {
                // Get the ID
                try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } else {
                throw new SQLException("Creating user failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mysqlConfig.disconnect(connection, stm);
    }

    @Override
    public User loadUser(String username, String password) {
        String sqlQuery = "SELECT * FROM user WHERE username=? AND password=?";
        User user = null;
        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlQuery);
            stm.setString(1, username);
            stm.setString(2, password);
            System.out.println(stm);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setSecondName(rs.getString("second_name"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoLoginMysql loadUser " + username);
        }
        mysqlConfig.disconnect(connection, stm);
        return user;
    }

    @Override
    public User loadUser(int userId) {
        String sqlQuery = "SELECT * FROM user WHERE userId=?";
        User user = null;
        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlQuery);
            stm.setInt(1, userId);
            System.out.println(stm);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setSecondName(rs.getString("second_name"));
                user.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoLoginMysql loadUser " + userId);
        }
        mysqlConfig.disconnect(connection, stm);
        return user;
    }

    @Override
    public ArrayList<User> loadUsers() {
        ArrayList<User> userList = new ArrayList<User>();
        Connection connection = mysqlConfig.connect();
        String sqlQuery = "SELECT * FROM user";

        ResultSet rs = null;
        PreparedStatement stm = null;

        try {
            stm = connection.prepareStatement(sqlQuery);
            rs = stm.executeQuery();
            while (rs.next()) {
                User user = new User();

                user.setUserId(rs.getInt("userId"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setSecondName(rs.getString("second_name"));
                user.setEmail(rs.getString("email"));

                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoLoginMysql loadUsers");
        }
        mysqlConfig.disconnect(connection, stm);
        return userList;
    }

    @Override
    public void updateUser(User user) {
        String sqlUpdate = "UPDATE user SET username=?, password=?, first_name=?, second_name=?, email=? WHERE userId=?";

        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlUpdate);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getFirstName());
            stm.setString(4, user.getSecondName());
            stm.setString(5, user.getEmail());
            stm.setInt(6, user.getUserId());

            if (stm.executeUpdate() < 1) {
                user.setUserId(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoLoginMysql updateUser " + user.getUserId());
        }
        mysqlConfig.disconnect(connection, stm);
    }

    @Override
    public boolean deleteUser(int userId) {
        boolean ret = false;
        String sqlDelete = "DELETE FROM user WHERE userId=?";
        Connection connection = mysqlConfig.connect();
        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sqlDelete);
            stm.setInt(1, userId);
            if (stm.executeUpdate() > 0) {
                ret = true;
            } else {
                System.out.println("Could not delete user " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error DaoLoginMysql deleteUser " + userId);
        }
        mysqlConfig.disconnect(connection, stm);

        return ret;
    }

}
