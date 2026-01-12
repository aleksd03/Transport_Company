package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.dto.DriverPaidTransportCountDto;
import org.informatics.dto.DriverRevenueDto;
import org.informatics.dto.DriverTransportCountDto;
import org.informatics.entity.enums.PaymentStatus;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Access Object for generating various business reports.
 * Provides statistical queries about transports, revenue, and driver performance.
 */
public class ReportDao {

    /**
     * Returns the total number of all transports in the system.
     *
     * @return total count of transports
     */
    public static long getTotalTransportsCount() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(t) FROM Transport t", Long.class)
                    .getSingleResult();
        }
    }

    /**
     * Calculates total revenue from PAID transports only.
     * Unpaid transports are not included in the calculation.
     *
     * @return total revenue in BGN from paid transports
     */
    public static double getTotalTransportsRevenue() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Double result = session.createQuery(
                            "SELECT SUM(t.price) FROM Transport t WHERE t.paymentStatus = :paid",
                            Double.class)
                    .setParameter("paid", PaymentStatus.PAID)
                    .getSingleResult();
            return result == null ? 0.0 : result;
        }
    }

    /**
     * Calculates total value of ALL transports (both PAID and UNPAID).
     *
     * @return total value in BGN of all transports
     */
    public static double getTotalTransportsValue() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Double result = session.createQuery("SELECT SUM(t.price) FROM Transport t", Double.class)
                    .getSingleResult();
            return result == null ? 0.0 : result;
        }
    }

    /**
     * Returns all drivers with the count of transports they have performed.
     * Results are sorted by transport count in descending order.
     *
     * @return list of DTOs containing driver info and transport count
     */
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

    /**
     * Returns all drivers with the count of PAID transports they have performed.
     * Results are sorted by paid transport count in descending order.
     *
     * @return list of DTOs containing driver info and paid transport count
     */
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

    /**
     * Calculates revenue for a specific time period (PAID transports only).
     * Both start and end dates are inclusive.
     *
     * @param from start date (inclusive)
     * @param to end date (inclusive)
     * @return total revenue in BGN for the specified period
     */
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

    /**
     * Returns revenue generated by each driver (PAID transports only).
     * Results are sorted by revenue in descending order.
     *
     * @return list of DTOs containing driver info and their total revenue
     */
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
}
