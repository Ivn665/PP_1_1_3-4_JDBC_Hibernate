package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final String TABLE_NAME = "USERS";
    private Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {
        //
    }

    @Override
    public void createUsersTable() throws SQLException {

        String sql = String.format("""
                CREATE TABLE `%s` (
                `id` bigint NOT NULL AUTO_INCREMENT,
                `name` varchar(45) NOT NULL,
                `lastName` varchar(45) NOT NULL,
                `age` tinyint NOT NULL,
                PRIMARY KEY (`id`),
                UNIQUE KEY `id_UNIQUE` (`id`)
                ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3
                """, TABLE_NAME);
        if (!ifExist()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Не удалось создать новую таблицу");
                throw e;
            }
        }
    }

    @Override
    public void dropUsersTable() throws SQLException {

        String sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Не удалось удалить таблицу");
            throw e;
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) throws SQLException {

        String sql = String.format("INSERT INTO %s (name, lastName, age) VALUES (?, ?, ?);", TABLE_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void removeUserById(long id) throws SQLException {

        String sql = String.format("DELETE FROM %s WHERE id = ?;", TABLE_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {

        String sql = String.format("SELECT * FROM %s;", TABLE_NAME);
        List<User> usersList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                usersList.add(user);
            }
        } catch (SQLException e) {
            throw e;
        }
        return usersList;
    }

    @Override
    public void cleanUsersTable() throws SQLException {

        String sql = String.format("TRUNCATE %s", TABLE_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean ifExist() throws SQLException {
        String sql = String.format("SHOW TABLES LIKE '%s'", TABLE_NAME);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return preparedStatement.executeQuery().next() ? true : false;
        } catch (SQLException e) {
            throw e;
        }
    }
}
