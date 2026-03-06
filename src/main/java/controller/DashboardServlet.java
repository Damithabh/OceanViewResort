package controller;

import model.Reservation;
import model.Room;
import model.User;
import service.ReservationService;
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
 * Controller for the Dashboard overview page.
 * Computes KPI statistics and forwards to the dashboard view.
 * 
 * @author Ocean View Resort Dev Team
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private RoomService roomService;
    private ReservationService reservationService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.roomService = new RoomService();
        this.reservationService = new ReservationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");

        // Fetch room data for KPI cards
        List<Room> allRooms = roomService.getAllRooms();
        long availableCount = allRooms.stream().filter(r -> "AVAILABLE".equals(r.getStatus())).count();
        long occupiedCount = allRooms.stream().filter(r -> "OCCUPIED".equals(r.getStatus())).count();
        long maintenanceCount = allRooms.stream().filter(r -> "MAINTENANCE".equals(r.getStatus())).count();

        // Fetch reservation count
        List<Reservation> allReservations = reservationService.getAllReservations();
        long activeReservations = allReservations.stream()
                .filter(r -> "CONFIRMED".equals(r.getStatus()) || "CHECKED_IN".equals(r.getStatus()))
                .count();

        // Set attributes for JSP Expression Language
        req.setAttribute("allRooms", allRooms);
        req.setAttribute("totalRooms", allRooms.size());
        req.setAttribute("availableCount", availableCount);
        req.setAttribute("occupiedCount", occupiedCount);
        req.setAttribute("maintenanceCount", maintenanceCount);
        req.setAttribute("activeReservations", activeReservations);
        req.setAttribute("isAdmin", user.isAdmin());

        req.getRequestDispatcher("/jsp/dashboard.jsp").forward(req, resp);
    }
}
