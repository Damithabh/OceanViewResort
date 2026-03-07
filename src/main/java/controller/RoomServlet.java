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
import java.math.BigDecimal;
import java.util.List;

/**
 * Controller for Admin Room Management.
 * Provides full CRUD operations for rooms.
 * Only accessible by users with ADMIN role.
 * 
 * @author Ocean View Resort Dev Team
 */
@WebServlet("/manage-rooms")
public class RoomServlet extends HttpServlet {

    private RoomService roomService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.roomService = new RoomService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        // Admin-only access check
        User user = (User) session.getAttribute("loggedUser");
        if (!user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=admin_only");
            return;
        }

        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(req.getParameter("id"));
                roomService.deleteRoom(id);
                resp.sendRedirect(req.getContextPath() + "/manage-rooms?msg=deleted");
            } catch (Exception e) {
                resp.sendRedirect(req.getContextPath() + "/manage-rooms?error=delete_failed");
            }
        } else {
            // Default: List all rooms with optional filter
            String filterType = req.getParameter("filterType");
            String filterStatus = req.getParameter("filterStatus");

            List<Room> rooms;
            if ((filterType != null && !filterType.isEmpty())
                    || (filterStatus != null && !filterStatus.isEmpty())) {
                rooms = roomService.searchRooms(filterType, filterStatus);
                req.setAttribute("filterType", filterType);
                req.setAttribute("filterStatus", filterStatus);
            } else {
                rooms = roomService.getAllRooms();
            }

            req.setAttribute("rooms", rooms);
            req.getRequestDispatcher("/jsp/room-management.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        User user = (User) session.getAttribute("loggedUser");
        if (!user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/dashboard?error=admin_only");
            return;
        }

        String action = req.getParameter("action");

        if ("create".equals(action)) {
            handleCreateRoom(req, resp);
        } else if ("update".equals(action)) {
            handleUpdateRoom(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/manage-rooms?error=invalid_action");
        }
    }

    /**
     * Creates a new room using the RoomFactory via RoomService.
     */
    private void handleCreateRoom(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String roomNumber = req.getParameter("roomNumber");
        String roomType = req.getParameter("roomType");
        String priceStr = req.getParameter("pricePerNight");
        String description = req.getParameter("description");

        try {
            BigDecimal customPrice = (priceStr != null && !priceStr.isEmpty())
                    ? new BigDecimal(priceStr)
                    : null;

            boolean success = roomService.createRoom(roomNumber, roomType, customPrice, description);
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/manage-rooms?msg=created");
            } else {
                resp.sendRedirect(req.getContextPath() + "/manage-rooms?error=create_failed");
            }
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/manage-rooms?error=invalid_data");
        }
    }

    /**
     * Updates an existing room's properties.
     */
    private void handleUpdateRoom(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("roomId"));
            Room room = roomService.getRoomById(id);
            if (room == null) {
                resp.sendRedirect(req.getContextPath() + "/manage-rooms?error=not_found");
                return;
            }

            room.setRoomNumber(req.getParameter("roomNumber"));
            room.setRoomType(req.getParameter("roomType"));
            room.setStatus(req.getParameter("status"));
            room.setDescription(req.getParameter("description"));

            String priceStr = req.getParameter("pricePerNight");
            if (priceStr != null && !priceStr.isEmpty()) {
                room.setPricePerNight(new BigDecimal(priceStr));
            }

            boolean success = roomService.updateRoom(room);
            if (success) {
                resp.sendRedirect(req.getContextPath() + "/manage-rooms?msg=updated");
            } else {
                resp.sendRedirect(req.getContextPath() + "/manage-rooms?error=update_failed");
            }
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/manage-rooms?error=invalid_data");
        }
    }
}
