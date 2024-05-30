package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.dao.UserDao;
import org.hibernate.HibernateException;

import java.sql.*;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDao userDao = new UserDaoJDBCImpl();
    //private final UserDao userDao = new UserDaoHibernateImpl();

    public void createUsersTable() throws SQLException, HibernateException {
        userDao.createUsersTable();
    }

    public void dropUsersTable() throws SQLException, HibernateException {
        userDao.dropUsersTable();
    }

    public void saveUser(String name, String lastName, byte age) throws SQLException, HibernateException {
        userDao.saveUser(name, lastName, age);
        System.out.printf("User с именем — %s добавлен в базу данных\n", name);
    }

    public void removeUserById(long id) throws SQLException, HibernateException {
        userDao.removeUserById(id);
    }

    public List<User> getAllUsers() throws SQLException, HibernateException {
        return userDao.getAllUsers();
    }

    public void cleanUsersTable() throws SQLException, HibernateException {
        userDao.cleanUsersTable();
    }
}
