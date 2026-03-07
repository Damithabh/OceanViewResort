package dao;

import model.User;
import util.AppLogger;
import util.DBConnectionPool;
import exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Concrete DAO implementation for User entity.
 * Handles all CRUD operations and user lookup by username.
 * Uses PreparedStatement for SQL injection prevention.
 * 
 * @author Ocean View Resort Dev Team
 */
public class UserDAOImpl implements GenericDAO<User> {

    private static final Logger LOGGER = AppLogger.getLogger(UserDAOImpl.class);

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserDAO.findById failed for id=" + id, e);
            throw new DataAccessException("Failed to find user by ID: " + id, e);
        }
        return null;
    }

    /**
     * Finds a user by their unique username.
     * Used primarily during authentication.
     *
     * @param username The username to search for
     * @return The User if found, null otherwise
     */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserDAO.findByUsername failed for username=" + username, e);
            throw new DataAccessException("Failed to find user by username: " + username, e);
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserDAO.findAll failed.", e);
            throw new DataAccessException("Failed to retrieve all users.", e);
        }
        return users;
    }

    @Override
    public boolean create(User user) {
        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                    }
                }
                LOGGER.info("User created: " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserDAO.create failed for username=" + user.getUsername(), e);
            throw new DataAccessException("Failed to create user: " + user.getUsername(), e);
        }
        return false;
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, password_hash = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole());
            stmt.setInt(4, user.getId());

            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                LOGGER.info("User updated: id=" + user.getId());
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserDAO.update failed for id=" + user.getId(), e);
            throw new DataAccessException("Failed to update user: " + user.getId(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBConnectionPool.getInstance().getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            boolean success = stmt.executeUpdate() > 0;
            if (success) {
                LOGGER.info("User deleted: id=" + id);
            }
            return success;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "UserDAO.delete failed for id=" + id, e);
            throw new DataAccessException("Failed to delete user: " + id, e);
        }
    }

    /**
     * Extracts a User instance from a ResultSet row.
     *
     * @param rs The current ResultSet row
     * @return A fully populated User object
     * @throws SQLException If column access fails
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getString("role"),
                rs.getTimestamp("created_at"));
    }
}
