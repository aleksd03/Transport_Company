package org.informatics.exception;

/**
 * Thrown when attempting to access or delete an entity that doesn't exist in the database.
 */
public class EntityNotFoundException extends AppException {
    public EntityNotFoundException(String entityType, long id) {
        super(String.format("%s with ID %d not found.", entityType, id));
    }
}