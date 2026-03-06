package controller;

import dao.AuditLogDAO;
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
import java.util.Map;

/**
 * Controller for Reports, Receipts, and Audit Log viewing.
 * 
 * Endpoints:
 * GET /reports?action=monthly → Monthly revenue summary
 * GET /reports?action=receipt&id= → Payment receipt for a single reservation
 * GET /reports?action=logs → Audit log viewer
 * 
 * @author Ocean View Resort Dev Team
 */
@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    private ReservationService reservationService;
    private RoomService roomService;
    private AuditLogDAO auditLogDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        this.reservationService = new ReservationService();
        this.roomService = new RoomService();
        this.auditLogDAO = new AuditLogDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        String action = req.getParameter("action");

        if ("receipt".equals(action)) {
            handleReceipt(req, resp);
        } else if ("logs".equals(action)) {
            handleAuditLogs(req, resp);
        } else {
            // Default: monthly report
            handleMonthlyReport(req, resp);
        }
    }

    /**
     * Generates a printable payment receipt for a single reservation.
     */
    private void handleReceipt(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Reservation reservation = reservationService.getReservationById(id);

            if (reservation == null) {
                resp.sendRedirect(req.getContextPath() + "/manage-reservations?error=not_found");
                return;
            }

            Room room = roomService.getRoomById(reservation.getRoomId());
            req.setAttribute("reservation", reservation);
            req.setAttribute("room", room);
            req.getRequestDispatcher("/jsp/receipt.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/manage-reservations?error=invalid_id");
        }
    }

    /**
     * Displays monthly reservation summary with revenue totals.
     */
    private void handleMonthlyReport(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Map<String, Object>> monthlySummary = auditLogDAO.getMonthlyReservationSummary();
        List<Reservation> allReservations = reservationService.getAllReservations();

        req.setAttribute("monthlySummary", monthlySummary);
        req.setAttribute("allReservations", allReservations);
        req.getRequestDispatcher("/jsp/reports.jsp").forward(req, resp);
    }

    /**
     * Displays the audit log with optional event type filtering.
     */
    private void handleAuditLogs(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String filterEvent = req.getParameter("filterEvent");
        String filterEntity = req.getParameter("filterEntity");

        List<Map<String, Object>> logs;
        if ((filterEvent != null && !filterEvent.isEmpty()) || (filterEntity != null && !filterEntity.isEmpty())) {
            logs = auditLogDAO.search(filterEntity, filterEvent);
            req.setAttribute("filterEvent", filterEvent);
            req.setAttribute("filterEntity", filterEntity);
        } else {
            logs = auditLogDAO.findAll();
        }

        req.setAttribute("auditLogs", logs);
        req.getRequestDispatcher("/jsp/audit-log.jsp").forward(req, resp);
    }
}
