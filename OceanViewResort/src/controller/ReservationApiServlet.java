package controller;

import com.google.gson.Gson;
import model.Reservation;
import service.ReservationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * RESTful Web Service Controller for Reservations.
 * Outputs ONLY JSON responses to satisfy the Distributed Architecture
 * Requirement.
 */
@WebServlet("/api/reservations/*")
public class ReservationApiServlet extends HttpServlet {

    private ReservationService reservationService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        this.reservationService = new ReservationService();
        this.gson = new Gson();
    }

    /**
     * GET /api/reservations (All)
     * GET /api/reservations/{id} (Single)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
                // Return all reservations
                List<Reservation> allReservations = reservationService.getAllReservations();
                out.print(gson.toJson(allReservations));
            } else {
                // Return single reservation based on ID
                String[] splits = pathInfo.split("/");
                if (splits.length >= 2) {
                    int id = Integer.parseInt(splits[1]);
                    Reservation res = reservationService.getReservationById(id);
                    if (res != null) {
                        out.print(gson.toJson(res));
                    } else {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.print("{\"error\": \"Reservation not found\"}");
                    }
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"error\": \"Invalid ID format\"}");
                }
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID must be an integer\"}");
        }
        out.flush();
    }

    /**
     * POST /api/reservations
     * Expects JSON Body: {"userId": 1, "roomId": 1, "guestName": "John", "checkIn":
     * "2026-04-01", "checkOut": "2026-04-05"}
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse JSON manually or cast to Map
            Map<String, Object> body = gson.fromJson(sb.toString(), Map.class);

            // Handle different number parsing behaviors of Gson to prevent
            // ClassCastException
            int userId = body.get("userId") instanceof Number ? ((Number) body.get("userId")).intValue()
                    : Integer.parseInt(String.valueOf(body.get("userId")));
            int roomId = body.get("roomId") instanceof Number ? ((Number) body.get("roomId")).intValue()
                    : Integer.parseInt(String.valueOf(body.get("roomId")));
            String guestName = String.valueOf(body.get("guestName"));
            String checkIn = String.valueOf(body.get("checkIn"));
            String checkOut = String.valueOf(body.get("checkOut"));

            boolean success = reservationService.bookRoom(userId, roomId, guestName, checkIn, checkOut);

            if (success) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"message\": \"Reservation created successfully\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Failed to create reservation. Room may be unavailable or dates invalid.\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Malformed JSON or internal server error\"}");
            e.printStackTrace();
        }
        out.flush();
    }
}
