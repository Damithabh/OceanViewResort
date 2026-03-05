<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <% if (session.getAttribute("loggedUser")==null) { response.sendRedirect(request.getContextPath()
            + "/jsp/login.jsp?error=unauthorized" ); return; } %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Manage Reservations | Ocean View</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
                <style>
                    .res-card {
                        transition: all 0.3s ease;
                        cursor: default;
                    }

                    .res-card:hover {
                        transform: translateY(-5px);
                        box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1) !important;
                    }
                </style>
            </head>

            <body>

                <!-- Sidebar Layout -->
                <div class="sidebar">
                    <h3 class="text-center mb-4 text-white pb-3 border-bottom border-light">
                        <i class="fa-solid fa-hotel me-2"></i>Ocean View
                    </h3>

                    <a href="${pageContext.request.contextPath}/dashboard">
                        <i class="fa-solid fa-chart-pie me-2"></i>Dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/manage-reservations" class="active">
                        <i class="fa-solid fa-calendar-check me-2"></i>Reservations
                    </a>
                    <a href="${pageContext.request.contextPath}/manage-reservations?action=new">
                        <i class="fa-solid fa-plus-circle me-2"></i>New Booking
                    </a>
                </div>

                <!-- Content -->
                <div class="content-area bg-light">
                    <div class="container-fluid animate-fade-in">
                        <div class="d-flex justify-content-between align-items-center mb-4 pt-3">
                            <h2 class="fw-bold" style="color: var(--primary-color);">Reservation Ledger</h2>
                            <a href="${pageContext.request.contextPath}/manage-reservations?action=new"
                                class="btn btn-antigravity">
                                <i class="fa-solid fa-plus me-2"></i>Create Reservation
                            </a>
                        </div>

                        <% String msg=request.getParameter("msg"); if ("created".equals(msg)) { out.print("<div
                            class='alert alert-success animate-fade-in'><i
                                class='fa-solid fa-check-circle me-2'></i>Reservation successfully configured via Stored
                            Procedures.
                    </div>");
                    }
                    if ("deleted".equals(msg)) {
                    out.print("<div class='alert alert-warning animate-fade-in'><i
                            class='fa-solid fa-trash me-2'></i>Reservation records annulled. Room status restored.</div>
                    ");
                    }
                    %>

                    <div class="row">
                        <c:choose>
                            <c:when test="${empty reservations}">
                                <div class="col-12 text-center py-5">
                                    <i class="fa-solid fa-calendar-xmark fa-4x text-muted mb-3 opacity-50"></i>
                                    <h4 class="text-muted">No reservations found in the system.</h4>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="res" items="${reservations}">
                                    <div class="col-md-6 col-lg-4 mb-4">
                                        <div class="card shadow-sm border-0 h-100 res-card"
                                            style="border-radius: 12px; border-top: 4px solid var(--secondary-color) !important;">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between border-bottom pb-2 mb-3">
                                                    <span class="text-muted small fw-bold">ID:
                                                        ${res.reservationNumber}</span>
                                                    <span
                                                        class="badge bg-success-subtle text-success border border-success-subtle">
                                                        <i class="fa-solid fa-check me-1"></i>${res.status}
                                                    </span>
                                                </div>

                                                <h5 class="fw-bold text-dark mb-1"><i
                                                        class="fa-regular fa-user me-2 text-primary"></i>${res.guestName}
                                                </h5>
                                                <p class="text-muted small mb-3">Room Assignment:
                                                    <strong>RM-${res.roomId}</strong>
                                                </p>

                                                <div class="bg-light rounded p-3 mb-3">
                                                    <div class="row text-center">
                                                        <div class="col-5 border-end">
                                                            <span
                                                                class="d-block small text-muted text-uppercase mb-1">Check
                                                                In</span>
                                                            <span class="fw-bold text-dark">${res.checkIn}</span>
                                                        </div>
                                                        <div
                                                            class="col-2 d-flex align-items-center justify-content-center text-muted">
                                                            <i class="fa-solid fa-arrow-right"></i>
                                                        </div>
                                                        <div class="col-5">
                                                            <span
                                                                class="d-block small text-muted text-uppercase mb-1">Check
                                                                Out</span>
                                                            <span class="fw-bold text-dark">${res.checkOut}</span>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="d-flex justify-content-between align-items-center mt-auto">
                                                    <h4 class="mb-0 fw-bold" style="color: var(--primary-color);">$
                                                        <c:out value="${res.totalAmount}" />
                                                    </h4>

                                                    <!-- Simple Delete via Controller action parsing -->
                                                    <a href="${pageContext.request.contextPath}/manage-reservations?action=delete&id=${res.id}"
                                                        class="btn btn-outline-danger btn-sm"
                                                        onclick="return confirm('Are you sure you want to permanently cancel this reservation? This will free the assigned room immediately.')">
                                                        <i class="fa-solid fa-trash-alt me-1"></i>Cancel
                                                    </a>
                                                </div>
                                            </div>
                                            <div
                                                class="card-footer bg-white border-0 text-muted small text-center pt-0">
                                                Logged at: ${res.createdAt}
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>