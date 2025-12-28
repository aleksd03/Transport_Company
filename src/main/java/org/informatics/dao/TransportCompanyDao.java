package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.dto.CompanyRevenueDto;
import org.informatics.entity.TransportCompany;
import org.informatics.entity.enums.PaymentStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class TransportCompanyDao {

    public static void create(TransportCompany transportCompany) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(transportCompany);
            transaction.commit();
        }
    }

    public static TransportCompany get(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(TransportCompany.class, id);
        }
    }

    public static List<TransportCompany> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM TransportCompany c", TransportCompany.class)
                    .getResultList();
        }
    }

    public static void updateName(long id, String newName) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            TransportCompany transportCompany = session.find(TransportCompany.class, id);
            transportCompany.setName(newName);
            session.persist(transportCompany);
            transaction.commit();
        }
    }

    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            TransportCompany transportCompany = session.find(TransportCompany.class, id);
            session.remove(transportCompany);
            transaction.commit();
        }
    }

    public static java.util.List<TransportCompany> getAllSortedByName() {
        try (org.hibernate.Session session = org.informatics.configuration.SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM TransportCompany c ORDER BY c.name", TransportCompany.class)
                    .getResultList();
        }
    }

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
