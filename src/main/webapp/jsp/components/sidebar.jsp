<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%-- Reusable Sidebar Component — included via jsp:include. Eliminates sidebar code duplication across all dashboard
        pages. Expected parameter: activePage (dashboard, reservations, new-booking, rooms) --%>
        <%@ taglib prefix="c" uri="jakarta.tags.core" %>
            <div class="sidebar">
                <div class="sidebar-brand">
                    <h4 class="text-center text-white mb-0">
                        <i class="fa-solid fa-hotel me-2 text-info"></i>Ocean View
                    </h4>
                </div>

                <div class="sidebar-nav">
                    <a href="${pageContext.request.contextPath}/dashboard"
                        class="${param.activePage == 'dashboard' ? 'active' : ''}">
                        <i class="fa-solid fa-chart-pie me-2"></i>Dashboard
                    </a>
                    <a href="${pageContext.request.contextPath}/manage-reservations"
                        class="${param.activePage == 'reservations' ? 'active' : ''}">
                        <i class="fa-solid fa-calendar-check me-2"></i>Reservations
                    </a>
                    <a href="${pageContext.request.contextPath}/manage-reservations?action=new"
                        class="${param.activePage == 'new-booking' ? 'active' : ''}">
                        <i class="fa-solid fa-plus-circle me-2"></i>New Booking
                    </a>

                    <c:if test="${sessionScope.loggedUser.admin}">
                        <a href="${pageContext.request.contextPath}/manage-rooms"
                            class="${param.activePage == 'rooms' ? 'active' : ''}">
                            <i class="fa-solid fa-door-open me-2"></i>Room Management
                        </a>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/reports"
                        class="${param.activePage == 'reports' ? 'active' : ''}">
                        <i class="fa-solid fa-chart-bar me-2"></i>Reports & Receipts
                    </a>
                    <a href="${pageContext.request.contextPath}/reports?action=logs"
                        class="${param.activePage == 'logs' ? 'active' : ''}">
                        <i class="fa-solid fa-clock-rotate-left me-2"></i>Audit Logs
                    </a>
                </div>

                <div class="sidebar-footer">
                    <div class="text-light small mb-2 opacity-75">
                        Logged in as: <strong>${sessionScope.loggedUser.username}</strong><br>
                        Role: <strong>${sessionScope.loggedUser.role}</strong>
                    </div>
                    <a href="${pageContext.request.contextPath}/auth?action=logout" class="text-danger fw-bold small">
                        <i class="fa-solid fa-sign-out-alt me-2"></i>Logout
                    </a>
                </div>
            </div>