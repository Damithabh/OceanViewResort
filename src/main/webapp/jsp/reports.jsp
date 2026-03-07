<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Monthly Reports | Ocean View Resort</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        </head>

        <body>

            <jsp:include page="/jsp/components/sidebar.jsp">
                <jsp:param name="activePage" value="reports" />
            </jsp:include>

            <div class="content-area animate-fade-in">
                <div class="d-flex align-items-center justify-content-between mb-3">
                    <div>
                        <h2 class="fw-bold" style="color: var(--primary-color);">
                            <i class="fa-solid fa-chart-bar me-2"></i>Monthly Reports
                        </h2>
                        <p class="text-muted mb-0">Revenue overview and booking summaries.</p>
                    </div>
                    <button class="btn btn-outline-antigravity" onclick="window.print();">
                        <i class="fa-solid fa-print me-2"></i>Print Report
                    </button>
                </div>

                <!-- Monthly Revenue Summary -->
                <div class="card p-4 mb-4">
                    <h5 class="fw-bold mb-3"><i class="fa-solid fa-calendar-alt me-2 text-primary"></i>Monthly Revenue
                        Summary</h5>
                    <c:choose>
                        <c:when test="${empty monthlySummary}">
                            <div class="text-center py-4 text-muted">
                                <i class="fa-solid fa-inbox fa-2x mb-2 d-block"></i>
                                No reservation data available yet.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover" id="monthlyTable">
                                    <thead>
                                        <tr>
                                            <th>Month</th>
                                            <th>Total Bookings</th>
                                            <th>Total Revenue</th>
                                            <th>Avg. per Booking</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${monthlySummary}" var="row">
                                            <tr>
                                                <td class="fw-bold">${row.month}</td>
                                                <td><span class="badge bg-primary">${row.booking_count}</span></td>
                                                <td class="fw-bold text-success">$${row.total_revenue}</td>
                                                <td class="text-muted">
                                                    <c:if test="${row.booking_count > 0}">
                                                        $
                                                        <fmt:formatNumber
                                                            value="${row.total_revenue / row.booking_count}"
                                                            pattern="#,##0.00" />
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- All Reservations with Receipt Links -->
                <div class="card p-4">
                    <h5 class="fw-bold mb-3"><i class="fa-solid fa-receipt me-2 text-primary"></i>All Bookings — Payment
                        Receipts</h5>
                    <c:choose>
                        <c:when test="${empty allReservations}">
                            <div class="text-center py-4 text-muted">
                                <i class="fa-solid fa-inbox fa-2x mb-2 d-block"></i>
                                No reservations found.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Reservation #</th>
                                            <th>Guest</th>
                                            <th>Room</th>
                                            <th>Check-in</th>
                                            <th>Check-out</th>
                                            <th>Amount</th>
                                            <th>Status</th>
                                            <th class="text-end">Receipt</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${allReservations}" var="res">
                                            <tr>
                                                <td class="fw-bold">${res.reservationNumber}</td>
                                                <td>${res.guestName}</td>
                                                <td>${res.roomNumber} (${res.roomType})</td>
                                                <td>${res.checkIn}</td>
                                                <td>${res.checkOut}</td>
                                                <td class="fw-bold">$${res.totalAmount}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${res.status == 'CONFIRMED'}"><span
                                                                class="badge bg-success">CONFIRMED</span></c:when>
                                                        <c:when test="${res.status == 'CANCELLED'}"><span
                                                                class="badge bg-secondary">CANCELLED</span></c:when>
                                                        <c:otherwise><span
                                                                class="badge bg-info text-dark">${res.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-end">
                                                    <a href="${pageContext.request.contextPath}/reports?action=receipt&id=${res.id}"
                                                        class="btn btn-sm btn-outline-primary" title="View Receipt">
                                                        <i class="fa-solid fa-file-invoice-dollar"></i>
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>