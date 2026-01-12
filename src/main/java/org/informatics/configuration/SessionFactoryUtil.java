package org.informatics.configuration;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.informatics.entity.*;

import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for managing Hibernate SessionFactory.
 * Provides a singleton SessionFactory instance configured from properties files.
 *
 * Different properties files can be loaded based on the system property "hibernate.properties":
 * - hibernate.properties (default for main application)
 * - hibernate-unit.properties (for unit tests)
 * - hibernate-integration.properties (for integration tests)
 */
public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;

    /**
     * Returns the singleton SessionFactory instance.
     * Creates the SessionFactory on first access using the configured properties file.
     *
     * @return the Hibernate SessionFactory
     * @throws RuntimeException if properties file cannot be loaded
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();

            // Load properties file based on system property (for test configuration)
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

            // Register all entity classes
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

    /**
     * Closes the SessionFactory and releases all resources.
     * Used primarily for cleanup in test environments.
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}