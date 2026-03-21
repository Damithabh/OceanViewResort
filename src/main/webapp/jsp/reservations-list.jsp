<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Reservations | Ocean View Resort</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        </head>

        <body>

            <jsp:include page="/jsp/components/sidebar.jsp">
                <jsp:param name="activePage" value="reservations" />
            </jsp:include>

            <div class="content-area animate-fade-in">
                <div class="d-flex align-items-center justify-content-between mb-3">
                    <div>
                        <h2 class="fw-bold" style="color: var(--primary-color);">
                            <i class="fa-solid fa-calendar-check me-2"></i>Reservations
                        </h2>
                        <p class="text-muted mb-0">Manage all resort bookings.</p>
                    </div>
                    <a href="${pageContext.request.contextPath}/manage-reservations?action=new"
                        class="btn btn-antigravity">
                        <i class="fa-solid fa-plus me-2"></i>New Booking
                    </a>
                </div>

                <%-- Success/Warning Messages --%>
                    <c:if test="${param.msg == 'created'}">
                        <div class="alert alert-success py-2"><i class="fa-solid fa-check-circle me-2"></i>Reservation
                            created successfully!</div>
                    </c:if>
                    <c:if test="${param.msg == 'deleted'}">
                        <div class="alert alert-warning py-2"><i class="fa-solid fa-info-circle me-2"></i>Reservation
                            cancelled. Room has been freed.</div>
                    </c:if>

                    <!-- Search / Filter Bar -->
                    <div class="card p-3 mb-4">
                        <form action="${pageContext.request.contextPath}/manage-reservations" method="GET"
                            class="row g-2 align-items-end">
                            <div class="col-md-4">
                                <label class="form-label small fw-bold">Guest Name</label>
                                <input type="text" class="form-control" name="searchGuest"
                                    placeholder="Search by guest name..." value="${searchGuest}">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-bold">Status</label>
                                <select class="form-select" name="searchStatus">
                                    <option value="">All Statuses</option>
                                    <option value="CONFIRMED" ${searchStatus=='CONFIRMED' ? 'selected' : '' }>Confirmed
                                    </option>
                                    <option value="CANCELLED" ${searchStatus=='CANCELLED' ? 'selected' : '' }>Cancelled
                                    </option>
                                    <option value="CHECKED_IN" ${searchStatus=='CHECKED_IN' ? 'selected' : '' }>Checked
                                        In</option>
                                    <option value="CHECKED_OUT" ${searchStatus=='CHECKED_OUT' ? 'selected' : '' }>
                                        Checked Out</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-outline-antigravity w-100">
                                    <i class="fa-solid fa-search me-1"></i>Search
                                </button>
                            </div>
                            <div class="col-md-2">
                                <a href="${pageContext.request.contextPath}/manage-reservations"
                                    class="btn btn-outline-secondary w-100">
                                    <i class="fa-solid fa-rotate-left me-1"></i>Reset
                                </a>
                            </div>
                        </form>
                    </div>

                    <!-- Reservation Cards -->
                    <c:choose>
                        <c:when test="${empty reservations}">
                            <div class="text-center py-5">
                                <i class="fa-solid fa-inbox fa-3x text-muted mb-3 d-block"></i>
                                <h5 class="text-muted">No reservations found.</h5>
                                <a href="${pageContext.request.contextPath}/manage-reservations?action=new"
                                    class="btn btn-antigravity mt-3">
                                    Create First Booking
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row g-3">
                                <c:forEach items="${reservations}" var="res">
                                    <div class="col-lg-6">
                                        <div class="card res-card p-3">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <div>
                                                    <span class="badge bg-primary mb-2">${res.reservationNumber}</span>
                                                    <h5 class="fw-bold mb-0">${res.guestName}</h5>
                                                    <div class="text-muted small">
                                                        <i class="fa-solid fa-envelope me-1"></i>${res.guestEmail}
                                                    </div>
                                                </div>
                                                <c:choose>
                                                    <c:when test="${res.status == 'CONFIRMED'}">
                                                        <span class="badge bg-success">CONFIRMED</span>
                                                    </c:when>
                                                    <c:when test="${res.status == 'CANCELLED'}">
                                                        <span class="badge bg-secondary">CANCELLED</span>
                                                    </c:when>
                                                    <c:when test="${res.status == 'CHECKED_IN'}">
                                                        <span class="badge bg-info text-dark">CHECKED IN</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-dark">CHECKED OUT</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="row text-muted small mb-2">
                                                <div class="col-6">
                                                    <i class="fa-solid fa-door-open me-1"></i>Room ${res.roomNumber}
                                                    (${res.roomType})
                                                </div>
                                                <div class="col-6 text-end">
                                                    <i class="fa-solid fa-user-tie me-1"></i>Booked by: ${res.bookedBy}
                                                </div>
                                            </div>
                                            <div class="row small mb-3">
                                                <div class="col-4">
                                                    <i class="fa-regular fa-calendar me-1"></i>${res.checkIn}
                                                </div>
                                                <div class="col-4 text-center">→</div>
                                                <div class="col-4 text-end">
                                                    <i class="fa-regular fa-calendar-check me-1"></i>${res.checkOut}
                                                </div>
                                            </div>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <h5 class="fw-bold text-gradient mb-0">$${res.totalAmount}</h5>
                                                <c:if test="${res.status == 'CONFIRMED'}">
                                                    <a href="${pageContext.request.contextPath}/manage-reservations?action=delete&id=${res.id}"
                                                        class="btn btn-outline-danger btn-sm"
                                                        onclick="return confirm('Cancel reservation ${res.reservationNumber}?');">
                                                        <i class="fa-solid fa-times me-1"></i>Cancel
                                                    </a>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>