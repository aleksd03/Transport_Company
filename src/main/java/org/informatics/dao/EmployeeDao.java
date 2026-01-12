package org.informatics.dao;

import org.informatics.configuration.SessionFactoryUtil;
import org.informatics.entity.Employee;
import org.informatics.exception.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Data Access Object for Employee entity.
 * Provides CRUD operations for all employees (including drivers).
 */
public class EmployeeDao {

    /**
     * Creates and persists a new employee.
     *
     * @param employee the employee to create
     */
    public static void create(Employee employee) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(employee);
            transaction.commit();
        }
    }

    /**
     * Retrieves all employees from the database.
     *
     * @return list of all employees
     */
    public static List<Employee> getAll() {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT e FROM Employee e", Employee.class)
                    .getResultList();
        }
    }

    /**
     * Deletes an employee by ID.
     *
     * @param id the employee ID
     * @throws EntityNotFoundException if employee doesn't exist
     */
    public static void delete(long id) {
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Employee employee = session.find(Employee.class, id);
            if (employee == null) {
                throw new EntityNotFoundException("Employee", id);
            }
            session.remove(employee);
            transaction.commit();
        }
    }
}
