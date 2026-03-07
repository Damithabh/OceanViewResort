package model;

import java.sql.Timestamp;

/**
 * User model representing a staff or admin account in the resort system.
 * Maps directly to the 'users' database table.
 * 
 * @author Ocean View Resort Dev Team
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String role; // ADMIN, STAFF
    private Timestamp createdAt;

    /** Default no-arg constructor for POJO compliance. */
    public User() {
    }

    /**
     * Full constructor for creating a User from database results.
     *
     * @param id           The unique user identifier
     * @param username     The login username
     * @param passwordHash SHA-256 hashed password
     * @param role         User role (ADMIN or STAFF)
     * @param createdAt    Account creation timestamp
     */
    public User(int id, String username, String passwordHash, String role, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Checks whether this user has administrator privileges.
     *
     * @return true if the user role is ADMIN
     */
    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.role);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', role='" + role + "'}";
    }
}
