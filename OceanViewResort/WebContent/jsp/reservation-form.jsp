<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <% if (session.getAttribute("loggedUser")==null) { response.sendRedirect(request.getContextPath()
            + "/jsp/login.jsp?error=unauthorized" ); return; } %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>New Booking | Ocean View</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
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
                    <a href="${pageContext.request.contextPath}/manage-reservations">
                        <i class="fa-solid fa-calendar-check me-2"></i>Reservations
                    </a>
                    <a href="${pageContext.request.contextPath}/manage-reservations?action=new" class="active">
                        <i class="fa-solid fa-plus-circle me-2"></i>New Booking
                    </a>
                </div>

                <!-- Content Area -->
                <div class="content-area bg-light">
                    <div class="container-fluid animate-fade-in">
                        <div class="row pt-3 mb-4">
                            <div class="col-12">
                                <h2 class="fw-bold" style="color: var(--primary-color);">Create Reservation</h2>
                                <p class="text-muted">Fill out the guest details below to secure a room.</p>
                            </div>
                        </div>

                        <div class="row justify-content-center">
                            <div class="col-lg-8">
                                <div class="card shadow-sm border-0" style="border-radius: 15px;">

                                    <% String error=request.getParameter("error"); if ("booking_failed".equals(error)) {
                                        out.print("<div class='alert alert-danger mx-4 mt-4'><i
                                            class='fa-solid fa-triangle-exclamation me-2'></i>Booking failed. Database
                                        constraint triggered - dates may overlap or room taken.
                                </div>");
                                }
                                if ("invalid_data".equals(error)) {
                                out.print("<div class='alert alert-danger mx-4 mt-4'><i
                                        class='fa-solid fa-triangle-exclamation me-2'></i>Invalid data submitted. Verify
                                    date selections.</div>");
                                }
                                %>

                                <div class="card-body p-5">
                                    <form id="reservationForm"
                                        action="${pageContext.request.contextPath}/manage-reservations" method="POST">
                                        <!-- Action to route to POST instead of GET -->
                                        <input type="hidden" name="action" value="create">

                                        <h5 class="fw-bold mb-4" style="color: var(--secondary-color);">
                                            <i class="fa-regular fa-id-badge me-2"></i>Guest Information
                                        </h5>

                                        <div class="form-floating mb-4">
                                            <input type="text" class="form-control bg-light border-0" id="guestName"
                                                name="guestName" placeholder="John Doe" required>
                                            <label for="guestName">Full Guest Name</label>
                                            <div class="invalid-feedback" id="nameError">Please enter a valid guest
                                                name.</div>
                                        </div>

                                        <h5 class="fw-bold mb-4 mt-5" style="color: var(--secondary-color);">
                                            <i class="fa-solid fa-door-open me-2"></i>Room Selection
                                        </h5>

                                        <div class="form-floating mb-4">
                                            <select class="form-select bg-light border-0 py-3 mt-2" id="roomId"
                                                name="roomId" required style="height: auto;">
                                                <option value="" selected disabled>Select an available room...</option>
                                                <c:forEach var="room" items="${availableRooms}">
                                                    <option value="${room.id}" data-price="${room.pricePerNight}">
                                                        Room ${room.roomNumber} - ${room.roomType}
                                                        ($${room.pricePerNight}/night)
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <div class="invalid-feedback">A room must be selected.</div>
                                        </div>
                                        <c:if test="${empty availableRooms}">
                                            <div class="alert alert-warning">
                                                <i class="fa-solid fa-face-frown me-2"></i>No rooms are currently
                                                available in the resort.
                                            </div>
                                        </c:if>


                                        <h5 class="fw-bold mb-4 mt-5" style="color: var(--secondary-color);">
                                            <i class="fa-regular fa-calendar-days me-2"></i>Stay Duration
                                        </h5>

                                        <div class="row g-3 mb-4">
                                            <div class="col-md-6">
                                                <div class="form-floating">
                                                    <input type="date" class="form-control bg-light border-0"
                                                        id="checkIn" name="checkIn" required>
                                                    <label for="checkIn">Check-In Date</label>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="form-floating">
                                                    <input type="date" class="form-control bg-light border-0"
                                                        id="checkOut" name="checkOut" required>
                                                    <label for="checkOut">Check-Out Date</label>
                                                    <div class="invalid-feedback" id="dateError">Check-out must be
                                                        strictly after Check-in.</div>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Dynamic Pricing Estimation -->
                                        <div
                                            class="bg-light p-4 rounded-3 border mb-4 d-flex justify-content-between align-items-center">
                                            <span class="text-muted fw-bold text-uppercase small"><i
                                                    class="fa-solid fa-calculator me-2"></i>Estimated Total</span>
                                            <h3 class="mb-0 fw-bold" style="color: var(--primary-color);"
                                                id="totalEstimation">$0.00</h3>
                                        </div>

                                        <hr class="my-4">

                                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                                            <a href="${pageContext.request.contextPath}/dashboard"
                                                class="btn btn-light px-4 border text-muted">Cancel</a>
                                            <button type="submit" class="btn btn-antigravity px-5" id="submitBtn"
                                                ${empty availableRooms ? 'disabled' : '' }>Confirm Booking</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                </div>

                <!-- Vanilla Javascript Real-time Validation Engine -->
                <script>
                    document.addEventListener('DOMContentLoaded', () => {
                        const checkInInput = document.getElementById('checkIn');
                        const checkOutInput = document.getElementById('checkOut');
                        const roomSelect = document.getElementById('roomId');
                        const estimationDisplay = document.getElementById('totalEstimation');
                        const form = document.getElementById('reservationForm');
                        const dateError = document.getElementById('dateError');

                        // Set minimum check-in date to today
                        const today = new Date().toISOString().split('T')[0];
                        checkInInput.setAttribute('min', today);

                        // Re-eval function for total
                        const calculateTotal = () => {
                            if (checkInInput.value && checkOutInput.value && roomSelect.value) {
                                const start = new Date(checkInInput.value);
                                const end = new Date(checkOutInput.value);

                                // Reset Error State
                                checkOutInput.classList.remove('is-invalid');
                                dateError.style.display = 'none';

                                if (end <= start) {
                                    checkOutInput.classList.add('is-invalid');
                                    dateError.style.display = 'block';
                                    estimationDisplay.innerText = '$0.00';
                                    return;
                                }

                                // Calculate days Difference (ChronoUnit.DAYS logic ported to JS)
                                const timeDiff = end.getTime() - start.getTime();
                                const dayDiff = Math.ceil(timeDiff / (1000 * 3600 * 24));

                                // Get Price from selected option data attribute
                                const selectedOption = roomSelect.options[roomSelect.selectedIndex];
                                const pricePerNight = selectedOption.getAttribute('data-price');

                                if (pricePerNight && dayDiff > 0) {
                                    const total = (parseFloat(pricePerNight) * dayDiff).toFixed(2);
                                    estimationDisplay.innerText = '$' + total;

                                    // Small animation
                                    estimationDisplay.style.transform = 'scale(1.1)';
                                    setTimeout(() => { estimationDisplay.style.transform = 'scale(1)'; }, 150);
                                }
                            } else {
                                estimationDisplay.innerText = '$0.00';
                            }
                        };

                        // Setup Listeners
                        checkInInput.addEventListener('change', () => {
                            // Ensure checkout min date is check-in + 1
                            if (checkInInput.value) {
                                const minOut = new Date(checkInInput.value);
                                minOut.setDate(minOut.getDate() + 1);
                                checkOutInput.setAttribute('min', minOut.toISOString().split('T')[0]);
                            }
                            calculateTotal();
                        });
                        checkOutInput.addEventListener('change', calculateTotal);
                        roomSelect.addEventListener('change', calculateTotal);

                        // Primary Form Submission Block
                        form.addEventListener('submit', (evt) => {
                            const start = new Date(checkInInput.value);
                            const end = new Date(checkOutInput.value);

                            if (end <= start) {
                                evt.preventDefault();
                                evt.stopPropagation();
                                checkOutInput.classList.add('is-invalid');
                                dateError.style.display = 'block';
                            }
                        });
                    });
                </script>
            </body>

            </html>