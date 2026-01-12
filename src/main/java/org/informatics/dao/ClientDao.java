package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Client;
import org.informatics.exception.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Data Access Object for Client entity.
 * Provides CRUD operations for clients who request transport services.
 */
public class ClientDao {

    /**
     * Creates and persists a new client.
     *
     * @param client the client to create
     */
    public static void create(Client client) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(client);
            transaction.commit();
        }
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return list of all clients
     */
    public static List<Client> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT c FROM Client c", Client.class)
                    .getResultList();
        }
    }

    /**
     * Deletes a client by ID.
     *
     * @param id the client ID
     * @throws EntityNotFoundException if client doesn't exist
     */
    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Client client = session.find(Client.class, id);
            if (client == null) {
                throw new EntityNotFoundException("Client", id);
            }
            session.remove(client);
            transaction.commit();
        }
    }

    /**
     * Retrieves a client by ID.
     *
     * @param id the client ID
     * @return the client, or null if not found
     */
    public static Client get(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Client.class, id);
        }
    }
}
