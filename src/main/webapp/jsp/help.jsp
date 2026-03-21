<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help & Documentation | Ocean View Resort</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <style>
        .help-section {
            background: rgba(255, 255, 255, 0.85);
            backdrop-filter: blur(12px);
            border-radius: 16px;
            padding: 2rem;
            margin-bottom: 2rem;
            box-shadow: 0 8px 32px rgba(0, 0, 0, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.4);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .help-section:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
        }

        .help-icon {
            width: 48px;
            height: 48px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            margin-right: 1.5rem;
            color: white;
        }

        .help-title-bar {
            display: flex;
            align-items: flex-start;
            margin-bottom: 1.5rem;
        }

        .help-content ul {
            list-style-type: none;
            padding-left: 0;
        }

        .help-content ul li {
            position: relative;
            padding-left: 1.5rem;
            margin-bottom: 0.75rem;
            color: var(--text-color);
            opacity: 0.9;
        }

        .help-content ul li::before {
            content: '•';
            color: var(--primary-color);
            font-weight: bold;
            font-size: 1.2rem;
            position: absolute;
            left: 0;
            top: -2px;
        }

        .accordion-item {
            border: none;
            margin-bottom: 1rem;
            border-radius: 12px !important;
            overflow: hidden;
            background: rgba(255, 255, 255, 0.6);
            backdrop-filter: blur(8px);
            border: 1px solid rgba(255, 255, 255, 0.5);
        }

        .accordion-button {
            background: transparent;
            font-weight: 600;
            color: var(--primary-color);
            padding: 1.25rem 1.5rem;
        }

        .accordion-button:not(.collapsed) {
            background: rgba(14, 165, 233, 0.05);
            color: var(--secondary-color);
            box-shadow: none;
        }

        .accordion-button:focus {
            box-shadow: none;
            border-color: rgba(14, 165, 233, 0.2);
        }

        .accordion-body {
            background: transparent;
            padding: 1.5rem;
            color: var(--text-color);
        }
        
        .bg-gradient-primary { background: linear-gradient(135deg, #0ea5e9, #3b82f6); }
        .bg-gradient-success { background: linear-gradient(135deg, #10b981, #059669); }
        .bg-gradient-warning { background: linear-gradient(135deg, #f59e0b, #d97706); }
        .bg-gradient-danger  { background: linear-gradient(135deg, #ef4444, #dc2626); }
        .bg-gradient-info    { background: linear-gradient(135deg, #6366f1, #4f46e5); }
    </style>
</head>

<body>

    <!-- Sidebar Include -->
    <jsp:include page="/jsp/components/sidebar.jsp">
        <jsp:param name="activePage" value="help" />
    </jsp:include>

    <!-- Main Content -->
    <div class="content-area animate-fade-in">
        <div class="d-flex align-items-center justify-content-between mb-4">
            <div>
                <h2 class="fw-bold" style="color: var(--primary-color);">System Help & Documentation</h2>
                <p class="text-muted mb-0">Learn how to navigate and operate the Ocean View Resort Management System.</p>
            </div>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-outline-primary">
                <i class="fa-solid fa-arrow-left me-2"></i>Back to Dashboard
            </a>
        </div>

        <div class="row">
            <div class="col-lg-8 mx-auto">
                
                <!-- Welcome Section -->
                <div class="help-section">
                    <div class="help-title-bar">
                        <div class="help-icon bg-gradient-primary">
                            <i class="fa-solid fa-plane-arrival"></i>
                        </div>
                        <div>
                            <h4 class="fw-bold mb-1">Getting Started</h4>
                            <p class="text-muted small mb-0">Overview of the system layout and basic navigation.</p>
                        </div>
                    </div>
                    <div class="help-content">
                        <p>Welcome to the Ocean View Resort System! The interface is divided into two main areas:</p>
                        <ul>
                            <li><strong>Sidebar Navigation:</strong> Located on the left side, it provides quick access to all major modules across the application.</li>
                            <li><strong>Main Content Area:</strong> The central workspace where data is displayed, forms are filled out, and actions are executed.</li>
                        </ul>
                        <div class="alert alert-info border-0 shadow-sm mt-3" role="alert">
                            <i class="fa-solid fa-lightbulb me-2 text-warning"></i>
                            <strong>Tip:</strong> You can always view your current user level (Admin or Staff) at the bottom left of the sidebar.
                        </div>
                    </div>
                </div>

                <!-- Features Accordion -->
                <div class="accordion mb-5" id="helpAccordion">
                    
                    <!-- Dashboard -->
                    <div class="accordion-item shadow-sm">
                        <h2 class="accordion-header" id="headingDashboard">
                            <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#collapseDashboard" aria-expanded="true" aria-controls="collapseDashboard">
                                <i class="fa-solid fa-chart-pie me-3 form-icon text-primary text-opacity-75"></i> Understanding the Dashboard
                            </button>
                        </h2>
                        <div id="collapseDashboard" class="accordion-collapse collapse show" aria-labelledby="headingDashboard" data-bs-parent="#helpAccordion">
                            <div class="accordion-body">
                                <p>The central hub of the resort operations, featuring real-time insights.</p>
                                <ul>
                                    <li><strong>KPI Cards:</strong> View summaries of Available, Occupied, and Maintenance rooms, plus active reservations.</li>
                                    <li><strong>Occupancy Chart:</strong> Visual breakdown of current room statuses.</li>
                                    <li><strong>Room Inventory List:</strong> A quick glance at every room, its status, and nightly rate.</li>
                                    <li><strong>Quick Links:</strong> Shortcuts to reports, receipts, and audit logs.</li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <!-- Reservations -->
                    <div class="accordion-item shadow-sm">
                        <h2 class="accordion-header" id="headingReservations">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseReservations" aria-expanded="false" aria-controls="collapseReservations">
                                <i class="fa-solid fa-calendar-check me-3 form-icon text-success text-opacity-75"></i> Managing Reservations
                            </button>
                        </h2>
                        <div id="collapseReservations" class="accordion-collapse collapse" aria-labelledby="headingReservations" data-bs-parent="#helpAccordion">
                            <div class="accordion-body">
                                <p>This section handles all guest bookings and check-in/out workflows.</p>
                                <ul>
                                    <li><strong>New Booking:</strong> Search for available rooms based on check-in/check-out dates. Once selected, fill in the guest's details to confirm the reservation. The room's status will automatically shift to occupied.</li>
                                    <li><strong>Reservations List:</strong> View all current reservations. You can edit booking info, cancel reservations, or process check-outs.</li>
                                    <li><strong>Check-Out:</strong> Completing a check-out will free up the room and make it 'Available' for new guests automatically.</li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <!-- Room Management -->
                    <div class="accordion-item shadow-sm">
                        <h2 class="accordion-header" id="headingRooms">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseRooms" aria-expanded="false" aria-controls="collapseRooms">
                                <i class="fa-solid fa-door-open me-3 form-icon text-danger text-opacity-75"></i> Room Management (Admin Only)
                            </button>
                        </h2>
                        <div id="collapseRooms" class="accordion-collapse collapse" aria-labelledby="headingRooms" data-bs-parent="#helpAccordion">
                            <div class="accordion-body">
                                <p>Admins can configure physically available rooms in the hotel network.</p>
                                <ul>
                                    <li><strong>Adding a Room:</strong> Provide the room number, type (Standard, Deluxe, Suite), price per night, and initial status to register it in the system.</li>
                                    <li><strong>Editing Status:</strong> Manually flip a room to 'Maintenance' if repairs are needed, preventing staff from booking it.</li>
                                    <li><strong>Deleting Rooms:</strong> Remove a room from the inventory permanently. <em>Caution: Only delete rooms with no active reservations.</em></li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <!-- Reports & Logs -->
                    <div class="accordion-item shadow-sm">
                        <h2 class="accordion-header" id="headingReports">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseReports" aria-expanded="false" aria-controls="collapseReports">
                                <i class="fa-solid fa-chart-bar me-3 form-icon text-warning text-opacity-75"></i> Reports & Audit Logs
                            </button>
                        </h2>
                        <div id="collapseReports" class="accordion-collapse collapse" aria-labelledby="headingReports" data-bs-parent="#helpAccordion">
                            <div class="accordion-body">
                                <p>Tools for auditing revenue and tracking system usage.</p>
                                <ul>
                                    <li><strong>Monthly Revenue Reports:</strong> View aggregated earnings calculated from the 'Total Cost' of completed reservations.</li>
                                    <li><strong>Payment Receipts:</strong> Generate printable invoice pages for individual guests post-booking.</li>
                                    <li><strong>Audit Logs:</strong> View system-wide activity, such as who booked what room and when.</li>
                                </ul>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap JS for accordion functionality -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
