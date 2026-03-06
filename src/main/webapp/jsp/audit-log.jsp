<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Audit Logs | Ocean View Resort</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        </head>

        <body>

            <jsp:include page="/jsp/components/sidebar.jsp">
                <jsp:param name="activePage" value="logs" />
            </jsp:include>

            <div class="content-area animate-fade-in">
                <div class="d-flex align-items-center justify-content-between mb-3">
                    <div>
                        <h2 class="fw-bold" style="color: var(--primary-color);">
                            <i class="fa-solid fa-clock-rotate-left me-2"></i>Booking Audit Logs
                        </h2>
                        <p class="text-muted mb-0">System event history and activity trail.</p>
                    </div>
                </div>

                <!-- Filter Bar -->
                <div class="card p-3 mb-4">
                    <form action="${pageContext.request.contextPath}/reports" method="GET"
                        class="row g-2 align-items-end">
                        <input type="hidden" name="action" value="logs">
                        <div class="col-md-3">
                            <label class="form-label small fw-bold">Event Type</label>
                            <select class="form-select" name="filterEvent">
                                <option value="">All Events</option>
                                <option value="RESERVATION_CREATED" ${filterEvent=='RESERVATION_CREATED' ? 'selected'
                                    : '' }>Reservation Created</option>
                                <option value="RESERVATION_CANCELLED" ${filterEvent=='RESERVATION_CANCELLED'
                                    ? 'selected' : '' }>Reservation Cancelled</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label small fw-bold">Entity Type</label>
                            <select class="form-select" name="filterEntity">
                                <option value="">All Entities</option>
                                <option value="RESERVATION" ${filterEntity=='RESERVATION' ? 'selected' : '' }>
                                    Reservation</option>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <button type="submit" class="btn btn-outline-antigravity w-100">
                                <i class="fa-solid fa-filter me-1"></i>Filter
                            </button>
                        </div>
                        <div class="col-md-2">
                            <a href="${pageContext.request.contextPath}/reports?action=logs"
                                class="btn btn-outline-secondary w-100">
                                <i class="fa-solid fa-rotate-left me-1"></i>Reset
                            </a>
                        </div>
                    </form>
                </div>

                <!-- Audit Log Table -->
                <div class="card p-3">
                    <c:choose>
                        <c:when test="${empty auditLogs}">
                            <div class="text-center py-5 text-muted">
                                <i class="fa-solid fa-inbox fa-3x mb-3 d-block"></i>
                                <h5>No audit log entries found.</h5>
                                <p>Logs are automatically created when reservations are made or cancelled.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover mb-0">
                                    <thead>
                                        <tr>
                                            <th>#</th>
                                            <th>Event</th>
                                            <th>Entity</th>
                                            <th>Description</th>
                                            <th>Performed By</th>
                                            <th>Timestamp</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${auditLogs}" var="log">
                                            <tr>
                                                <td class="text-muted">${log.id}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${log.event_type == 'RESERVATION_CREATED'}">
                                                            <span class="badge bg-success"><i
                                                                    class="fa-solid fa-plus me-1"></i>Created</span>
                                                        </c:when>
                                                        <c:when test="${log.event_type == 'RESERVATION_CANCELLED'}">
                                                            <span class="badge bg-danger"><i
                                                                    class="fa-solid fa-times me-1"></i>Cancelled</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${log.event_type}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td><span class="badge bg-light text-dark">${log.entity_type}</span>
                                                </td>
                                                <td class="small">${log.description}</td>
                                                <td class="fw-bold">${log.performed_by}</td>
                                                <td class="text-muted small">${log.created_at}</td>
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