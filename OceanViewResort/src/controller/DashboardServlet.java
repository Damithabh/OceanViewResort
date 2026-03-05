package controller;

import model.Room;
import model.User;
import service.RoomService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the Dashboard.
 * Ensures the user is logged in before rendering the dashboard.jsp
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private RoomService roomService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.roomService = new RoomService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DEBUG: DashboardServlet.doGet called for URI: " + req.getRequestURI());
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            // Unauthorized access attempt
            System.err.println("❌ Unauthorized access attempt to /dashboard");
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        System.out.println("DEBUG: User logged in: " + user.getUsername());

        // Fetch data for the dashboard (e.g. rooms list)
        List<Room> allRooms = roomService.getAllRooms();
        System.out.println("DEBUG: allRooms count: " + allRooms.size());

        // Compute simple stats for the Chart.js display
        long availableCount = allRooms.stream().filter(r -> "AVAILABLE".equals(r.getStatus())).count();
        long occupiedCount = allRooms.size() - availableCount;

        // Set contextual attributes to be used by JSTL/Expression Language
        req.setAttribute("allRooms", allRooms);
        req.setAttribute("availableCount", availableCount);
        req.setAttribute("occupiedCount", occupiedCount);
        req.setAttribute("isAdmin", user.isAdmin());

        // Forward to the View layer
        String forwardPath = "/jsp/dashboard.jsp";
        System.out.println("DEBUG: Forwarding to: " + forwardPath);
        req.getRequestDispatcher(forwardPath).forward(req, resp);
        System.out.println("DEBUG: Forwarding complete.");
    }
}
