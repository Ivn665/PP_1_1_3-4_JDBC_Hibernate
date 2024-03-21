package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.Entity;

import javax.persistence.Table;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final SessionFactory SESSION_FACTORY = Util.getSessionFactory();

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
                """, Entity.class.getAnnotation(Table.class).name());
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void dropUsersTable() throws HibernateException {
        String sql = String.format("DROP TABLE IF EXISTS %s", Entity.class.getAnnotation(Table.class).name());
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) throws HibernateException {
        try (Session session = SESSION_FACTORY.openSession()) {
            User user = new User(name, lastName, age);
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void removeUserById(long id) throws HibernateException {

        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) System.out.printf("Юзер с id = %d в базе данных не найден\n", id);
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() throws HibernateException {

        List<User> usersList;
        try (Session session = SESSION_FACTORY.openSession()) {
            usersList = session.createQuery("FROM User", User.class).list();
        } catch (HibernateException e) {
            throw e;
        }
        return usersList;
    }

    @Override
    public void cleanUsersTable() throws HibernateException {

        Transaction transaction = null;
        try (Session session = SESSION_FACTORY.openSession()) {
            session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            SESSION_FACTORY.getCurrentSession().getTransaction().rollback();
            throw e;
        }
    }
}
