package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.dto.CompanyRevenueDto;
import org.informatics.entity.TransportCompany;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.exception.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Data Access Object for TransportCompany entity.
 * Provides CRUD operations and queries for transport companies.
 */
public class TransportCompanyDao {

    /**
     * Creates and persists a new transport company.
     *
     * @param transportCompany the company to create
     */
    public static void create(TransportCompany transportCompany) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(transportCompany);
            transaction.commit();
        }
    }

    /**
     * Retrieves a transport company by ID.
     *
     * @param id the company ID
     * @return the company, or null if not found
     */
    public static TransportCompany get(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(TransportCompany.class, id);
        }
    }

    /**
     * Retrieves all transport companies.
     *
     * @return list of all companies
     */
    public static List<TransportCompany> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM TransportCompany c", TransportCompany.class)
                    .getResultList();
        }
    }

    /**
     * Updates the name of a transport company.
     *
     * @param id the company ID
     * @param newName the new company name
     */
    public static void updateName(long id, String newName) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            TransportCompany transportCompany = session.find(TransportCompany.class, id);
            transportCompany.setName(newName);
            session.merge(transportCompany);
            transaction.commit();
        }
    }

    /**
     * Deletes a transport company by ID.
     *
     * @param id the company ID
     * @throws EntityNotFoundException if company doesn't exist
     */
    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            TransportCompany transportCompany = session.find(TransportCompany.class, id);
            if (transportCompany == null) {
                throw new EntityNotFoundException("TransportCompany", id);
            }
            session.remove(transportCompany);
            transaction.commit();
        }
    }

    /**
     * Retrieves all companies sorted by name alphabetically.
     *
     * @return list of companies sorted by name
     */
    public static List<TransportCompany> getAllSortedByName() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM TransportCompany c ORDER BY c.name", TransportCompany.class)
                    .getResultList();
        }
    }

    /**
     * Retrieves all companies with their revenue (from PAID transports only), sorted descending.
     *
     * @return list of DTOs containing company ID, name, and revenue
     */
    public static List<CompanyRevenueDto> getAllSortedByRevenueDesc() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT new org.informatics.dto.CompanyRevenueDto(c.id, c.name, " +
                                    "COALESCE(SUM(CASE WHEN t.paymentStatus = :paid THEN t.price ELSE 0 END), 0)) " +
                                    "FROM TransportCompany c LEFT JOIN Transport t ON t.company.id = c.id " +
                                    "GROUP BY c.id, c.name " +
                                    "ORDER BY COALESCE(SUM(CASE WHEN t.paymentStatus = :paid THEN t.price ELSE 0 END), 0) DESC",
                            CompanyRevenueDto.class)
                    .setParameter("paid", PaymentStatus.PAID)
                    .getResultList();
        }
    }
}
