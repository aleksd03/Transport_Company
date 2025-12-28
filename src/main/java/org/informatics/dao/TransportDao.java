package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Transport;
import org.informatics.entity.enums.PaymentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TransportDao {
    public static void create(Transport transport) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(transport);
            transaction.commit();
        }
    }

    public static Transport get(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Transport.class, id);
        }
    }

    public static List<Transport> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Transport t", Transport.class).getResultList();
        }
    }

    public static List<Transport> getAllSortedByDestination() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT t FROM Transport t ORDER BY t.destination", Transport.class)
                    .getResultList();
        }
    }

    public static void setPaymentStatus(long transportId, PaymentStatus status) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Transport transport = session.find(Transport.class, transportId);
            transport.setPaymentStatus(status);
            session.persist(transport);
            transaction.commit();
        }
    }

    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Transport transport = session.find(Transport.class, id);
            session.remove(transport);
            transaction.commit();
        }
    }
}
