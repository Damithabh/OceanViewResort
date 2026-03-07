<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Dashboard | Ocean View Resort</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        </head>

        <body>

            <!-- Sidebar Include -->
            <jsp:include page="/jsp/components/sidebar.jsp">
                <jsp:param name="activePage" value="dashboard" />
            </jsp:include>

            <!-- Main Content -->
            <div class="content-area animate-fade-in">
                <div class="d-flex align-items-center justify-content-between mb-4">
                    <div>
                        <h2 class="fw-bold" style="color: var(--primary-color);">Dashboard Overview</h2>
                        <p class="text-muted mb-0">Welcome back, <strong>${sessionScope.loggedUser.username}</strong>.
                        </p>
                    </div>
                    <span class="badge bg-info text-dark px-3 py-2">${sessionScope.loggedUser.role}</span>
                </div>

                <!-- KPI Cards Row -->
                <div class="row g-4 mb-4">
                    <div class="col-md-3">
                        <div class="kpi-card text-white" style="background: linear-gradient(135deg, #10B981, #059669);">
                            <div class="d-flex align-items-center">
                                <i class="fa-solid fa-door-open fs-2 me-3 opacity-75"></i>
                                <div>
                                    <div class="fs-1 fw-bold">${availableCount}</div>
                                    <div class="small opacity-75">Available Rooms</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card text-white" style="background: linear-gradient(135deg, #EF4444, #DC2626);">
                            <div class="d-flex align-items-center">
                                <i class="fa-solid fa-bed fs-2 me-3 opacity-75"></i>
                                <div>
                                    <div class="fs-1 fw-bold">${occupiedCount}</div>
                                    <div class="small opacity-75">Occupied Rooms</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card text-white" style="background: linear-gradient(135deg, #F59E0B, #D97706);">
                            <div class="d-flex align-items-center">
                                <i class="fa-solid fa-wrench fs-2 me-3 opacity-75"></i>
                                <div>
                                    <div class="fs-1 fw-bold">${maintenanceCount}</div>
                                    <div class="small opacity-75">Maintenance</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="kpi-card text-white" style="background: linear-gradient(135deg, #635BFF, #8A84FF);">
                            <div class="d-flex align-items-center">
                                <i class="fa-solid fa-calendar-check fs-2 me-3 opacity-75"></i>
                                <div>
                                    <div class="fs-1 fw-bold">${activeReservations}</div>
                                    <div class="small opacity-75">Active Bookings</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row g-4">
                    <!-- Occupancy Chart -->
                    <div class="col-lg-5">
                        <div class="card p-4 h-100">
                            <h5 class="fw-bold mb-3"><i class="fa-solid fa-chart-doughnut me-2 text-primary"></i>Room
                                Occupancy</h5>
                            <canvas id="occupancyChart" style="max-height: 280px;"></canvas>
                        </div>
                    </div>

                    <!-- Room Type Breakdown -->
                    <div class="col-lg-7">
                        <div class="card p-4 h-100">
                            <h5 class="fw-bold mb-3"><i class="fa-solid fa-th-large me-2 text-primary"></i>Room
                                Inventory</h5>
                            <div class="table-responsive">
                                <table class="table table-hover" id="roomTable">
                                    <thead>
                                        <tr>
                                            <th>Room #</th>
                                            <th>Type</th>
                                            <th>Rate</th>
                                            <th>Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${allRooms}" var="room">
                                            <tr>
                                                <td class="fw-bold">${room.roomNumber}</td>
                                                <td>
                                                    <span class="badge bg-light text-dark">${room.roomType}</span>
                                                </td>
                                                <td>$${room.pricePerNight}</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${room.status == 'AVAILABLE'}">
                                                            <span class="badge bg-success">AVAILABLE</span>
                                                        </c:when>
                                                        <c:when test="${room.status == 'OCCUPIED'}">
                                                            <span class="badge bg-danger">OCCUPIED</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-warning text-dark">MAINTENANCE</span>
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

                <!-- Reports & Tools Quick Access -->
                <div class="row g-4 mt-2">
                    <div class="col-12">
                        <h5 class="fw-bold mb-3" style="color: var(--primary-color);">
                            <i class="fa-solid fa-toolbox me-2"></i>Reports & Tools
                        </h5>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/reports" class="text-decoration-none">
                            <div class="card p-4 text-center h-100">
                                <i class="fa-solid fa-chart-bar fa-2x mb-3" style="color: var(--secondary-color);"></i>
                                <h6 class="fw-bold mb-1">Monthly Reports</h6>
                                <p class="text-muted small mb-0">Revenue summaries and booking analytics</p>
                            </div>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/reports" class="text-decoration-none">
                            <div class="card p-4 text-center h-100">
                                <i class="fa-solid fa-file-invoice-dollar fa-2x mb-3 text-success"></i>
                                <h6 class="fw-bold mb-1">Payment Receipts</h6>
                                <p class="text-muted small mb-0">Generate and print guest receipts</p>
                            </div>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/reports?action=logs" class="text-decoration-none">
                            <div class="card p-4 text-center h-100">
                                <i class="fa-solid fa-clock-rotate-left fa-2x mb-3 text-warning"></i>
                                <h6 class="fw-bold mb-1">Audit Logs</h6>
                                <p class="text-muted small mb-0">View booking event history and activity trail</p>
                            </div>
                        </a>
                    </div>
                </div>
            </div>

            <!-- Chart.js -->
            <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                const ctx = document.getElementById('occupancyChart').getContext('2d');
                new Chart(ctx, {
                    type: 'doughnut',
                    data: {
                        labels: ['Available', 'Occupied', 'Maintenance'],
                        datasets: [{
                            data: [${ availableCount }, ${ occupiedCount }, ${ maintenanceCount }],
                            backgroundColor: ['#10B981', '#EF4444', '#F59E0B'],
                            borderWidth: 2,
                            borderColor: '#fff'
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: {
                                position: 'bottom',
                                labels: { padding: 16, usePointStyle: true, font: { family: "'Inter', sans-serif" } }
                            }
                        }
                    }
                });
            </script>
        </body>

        </html>