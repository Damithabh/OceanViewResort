package controller;

import model.User;
import service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for Authentication (Login, Logout, Registration).
 * Implements the Post/Redirect/Get (PRG) pattern for form submissions
 * and manages HTTP sessions for stateful authentication.
 * 
 * @author Ocean View Resort Dev Team
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
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?msg=logged_out");
        } else {
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

    /**
     * Authenticates user credentials and establishes an HTTP session.
     * Uses PRG pattern to prevent form resubmission on browser refresh.
     */
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = authService.login(username, password);

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedUser", user);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=invalid_credentials");
        }
    }

    /**
     * Registers a new staff account and redirects to login.
     */
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (authService.register(username, password, "STAFF")) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?msg=registered");
        } else {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=username_taken");
        }
    }
}
