package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;

import java.util.Properties;

import java.sql.Connection;
import java.sql.DriverManager;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class Util {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/users_db_test";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2root!()root2";

    public static Connection getConnection() {
        Connection connection = null;
        if (connection == null) {
            try {
                //Class.forName(DB_DRIVER).getDeclaredConstructor().newInstance();
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection ERROR");
            }
        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        SessionFactory sessionFactory = null;
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, DB_DRIVER);
                settings.put(Environment.URL, URL);
                settings.put(Environment.USER, USERNAME);
                settings.put(Environment.PASS, PASSWORD);
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                //settings.put(Environment.HBM2DDL_AUTO, "");
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
