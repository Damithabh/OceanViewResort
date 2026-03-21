<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>New Booking | Ocean View Resort</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        </head>

        <body>

            <jsp:include page="/jsp/components/sidebar.jsp">
                <jsp:param name="activePage" value="new-booking" />
            </jsp:include>

            <div class="content-area animate-fade-in">
                <h2 class="fw-bold mb-1" style="color: var(--primary-color);">
                    <i class="fa-solid fa-plus-circle me-2"></i>Create New Reservation
                </h2>
                <p class="text-muted mb-4">Fill in the booking details below. All fields are required.</p>

                <% String error=request.getParameter("error"); %>
                    <% if ("booking_failed".equals(error)) { %>
                        <div class="alert alert-danger py-2"><i
                                class="fa-solid fa-exclamation-triangle me-2"></i>Booking failed. Room may be
                            unavailable or dates are invalid.</div>
                        <% } else if ("invalid_data".equals(error)) { %>
                            <div class="alert alert-danger py-2"><i
                                    class="fa-solid fa-exclamation-triangle me-2"></i>Invalid data submitted. Please
                                check your inputs.</div>
                            <% } %>

                                <div class="card p-4" style="max-width: 700px;">
                                    <form action="${pageContext.request.contextPath}/manage-reservations" method="POST"
                                        id="bookingForm">

                                        <!-- Guest Name -->
                                        <div class="mb-3">
                                            <label for="guestName" class="form-label fw-bold">
                                                <i class="fa-solid fa-user me-1"></i>Guest Full Name
                                            </label>
                                            <input type="text" class="form-control" id="guestName" name="guestName"
                                                placeholder="e.g. John Smith" required maxlength="100">
                                        </div>
 
                                        <!-- Guest Email -->
                                        <div class="mb-3">
                                            <label for="guestEmail" class="form-label fw-bold">
                                                <i class="fa-solid fa-envelope me-1"></i>Guest Email Address
                                            </label>
                                            <input type="email" class="form-control" id="guestEmail" name="guestEmail"
                                                placeholder="e.g. john@example.com" required maxlength="100">
                                            <div class="form-text small">We'll send a welcome email to this address.</div>
                                        </div>

                                        <!-- Room Selection -->
                                        <div class="mb-3">
                                            <label for="roomId" class="form-label fw-bold">
                                                <i class="fa-solid fa-door-open me-1"></i>Select Room
                                            </label>
                                            <select class="form-select" id="roomId" name="roomId" required>
                                                <option value="" disabled selected>— Choose an available room —</option>
                                                <c:forEach items="${availableRooms}" var="room">
                                                    <option value="${room.id}" data-price="${room.pricePerNight}">
                                                        Room ${room.roomNumber} — ${room.roomType}
                                                        ($${room.pricePerNight}/night)
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <c:if test="${empty availableRooms}">
                                                <div class="text-danger mt-2 small"><i
                                                        class="fa-solid fa-warning me-1"></i>No rooms available. All
                                                    rooms are currently booked.</div>
                                            </c:if>
                                        </div>

                                        <!-- Date Fields -->
                                        <div class="row g-3 mb-3">
                                            <div class="col-md-6">
                                                <label for="checkIn" class="form-label fw-bold">
                                                    <i class="fa-solid fa-calendar-day me-1"></i>Check-in Date
                                                </label>
                                                <input type="date" class="form-control" id="checkIn" name="checkIn"
                                                    required>
                                            </div>
                                            <div class="col-md-6">
                                                <label for="checkOut" class="form-label fw-bold">
                                                    <i class="fa-solid fa-calendar-check me-1"></i>Check-out Date
                                                </label>
                                                <input type="date" class="form-control" id="checkOut" name="checkOut"
                                                    required>
                                            </div>
                                        </div>

                                        <!-- Price Estimation -->
                                        <div class="card p-3 mb-4"
                                            style="background: var(--bg-light); border-style: dashed;"
                                            id="estimatePanel">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div>
                                                    <small class="text-muted">Estimated Total</small>
                                                    <h3 class="fw-bold mb-0 text-gradient" id="estimatedPrice">$0.00
                                                    </h3>
                                                </div>
                                                <div class="text-end">
                                                    <small class="text-muted" id="nightCount">0 nights</small>
                                                </div>
                                            </div>
                                        </div>

                                        <button type="submit" class="btn btn-antigravity w-100 py-2 fs-5"
                                            id="submitBtn">
                                            <i class="fa-solid fa-check-circle me-2"></i>Confirm Booking
                                        </button>
                                    </form>
                                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                // Sets minimum check-in date to today
                const today = new Date().toISOString().split('T')[0];
                document.getElementById('checkIn').setAttribute('min', today);

                // Recalculate price estimate on input change
                ['roomId', 'checkIn', 'checkOut'].forEach(id => {
                    document.getElementById(id).addEventListener('change', calculateEstimate);
                });

                function calculateEstimate() {
                    const roomSelect = document.getElementById('roomId');
                    const checkIn = document.getElementById('checkIn').value;
                    const checkOut = document.getElementById('checkOut').value;
                    const selectedOption = roomSelect.options[roomSelect.selectedIndex];
                    const pricePerNight = selectedOption ? parseFloat(selectedOption.dataset.price) : 0;

                    if (checkIn && checkOut && pricePerNight > 0) {
                        const nights = Math.ceil((new Date(checkOut) - new Date(checkIn)) / 86400000);
                        if (nights > 0) {
                            document.getElementById('estimatedPrice').textContent = '$' + (nights * pricePerNight).toFixed(2);
                            document.getElementById('nightCount').textContent = nights + ' night' + (nights > 1 ? 's' : '');
                        } else {
                            document.getElementById('estimatedPrice').textContent = '$0.00';
                            document.getElementById('nightCount').textContent = 'Invalid dates';
                        }
                    }

                    // Update min check-out date
                    if (checkIn) {
                        const nextDay = new Date(checkIn);
                        nextDay.setDate(nextDay.getDate() + 1);
                        document.getElementById('checkOut').setAttribute('min', nextDay.toISOString().split('T')[0]);
                    }
                }

                // Client-side validation
                document.getElementById('bookingForm').addEventListener('submit', function (e) {
                    const checkIn = new Date(document.getElementById('checkIn').value);
                    const checkOut = new Date(document.getElementById('checkOut').value);
                    if (checkOut <= checkIn) {
                        e.preventDefault();
                        alert('Check-out date must be after the check-in date.');
                    }
                });
            </script>
        </body>

        </html>