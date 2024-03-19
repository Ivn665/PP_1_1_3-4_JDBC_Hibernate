package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final String TABLE_NAME = "USERS";
    private static final SessionFactory SESSIONFACTORY = Util.getSessionFactory();
    private Session session = null;
    private Transaction transaction = null;

    public UserDaoHibernateImpl() {
        //
    }

    @Override
    public void createUsersTable() throws HibernateException {
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
            session = SESSIONFACTORY.openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() throws HibernateException {
        String sql = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        try {
            session = SESSIONFACTORY.openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) throws HibernateException {
        try {
            session = SESSIONFACTORY.openSession();
            User user = new User(name, lastName, age);
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) throws HibernateException {

        try {
            session = SESSIONFACTORY.openSession();
            User user = session.get(User.class, id);
            if (user == null) System.out.printf("Юзер с id = %d в базе данных не найден\n", id);
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() throws HibernateException {

        List<User> usersList;
        try {
            session = SESSIONFACTORY.openSession();
            usersList = session.createQuery("FROM User", User.class).list();
        } finally {
            session.close();
        }
        return usersList;
    }

    @Override
    public void cleanUsersTable() throws HibernateException {

        try {
            session = SESSIONFACTORY.openSession();
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
