package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        try {
            UserService userService = new UserServiceImpl();

            userService.createUsersTable();

            userService.saveUser("Степан", "Степанов", (byte) 27);
            userService.saveUser("Карл", "Карлсcон", (byte) 45);
            userService.saveUser("Дмитрий ", "Свекольников", (byte) 12);
            userService.saveUser("Гена", "Крокодил", (byte) 33);

            userService.removeUserById(1);
            List<User> userList = userService.getAllUsers();
            for (User user : userList) {
                System.out.println(user);
            }
            userService.cleanUsersTable();
            userService.dropUsersTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}