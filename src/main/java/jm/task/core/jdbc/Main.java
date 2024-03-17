package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {

            UserServiceImpl UserServiceImpl = new UserServiceImpl();

            UserServiceImpl.createUsersTable();

            UserServiceImpl.saveUser("Степан", "Степанов", (byte) 27);
            UserServiceImpl.saveUser("Карл", "Карлсcон", (byte) 45);
            UserServiceImpl.saveUser("Дмитрий ", "Свекольников", (byte) 12);
            UserServiceImpl.saveUser("Гена", "Крокодил", (byte) 33);

            UserServiceImpl.removeUserById(4);
            List<User> userList = UserServiceImpl.getAllUsers();
            for (User user : userList) {
                System.out.println(user);
            }
            UserServiceImpl.cleanUsersTable();
            UserServiceImpl.dropUsersTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}