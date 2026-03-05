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

@WebServlet("/manage-reservations")
public class ReservationServlet extends HttpServlet {

    private ReservationService reservationService;
    private RoomService roomService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.reservationService = new ReservationService();
        this.roomService = new RoomService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Strict Authorization check
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        String action = req.getParameter("action");

        if ("new".equals(action)) {
            // Forward to booking form
            List<Room> availableRooms = roomService.getAvailableRooms();
            req.setAttribute("availableRooms", availableRooms);
            req.getRequestDispatcher("/jsp/reservation-form.jsp").forward(req, resp);
        } else if ("delete".equals(action)) {
            // Delete request (usually should be a POST or DELETE REST method, but keeping
            // simple for web form link mapping)
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                reservationService.cancelReservation(id);
                resp.sendRedirect(req.getContextPath() + "/manage-reservations?msg=deleted");
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/manage-reservations?error=invalid_id");
            }
        } else {
            // Default: List all reservations
            List<Reservation> reservations = reservationService.getAllReservations();
            req.setAttribute("reservations", reservations);
            req.getRequestDispatcher("/jsp/reservations-list.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Strict Authorization check
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");

        String guestName = req.getParameter("guestName");

        // Input sanitization
        if (guestName != null) {
            guestName = guestName.replaceAll("[^a-zA-Z0-9 ]", ""); // simple sanitize to prevent basic XSS
        }

        String checkIn = req.getParameter("checkIn");
        String checkOut = req.getParameter("checkOut");
        String roomIdStr = req.getParameter("roomId");

        try {
            int roomId = Integer.parseInt(roomIdStr);
            boolean success = reservationService.bookRoom(user.getId(), roomId, guestName, checkIn, checkOut);

            if (success) {
                // Post/Redirect/Get pattern
                resp.sendRedirect(req.getContextPath() + "/manage-reservations?msg=created");
            } else {
                resp.sendRedirect(req.getContextPath() + "/manage-reservations?action=new&error=booking_failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/manage-reservations?action=new&error=invalid_data");
        }
    }
}
