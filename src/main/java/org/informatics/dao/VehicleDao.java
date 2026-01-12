package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Vehicle;
import org.informatics.exception.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Data Access Object for Vehicle entity.
 * Provides CRUD operations for all types of vehicles (buses, trucks, tankers).
 */
public class VehicleDao {

    /**
     * Creates and persists a new vehicle.
     *
     * @param vehicle the vehicle to create
     */
    public static void create(Vehicle vehicle) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(vehicle);
            transaction.commit();
        }
    }

    /**
     * Retrieves a vehicle by ID.
     *
     * @param id the vehicle ID
     * @return the vehicle, or null if not found
     */
    public static Vehicle get(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.find(Vehicle.class, id);
        }
    }

    /**
     * Retrieves all vehicles from the database.
     *
     * @return list of all vehicles
     */
    public static List<Vehicle> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
        }
    }

    /**
     * Deletes a vehicle by ID.
     *
     * @param id the vehicle ID
     * @throws EntityNotFoundException if vehicle doesn't exist
     */
    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Vehicle vehicle = session.find(Vehicle.class, id);
            if (vehicle == null) {
                throw new EntityNotFoundException("Vehicle", id);
            }
            session.remove(vehicle);
            transaction.commit();
        }
    }
}
