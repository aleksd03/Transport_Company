package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Driver;
import org.informatics.entity.enums.DriverQualification;
import org.hibernate.Session;

import java.util.List;

/**
 * Data Access Object for Driver entity.
 * Provides queries for retrieving and filtering drivers by various criteria.
 */
public class DriverDao {

    /**
     * Retrieves all drivers from the database.
     *
     * @return list of all drivers
     */
    public static List<Driver> getAllDrivers() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT d FROM Driver d", Driver.class)
                    .getResultList();
        }
    }

    /**
     * Retrieves all drivers sorted by salary in ascending order.
     *
     * @return list of drivers sorted by salary (lowest to highest)
     */
    public static List<Driver> getDriversSortedBySalaryAsc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT d FROM Driver d ORDER BY d.salary ASC", Driver.class)
                    .getResultList();
        }
    }

    /**
     * Retrieves all drivers who possess a specific qualification.
     *
     * @param qualification the qualification to filter by
     * @return list of drivers with the specified qualification
     */
    public static List<Driver> getDriversWithQualification(DriverQualification qualification) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT DISTINCT d FROM Driver d JOIN d.qualifications q WHERE q = :q",
                            Driver.class)
                    .setParameter("q", qualification)
                    .getResultList();
        }
    }
}
