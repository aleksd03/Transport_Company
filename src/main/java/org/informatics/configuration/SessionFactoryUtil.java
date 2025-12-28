package org.informatics.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.informatics.entity.*;

import java.io.InputStream;
import java.util.Properties;

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            // Choose which properties file to load:
            // - default: hibernate.properties (main)
            // - tests can override via: -Dhibernate.props=hibernate-unit.properties / hibernate-integration.properties
            String propsFile = System.getProperty("hibernate.props", "hibernate.properties");

            Properties props = new Properties();
            try (InputStream is = SessionFactoryUtil.class.getClassLoader().getResourceAsStream(propsFile)) {
                if (is == null) {
                    throw new RuntimeException("Cannot find " + propsFile + " in classpath.");
                }
                props.load(is);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load Hibernate properties file: " + propsFile, e);
            }

            configuration.setProperties(props);

            // Register entities
            configuration.addAnnotatedClass(TransportCompany.class);
            configuration.addAnnotatedClass(Client.class);
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(Driver.class);
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(Bus.class);
            configuration.addAnnotatedClass(Truck.class);
            configuration.addAnnotatedClass(Tanker.class);
            configuration.addAnnotatedClass(Transport.class);
            configuration.addAnnotatedClass(CargoTransport.class);
            configuration.addAnnotatedClass(PassengerTransport.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
}