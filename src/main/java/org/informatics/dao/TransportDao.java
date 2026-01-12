package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Transport;
import org.informatics.entity.enums.PaymentStatus;
import org.informatics.exception.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Data Access Object for Transport entity.
 * Provides CRUD operations and queries for all transport operations.
 */
public class TransportDao {

    /**
     * Creates and persists a new transport.
     *
     * @param transport the transport to create
     */
    public static void create(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(transport);
            transaction.commit();
        }
    }

    /**
     * Retrieves a transport by ID.
     *
     * @param id the transport ID
     * @return the transport, or null if not found
     */
    public static Transport get(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Transport.class, id);
        }
    }

    /**
     * Retrieves all transports from the database.
     *
     * @return list of all transports
     */
    public static List<Transport> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Transport t", Transport.class).getResultList();
        }
    }

    /**
     * Retrieves all transports sorted by destination alphabetically.
     *
     * @return list of transports sorted by destination
     */
    public static List<Transport> getAllSortedByDestination() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Transport t ORDER BY t.destination", Transport.class)
                    .getResultList();
        }
    }

    /**
     * Updates the payment status of a transport.
     *
     * @param transportId the transport ID
     * @param status the new payment status
     */
    public static void setPaymentStatus(long transportId, PaymentStatus status) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Transport transport = session.find(Transport.class, transportId);
            transport.setPaymentStatus(status);
            session.merge(transport);
            transaction.commit();
        }
    }

    /**
     * Deletes a transport by ID.
     *
     * @param id the transport ID
     * @throws EntityNotFoundException if transport doesn't exist
     */
    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Transport transport = session.find(Transport.class, id);
            if (transport == null) {
                throw new EntityNotFoundException("Transport", id);
            }
            session.remove(transport);
            transaction.commit();
        }
    }
}