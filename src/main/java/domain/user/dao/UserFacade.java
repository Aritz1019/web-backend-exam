package domain.user.dao;

import java.util.ArrayList;

import domain.user.model.User;

public class UserFacade {
    DaoUser daoUser = null;

    public UserFacade() {
        daoUser = new DaoUserMySQL();
    }

    public User loadUser(String username, String password) {
        return daoUser.loadUser(username, password);
    }

    public User loadUser(int userId) {
        return daoUser.loadUser(userId);
    }

    public ArrayList<User> loadUsers() {
        return daoUser.loadUsers();
    }

    public void saveUser(User user) {
        if (user.getUserId() == 0) {
            daoUser.insertUser(user);
        } else {
            daoUser.updateUser(user);
        }

    }

    public boolean deleteUser(int userId) {
        return daoUser.deleteUser(userId);
    }

}
