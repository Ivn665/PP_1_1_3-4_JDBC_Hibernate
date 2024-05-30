package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final String TABLE_NAME = "USERS";
    private Connection connection = null;

    public UserDaoJDBCImpl() {
        //
    }

    @Override
    public void createUsersTable() throws SQLException {

        String sql = String.format("""
                CREATE TABLE IF NOT EXISTS `%s` (
                `id` bigint NOT NULL AUTO_INCREMENT,
                `name` varchar(45) NOT NULL,
                `lastName` varchar(45) NOT NULL,
                `age` tinyint NOT NULL,
                PRIMARY KEY (`id`),
                UNIQUE KEY `id_UNIQUE` (`id`)
                ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3
                """, TABLE_NAME);
            try {
                connection = Util.getConnection();
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            } finally {
                connection.close();
            }
    }

    @Override
    public void dropUsersTable() throws SQLException {

        String sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
        try {
            connection = Util.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } finally {
            connection.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) throws SQLException {

        String sql = String.format("INSERT INTO %s (name, lastName, age) VALUES (?, ?, ?);", TABLE_NAME);
        try {
            connection = Util.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
        } finally {
            connection.close();
        }
    }

    @Override
    public void removeUserById(long id) throws SQLException {

        String sql = String.format("DELETE FROM %s WHERE id = ?;", TABLE_NAME);
        try {
            connection = Util.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            statement.executeUpdate();
        } finally {
            connection.close();
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {

        String sql = String.format("SELECT * FROM %s;", TABLE_NAME);
        List<User> usersList = new ArrayList<>();
        try {
            connection = Util.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                usersList.add(user);
            }
        } finally {
            connection.close();
        }
        return usersList;
    }

    @Override
    public void cleanUsersTable() throws SQLException {

        String sql = String.format("TRUNCATE %s", TABLE_NAME);
        try {
            connection = Util.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        } finally {
            connection.close();
        }
    }
}
