<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Payment Receipt — ${reservation.reservationNumber} | Ocean View Resort</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
            <style>
                @media print {
                    .no-print {
                        display: none !important;
                    }

                    .content-area {
                        margin-left: 0 !important;
                        padding: 0 !important;
                    }

                    .sidebar {
                        display: none !important;
                    }

                    body {
                        background: white !important;
                    }

                    .receipt-card {
                        box-shadow: none !important;
                        border: 1px solid #ddd !important;
                    }
                }

                .receipt-card {
                    max-width: 700px;
                    margin: 0 auto;
                    border-radius: 20px;
                    overflow: hidden;
                }

                .receipt-header {
                    background: linear-gradient(135deg, #0A2540, #1a3a5c);
                    color: white;
                    padding: 2rem;
                }

                .receipt-body {
                    padding: 2rem;
                }

                .receipt-row {
                    display: flex;
                    justify-content: space-between;
                    padding: 0.75rem 0;
                    border-bottom: 1px solid #f0f0f0;
                }

                .receipt-row:last-child {
                    border-bottom: none;
                }

                .receipt-total {
                    background: #f8fafc;
                    border-radius: 12px;
                    padding: 1.25rem;
                    margin-top: 1rem;
                }
            </style>
        </head>

        <body>

            <jsp:include page="/jsp/components/sidebar.jsp">
                <jsp:param name="activePage" value="reports" />
            </jsp:include>

            <div class="content-area animate-fade-in">
                <div class="d-flex align-items-center justify-content-between mb-4 no-print">
                    <a href="${pageContext.request.contextPath}/reports" class="btn btn-outline-secondary">
                        <i class="fa-solid fa-arrow-left me-2"></i>Back to Reports
                    </a>
                    <button class="btn btn-antigravity" onclick="window.print();">
                        <i class="fa-solid fa-print me-2"></i>Print Receipt
                    </button>
                </div>

                <div class="receipt-card card">
                    <!-- Receipt Header -->
                    <div class="receipt-header text-center">
                        <h3 class="fw-bold mb-1"><i class="fa-solid fa-hotel me-2"></i>Ocean View Resort</h3>
                        <p class="mb-0 opacity-75">Galle, Sri Lanka — Luxury by the Ocean</p>
                        <hr style="border-color: rgba(255,255,255,0.2);">
                        <h5 class="mb-0"><i class="fa-solid fa-file-invoice-dollar me-2"></i>PAYMENT RECEIPT</h5>
                    </div>

                    <!-- Receipt Body -->
                    <div class="receipt-body">
                        <div class="receipt-row">
                            <span class="text-muted">Reservation Number</span>
                            <strong>${reservation.reservationNumber}</strong>
                        </div>
                        <div class="receipt-row">
                            <span class="text-muted">Guest Name</span>
                            <strong>${reservation.guestName}</strong>
                        </div>
                        <div class="receipt-row">
                            <span class="text-muted">Room</span>
                            <strong>${reservation.roomNumber} — ${reservation.roomType}</strong>
                        </div>
                        <div class="receipt-row">
                            <span class="text-muted">Rate Per Night</span>
                            <strong>$${room.pricePerNight}</strong>
                        </div>
                        <div class="receipt-row">
                            <span class="text-muted">Check-in Date</span>
                            <strong>${reservation.checkIn}</strong>
                        </div>
                        <div class="receipt-row">
                            <span class="text-muted">Check-out Date</span>
                            <strong>${reservation.checkOut}</strong>
                        </div>
                        <div class="receipt-row">
                            <span class="text-muted">Booking Status</span>
                            <c:choose>
                                <c:when test="${reservation.status == 'CONFIRMED'}">
                                    <span class="badge bg-success">CONFIRMED</span>
                                </c:when>
                                <c:when test="${reservation.status == 'CANCELLED'}">
                                    <span class="badge bg-secondary">CANCELLED</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-info text-dark">${reservation.status}</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="receipt-row">
                            <span class="text-muted">Booked By</span>
                            <strong>${reservation.bookedBy}</strong>
                        </div>

                        <!-- Total Amount -->
                        <div class="receipt-total text-center">
                            <small class="text-muted d-block mb-1">TOTAL AMOUNT</small>
                            <h2 class="fw-bold  mb-0">$${reservation.totalAmount}</h2>
                        </div>

                        <div class="text-center mt-4 text-muted small">
                            <p class="mb-1">This is a computer-generated receipt.</p>
                            <p class="mb-0">&copy; 2026 Ocean View Resort — All Rights Reserved</p>
                        </div>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        </body>

        </html>