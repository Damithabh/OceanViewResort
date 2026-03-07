package dao;

import java.util.List;

/**
 * Generic Data Access Object interface defining standard CRUD operations.
 * All entity-specific DAOs implement this contract to ensure consistency.
 * 
 * Why Generic: Eliminates code duplication across DAO implementations
 * and enforces a uniform persistence API across the data access layer.
 * 
 * // Learned from https://www.baeldung.com/java-dao-pattern
 * 
 * @param <T> The entity type this DAO manages
 * @author Ocean View Resort Dev Team
 */
public interface GenericDAO<T> {

    /**
     * Retrieves an entity by its primary key.
     *
     * @param id The primary key identifier
     * @return The entity if found, null otherwise
     */
    T findById(int id);

    /**
     * Retrieves all entities of this type.
     *
     * @return A list of all entities (may be empty, never null)
     */
    List<T> findAll();

    /**
     * Persists a new entity to the database.
     *
     * @param entity The entity to create
     * @return true if the operation succeeded
     */
    boolean create(T entity);

    /**
     * Updates an existing entity in the database.
     *
     * @param entity The entity with updated fields
     * @return true if the operation succeeded
     */
    boolean update(T entity);

    /**
     * Deletes an entity by its primary key.
     *
     * @param id The primary key of the entity to delete
     * @return true if the operation succeeded
     */
    boolean delete(int id);
}
