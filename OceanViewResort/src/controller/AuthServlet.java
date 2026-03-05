package controller;

import model.User;
import service.AuthService;
import util.PasswordUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Authentication (Login/Logout).
 * Implements Session Management to prevent unauthorized access.
 */
@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.authService = new AuthService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("logout".equals(action)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate(); // Clear session
            }
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?msg=logged_out");
        } else {
            // Default to login page
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("login".equals(action)) {
            handleLogin(req, resp);
        } else if ("register".equals(action)) {
            handleRegister(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=invalid_action");
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password"); // Plain text from form

        User user = authService.login(username, password);

        if (user != null) {
            // Establish Session
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedUser", user);

            // Redirect to Dashboard Controller (PRG Pattern)
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=invalid_credentials");
        }
    }

    // Simple registration helper for staff setup
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (authService.register(username, password, "STAFF")) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?msg=registered");
        } else {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=username_taken");
        }
    }
}
