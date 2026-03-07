package service;

import dao.UserDAOImpl;
import model.User;
import util.AppLogger;
import util.PasswordUtils;

import java.util.logging.Logger;

/**
 * Service Layer for Authentication — encapsulates login and registration logic.
 * Separates business rules from controller/servlet concerns.
 * 
 * // Learned from https://www.baeldung.com/java-dao-pattern
 * 
 * @author Ocean View Resort Dev Team
 */
public class AuthService {

    private static final Logger LOGGER = AppLogger.getLogger(AuthService.class);
    private final UserDAOImpl userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Authenticates a user by verifying plain-text password against stored hash.
     *
     * @param username          The login username
     * @param plainTextPassword The raw password from the login form
     * @return The authenticated User object, or null if authentication fails
     */
    public User login(String username, String plainTextPassword) {
        if (username == null || plainTextPassword == null)
            return null;

        User user = userDAO.findByUsername(username.trim());

        if (user != null) {
            if (PasswordUtils.verifyPassword(plainTextPassword, user.getPasswordHash())) {
                LOGGER.info("Successful login: " + username);
                return user;
            }
            LOGGER.warning("Failed login attempt for username: " + username);
        }
        return null;
    }

    /**
     * Registers a new user account with hashed password.
     *
     * @param username    Desired username
     * @param rawPassword Plain-text password to be hashed
     * @param role        User role (ADMIN or STAFF)
     * @return true if registration succeeded
     */
    public boolean register(String username, String rawPassword, String role) {
        if (username == null || rawPassword == null)
            return false;

        // Prevent duplicate usernames
        if (userDAO.findByUsername(username.trim()) != null) {
            LOGGER.warning("Registration failed — username already taken: " + username);
            return false;
        }

        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setPasswordHash(PasswordUtils.hashPassword(rawPassword));
        newUser.setRole(role != null ? role.toUpperCase() : "STAFF");

        boolean created = userDAO.create(newUser);
        if (created) {
            LOGGER.info("New user registered: " + username + " (" + newUser.getRole() + ")");
        }
        return created;
    }
}
