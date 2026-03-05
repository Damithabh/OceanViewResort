package service;

import dao.UserDAOImpl;
import model.User;
import util.PasswordUtils;

/**
 * Service Layer Pattern
 * // Learned from https://www.baeldung.com/java-dao-pattern
 * Encapsulates authentication business logic away from the Servlet controllers.
 */
public class AuthService {

    private final UserDAOImpl userDAO;

    public AuthService() {
        this.userDAO = new UserDAOImpl();
    }

    /**
     * Authenticates a user by checking the plaintext password against the stored
     * SHA-256 hash.
     * 
     * @return User object if successful, null otherwise.
     */
    public User login(String username, String plainTextPassword) {
        User user = userDAO.findByUsername(username);

        if (user != null) {
            // Verify using the utility class (SHA-256)
            if (PasswordUtils.verifyPassword(plainTextPassword, user.getPasswordHash())) {
                return user;
            }
        }
        return null;
    }

    public boolean register(String username, String rawPassword, String role) {
        // Prevent duplicate usernames
        if (userDAO.findByUsername(username) != null) {
            return false;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(PasswordUtils.hashPassword(rawPassword));
        newUser.setRole(role != null ? role.toUpperCase() : "STAFF");

        return userDAO.create(newUser);
    }
}
