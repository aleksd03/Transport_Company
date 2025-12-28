package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.dto.DriverPaidTransportCountDto;
import org.informatics.dto.DriverRevenueDto;
import org.informatics.dto.DriverTransportCountDto;
import org.informatics.entity.enums.PaymentStatus;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

public class ReportDao {
    // 9a) total number of transports
    public static long getTotalTransportsCount() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(t) FROM Transport t", Long.class)
                    .getSingleResult();
        }
    }

    // 9b) total price of transports
    public static double getTotalTransportsRevenue() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Double result = session.createQuery("SELECT SUM(t.price) FROM Transport t WHERE t.paymentStatus = :paid", Double.class)
                    .setParameter("paid", PaymentStatus.PAID)
                    .getSingleResult();
            return result == null ? 0.0 : result;
        }
    }

    // 9c) all drivers with number of transports
    public static List<DriverTransportCountDto> getDriversWithTransportsCount() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT new org.informatics.dto.DriverTransportCountDto(d.id, d.firstName, d.lastName, COUNT(t)) " +
                                    "FROM Driver d LEFT JOIN Transport t ON t.driver.id = d.id " +
                                    "GROUP BY d.id, d.firstName, d.lastName " +
                                    "ORDER BY COUNT(t) DESC",
                            DriverTransportCountDto.class)
                    .getResultList();
        }
    }

    // 9d) revenue for a period (inclusive)
    public static double getRevenueForPeriod(LocalDate from, LocalDate to) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Double result = session.createQuery(
                            "SELECT SUM(t.price) FROM Transport t " +
                                    "WHERE t.transportDate BETWEEN :from AND :to " +
                                    "AND t.paymentStatus = :paid",
                            Double.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .setParameter("paid", PaymentStatus.PAID)
                    .getSingleResult();
            return result == null ? 0.0 : result;
        }
    }

    // 9e) revenue for each driver
    public static List<DriverRevenueDto> getRevenueByDriver() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT new org.informatics.dto.DriverRevenueDto(d.id, d.firstName, d.lastName, " +
                                    "COALESCE(SUM(CASE WHEN t.paymentStatus = :paid THEN t.price ELSE 0 END), 0)) " +
                                    "FROM Driver d LEFT JOIN Transport t ON t.driver.id = d.id " +
                                    "GROUP BY d.id, d.firstName, d.lastName " +
                                    "ORDER BY COALESCE(SUM(CASE WHEN t.paymentStatus = :paid THEN t.price ELSE 0 END), 0) DESC",
                            DriverRevenueDto.class)
                    .setParameter("paid", PaymentStatus.PAID)
                    .getResultList();
        }
    }

    public static double getTotalTransportsValue() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Double result = session.createQuery("SELECT SUM(t.price) FROM Transport t", Double.class)
                    .getSingleResult();
            return result == null ? 0.0 : result;
        }
    }

    public static List<DriverPaidTransportCountDto> getDriversWithPaidTransportsCount() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT new org.informatics.dto.DriverPaidTransportCountDto(d.id, d.firstName, d.lastName, " +
                                    "COALESCE(SUM(CASE WHEN t.paymentStatus = :paid THEN 1 ELSE 0 END), 0)) " +
                                    "FROM Driver d LEFT JOIN Transport t ON t.driver.id = d.id " +
                                    "GROUP BY d.id, d.firstName, d.lastName " +
                                    "ORDER BY COALESCE(SUM(CASE WHEN t.paymentStatus = :paid THEN 1 ELSE 0 END), 0) DESC",
                            DriverPaidTransportCountDto.class)
                    .setParameter("paid", PaymentStatus.PAID)
                    .getResultList();
        }
    }
}
