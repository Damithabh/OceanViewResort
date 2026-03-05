<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <% if (session.getAttribute("loggedUser")==null) { response.sendRedirect(request.getContextPath()
            + "/jsp/login.jsp?error=unauthorized" ); return; } %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Dashboard | Ocean View Resort</title>
                <!-- Bootstrap 5 -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <!-- FontAwesome -->
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <!-- Chart.js -->
                <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
                <!-- Custom Style -->
                <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
            </head>

            <body>

                <!-- Sidebar Navigation -->
                <div class="sidebar">
                    <h3 class="text-center mb-4 text-white pb-3 border-bottom border-light">
                        <i class="fa-solid fa-hotel me-2"></i>Ocean View
                    </h3>

                    <a href="${pageContext.request.contextPath}/dashboard" class="active">
                        <i class="fa-solid fa-chart-pie me-2"></i>Dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/manage-reservations">
                        <i class="fa-solid fa-calendar-check me-2"></i>Reservations
                    </a>
                    <a href="${pageContext.request.contextPath}/manage-reservations?action=new">
                        <i class="fa-solid fa-plus-circle me-2"></i>New Booking
                    </a>

                    <div style="position: absolute; bottom: 20px; width: 100%;">
                        <hr class="text-light">
                        <div class="px-3 text-light small mb-2 opacity-75">
                            Logged in as: <strong>${sessionScope.loggedUser.username}</strong><br>
                            Role: <strong>${sessionScope.loggedUser.role}</strong>
                        </div>
                        <a href="${pageContext.request.contextPath}/auth?action=logout" class="text-danger fw-bold">
                            <i class="fa-solid fa-sign-out-alt me-2"></i>Logout
                        </a>
                    </div>
                </div>

                <!-- Main Content Area -->
                <div class="content-area bg-light">
                    <div class="container-fluid animate-fade-in">
                        <div class="row mb-4 pt-3">
                            <div class="col-12 d-flex justify-content-between align-items-center">
                                <h2 class="fw-bold" style="color: var(--primary-color);">Resort Overview</h2>
                                <span class="badge rounded-pill bg-success px-3 py-2">System Online - Port 8080</span>
                            </div>
                        </div>

                        <!-- KPI Cards -->
                        <div class="row mb-4">
                            <!-- Available Rooms Card -->
                            <div class="col-md-4 mb-3">
                                <div class="card shadow-sm border-0 h-100"
                                    style="border-radius: 15px; border-left: 5px solid var(--accent-color) !important;">
                                    <div class="card-body py-4">
                                        <h6 class="text-muted fw-bold text-uppercase">Available Rooms</h6>
                                        <h2 class="mt-2 text-dark font-weight-bold">${availableCount}</h2>
                                        <p class="mb-0 text-success small"><i class="fa-solid fa-arrow-up me-1"></i>
                                            Ready for booking</p>
                                    </div>
                                </div>
                            </div>

                            <!-- Occupied Rooms Card -->
                            <div class="col-md-4 mb-3">
                                <div class="card shadow-sm border-0 h-100"
                                    style="border-radius: 15px; border-left: 5px solid var(--secondary-color) !important;">
                                    <div class="card-body py-4">
                                        <h6 class="text-muted fw-bold text-uppercase">Occupied Rooms</h6>
                                        <h2 class="mt-2 text-dark font-weight-bold">${occupiedCount}</h2>
                                        <p class="mb-0 text-primary small"><i class="fa-solid fa-bed me-1"></i>
                                            Currently hosting guests</p>
                                    </div>
                                </div>
                            </div>

                            <!-- API Status / Active Reservations -->
                            <div class="col-md-4 mb-3">
                                <div class="card shadow-sm border-0 h-100"
                                    style="background: linear-gradient(135deg, var(--primary-color), var(--secondary-color)); color: white; border-radius: 15px;">
                                    <div class="card-body py-4 d-flex align-items-center justify-content-between">
                                        <div>
                                            <h6 class="text-white-50 fw-bold text-uppercase">Distributed Layer Status
                                            </h6>
                                            <h4 class="mt-2 text-white">REST API Active</h4>
                                            <p class="mb-0 text-white-50 small">Endpoint: /api/reservations</p>
                                        </div>
                                        <i class="fa-solid fa-server fa-3x opacity-50"></i>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Charts Row -->
                        <div class="row">
                            <div class="col-md-8 mb-4">
                                <div class="card shadow-sm border-0 h-100" style="border-radius: 15px;">
                                    <div class="card-header bg-white border-0 pt-4 pb-0">
                                        <h5 class="fw-bold" style="color: var(--primary-color);">Live Room Occupancy
                                            (Chart.js)</h5>
                                        <p class="text-muted small">Visualized representation of current hotel state.
                                        </p>
                                    </div>
                                    <div class="card-body">
                                        <canvas id="occupancyChart" height="100"></canvas>
                                    </div>
                                </div>
                            </div>

                            <div class="col-md-4 mb-4">
                                <div class="card shadow-sm border-0 h-100" style="border-radius: 15px;">
                                    <div class="card-header bg-white border-0 pt-4 pb-0">
                                        <h5 class="fw-bold" style="color: var(--primary-color);">Room Types</h5>
                                    </div>
                                    <div class="card-body px-0">
                                        <ul class="list-group list-group-flush">
                                            <li
                                                class="list-group-item d-flex justify-content-between align-items-center border-0 py-3">
                                                <span><i class="fa-solid fa-key me-3 text-secondary"></i>Standard</span>
                                                <span class="badge bg-light text-dark rounded-pill">100 USD/night</span>
                                            </li>
                                            <li
                                                class="list-group-item d-flex justify-content-between align-items-center border-0 py-3">
                                                <span><i class="fa-solid fa-star me-3 text-primary"></i>Deluxe</span>
                                                <span class="badge bg-light text-dark rounded-pill">200 USD/night</span>
                                            </li>
                                            <li
                                                class="list-group-item d-flex justify-content-between align-items-center border-0 py-3">
                                                <span><i class="fa-solid fa-crown me-3 text-warning"></i>Suite</span>
                                                <span class="badge bg-light text-dark rounded-pill">500 USD/night</span>
                                            </li>
                                        </ul>

                                        <div class="text-center mt-4 px-3">
                                            <a href="${pageContext.request.contextPath}/manage-reservations?action=new"
                                                class="btn btn-antigravity w-100">
                                                <i class="fa-solid fa-bolt me-2"></i>Express Book
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- All Rooms Data Table (MVC Display) -->
                        <div class="row mt-2">
                            <div class="col-12">
                                <div class="card shadow-sm border-0" style="border-radius: 15px;">
                                    <div class="card-header bg-white border-0 pt-4 pb-2">
                                        <h5 class="fw-bold" style="color: var(--primary-color);">System Room Ledger</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="table-responsive">
                                            <table class="table table-hover align-middle">
                                                <thead class="table-light text-muted">
                                                    <tr>
                                                        <th>Room #</th>
                                                        <th>Type</th>
                                                        <th>Base Rate</th>
                                                        <th>Status</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="room" items="${allRooms}">
                                                        <tr>
                                                            <td class="fw-bold text-dark">${room.roomNumber}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${room.roomType == 'SUITE'}"><span
                                                                            class="badge bg-warning text-dark"><i
                                                                                class="fa-solid fa-crown me-1"></i>Suite</span>
                                                                    </c:when>
                                                                    <c:when test="${room.roomType == 'DELUXE'}"><span
                                                                            class="badge bg-primary">Deluxe</span>
                                                                    </c:when>
                                                                    <c:otherwise><span
                                                                            class="badge bg-secondary">Standard</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>$
                                                                <c:out value="${room.pricePerNight}" />
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${room.status == 'AVAILABLE'}">
                                                                        <span class="text-success fw-bold"><i
                                                                                class="fa-solid fa-circle me-1"
                                                                                style="font-size: 8px;"></i>Available</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="text-danger fw-bold"><i
                                                                                class="fa-solid fa-circle me-1"
                                                                                style="font-size: 8px;"></i>Occupied</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                <script>
                    // Initialize Chart.js with dynamic JSTL data injected by Servlet
                    document.addEventListener('DOMContentLoaded', function () {
                        var ctx = document.getElementById('occupancyChart').getContext('2d');

                        // Antigravity themed gradients
                        let gradientAvailable = ctx.createLinearGradient(0, 0, 0, 300);
                        gradientAvailable.addColorStop(0, 'rgba(0, 212, 255, 0.8)');
                        gradientAvailable.addColorStop(1, 'rgba(0, 212, 255, 0.2)');

                        let gradientOccupied = ctx.createLinearGradient(0, 0, 0, 300);
                        gradientOccupied.addColorStop(0, 'rgba(99, 91, 255, 0.8)');
                        gradientOccupied.addColorStop(1, 'rgba(99, 91, 255, 0.2)');

                        var occupancyChart = new Chart(ctx, {
                            type: 'bar', // Professional bar chart for layout balance
                            data: {
                                labels: ['Available', 'Occupied'],
                                datasets: [{
                                    label: 'Room Distribution',
                                    data: ['${availableCount}', '${occupiedCount}'],
                                    backgroundColor: [gradientAvailable, gradientOccupied],
                                    borderRadius: 10,
                                    borderSkipped: false,
                                    barThickness: 60
                                }]
                            },
                            options: {
                                responsive: true,
                                plugins: {
                                    legend: { display: false },
                                    tooltip: {
                                        backgroundColor: '#0A2540',
                                        padding: 12,
                                        titleFont: { size: 14, family: "'Outfit', sans-serif" },
                                        bodyFont: { size: 13, family: "'Outfit', sans-serif" }
                                    }
                                },
                                scales: {
                                    y: {
                                        beginAtZero: true,
                                        grid: { color: 'rgba(0,0,0,0.05)' },
                                        ticks: { precision: 0 }
                                    },
                                    x: {
                                        grid: { display: false }
                                    }
                                },
                                animation: {
                                    duration: 1500,
                                    easing: 'easeOutQuart'
                                }
                            }
                        });
                    });
                </script>
            </body>

            </html>