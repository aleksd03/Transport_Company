package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Driver;
import org.informatics.entity.enums.DriverQualification;
import org.hibernate.Session;

import java.util.List;

public class DriverDao {
    public static List<Driver> getAllDrivers() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT d FROM Driver d", Driver.class)
                    .getResultList();
        }
    }

    public static List<Driver> getDriversSortedBySalaryAsc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT d FROM Driver d ORDER BY d.salary ASC", Driver.class)
                    .getResultList();
        }
    }

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
