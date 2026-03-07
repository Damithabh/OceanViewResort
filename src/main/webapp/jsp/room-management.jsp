<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8">
            <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Room Management | Ocean View Resort</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
            <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        </head>

        <body>

            <jsp:include page="/jsp/components/sidebar.jsp">
                <jsp:param name="activePage" value="rooms" />
            </jsp:include>

            <div class="content-area animate-fade-in">
                <div class="d-flex align-items-center justify-content-between mb-3">
                    <div>
                        <h2 class="fw-bold" style="color: var(--primary-color);">
                            <i class="fa-solid fa-door-open me-2"></i>Room Management
                        </h2>
                        <p class="text-muted mb-0">Admin panel — Add, edit, or remove rooms.</p>
                    </div>
                    <button class="btn btn-antigravity" data-bs-toggle="modal" data-bs-target="#addRoomModal">
                        <i class="fa-solid fa-plus me-2"></i>Add Room
                    </button>
                </div>

                <%-- Status Messages --%>
                    <c:if test="${param.msg == 'created'}">
                        <div class="alert alert-success py-2"><i class="fa-solid fa-check-circle me-2"></i>Room added
                            successfully!</div>
                    </c:if>
                    <c:if test="${param.msg == 'updated'}">
                        <div class="alert alert-info py-2"><i class="fa-solid fa-check-circle me-2"></i>Room updated
                            successfully!</div>
                    </c:if>
                    <c:if test="${param.msg == 'deleted'}">
                        <div class="alert alert-warning py-2"><i class="fa-solid fa-trash me-2"></i>Room deleted.</div>
                    </c:if>
                    <c:if test="${param.error == 'delete_failed'}">
                        <div class="alert alert-danger py-2"><i class="fa-solid fa-exclamation-triangle me-2"></i>Cannot
                            delete room — it may have active reservations.</div>
                    </c:if>

                    <!-- Filter Bar -->
                    <div class="card p-3 mb-4">
                        <form action="${pageContext.request.contextPath}/manage-rooms" method="GET"
                            class="row g-2 align-items-end">
                            <div class="col-md-3">
                                <label class="form-label small fw-bold">Type</label>
                                <select class="form-select" name="filterType">
                                    <option value="">All Types</option>
                                    <option value="STANDARD" ${filterType=='STANDARD' ? 'selected' : '' }>Standard
                                    </option>
                                    <option value="DELUXE" ${filterType=='DELUXE' ? 'selected' : '' }>Deluxe</option>
                                    <option value="SUITE" ${filterType=='SUITE' ? 'selected' : '' }>Suite</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <label class="form-label small fw-bold">Status</label>
                                <select class="form-select" name="filterStatus">
                                    <option value="">All Statuses</option>
                                    <option value="AVAILABLE" ${filterStatus=='AVAILABLE' ? 'selected' : '' }>Available
                                    </option>
                                    <option value="OCCUPIED" ${filterStatus=='OCCUPIED' ? 'selected' : '' }>Occupied
                                    </option>
                                    <option value="MAINTENANCE" ${filterStatus=='MAINTENANCE' ? 'selected' : '' }>
                                        Maintenance</option>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <button type="submit" class="btn btn-outline-antigravity w-100"><i
                                        class="fa-solid fa-filter me-1"></i>Filter</button>
                            </div>
                            <div class="col-md-2">
                                <a href="${pageContext.request.contextPath}/manage-rooms"
                                    class="btn btn-outline-secondary w-100"><i
                                        class="fa-solid fa-rotate-left me-1"></i>Reset</a>
                            </div>
                        </form>
                    </div>

                    <!-- Room Table -->
                    <div class="card p-3">
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead>
                                    <tr>
                                        <th>Room #</th>
                                        <th>Type</th>
                                        <th>Rate/Night</th>
                                        <th>Status</th>
                                        <th>Description</th>
                                        <th class="text-end">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${rooms}" var="room">
                                        <tr>
                                            <td class="fw-bold">${room.roomNumber}</td>
                                            <td><span class="badge bg-light text-dark">${room.roomType}</span></td>
                                            <td>$${room.pricePerNight}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${room.status == 'AVAILABLE'}"><span
                                                            class="badge bg-success">AVAILABLE</span></c:when>
                                                    <c:when test="${room.status == 'OCCUPIED'}"><span
                                                            class="badge bg-danger">OCCUPIED</span></c:when>
                                                    <c:otherwise><span
                                                            class="badge bg-warning text-dark">MAINTENANCE</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-muted small">${room.description}</td>
                                            <td class="text-end">
                                                <button class="btn btn-sm btn-outline-primary me-1"
                                                    data-bs-toggle="modal" data-bs-target="#editRoomModal"
                                                    onclick="populateEditModal(${room.id}, '${room.roomNumber}', '${room.roomType}', '${room.pricePerNight}', '${room.status}', '${room.description}')">
                                                    <i class="fa-solid fa-pen"></i>
                                                </button>
                                                <a href="${pageContext.request.contextPath}/manage-rooms?action=delete&id=${room.id}"
                                                    class="btn btn-sm btn-outline-danger"
                                                    onclick="return confirm('Delete room ${room.roomNumber}?');">
                                                    <i class="fa-solid fa-trash"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
            </div>

            <!-- Add Room Modal -->
            <div class="modal fade" id="addRoomModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title fw-bold"><i class="fa-solid fa-plus-circle me-2"></i>Add New Room
                            </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/manage-rooms" method="POST">
                            <input type="hidden" name="action" value="create">
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Room Number</label>
                                    <input type="text" class="form-control" name="roomNumber" placeholder="e.g. 401"
                                        required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Room Type</label>
                                    <select class="form-select" name="roomType" required>
                                        <option value="STANDARD">Standard ($100/night)</option>
                                        <option value="DELUXE">Deluxe ($200/night)</option>
                                        <option value="SUITE">Suite ($500/night)</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Custom Price (optional)</label>
                                    <input type="number" step="0.01" class="form-control" name="pricePerNight"
                                        placeholder="Leave blank for default">
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Description</label>
                                    <textarea class="form-control" name="description" rows="2"
                                        placeholder="Room description..."></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-outline-secondary"
                                    data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-antigravity"><i
                                        class="fa-solid fa-check me-2"></i>Add Room</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Edit Room Modal -->
            <div class="modal fade" id="editRoomModal" tabindex="-1">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title fw-bold"><i class="fa-solid fa-pen me-2"></i>Edit Room</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/manage-rooms" method="POST">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="roomId" id="editRoomId">
                            <div class="modal-body">
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Room Number</label>
                                    <input type="text" class="form-control" name="roomNumber" id="editRoomNumber"
                                        required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Room Type</label>
                                    <select class="form-select" name="roomType" id="editRoomType" required>
                                        <option value="STANDARD">Standard</option>
                                        <option value="DELUXE">Deluxe</option>
                                        <option value="SUITE">Suite</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Price Per Night</label>
                                    <input type="number" step="0.01" class="form-control" name="pricePerNight"
                                        id="editPrice" required>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Status</label>
                                    <select class="form-select" name="status" id="editStatus" required>
                                        <option value="AVAILABLE">Available</option>
                                        <option value="OCCUPIED">Occupied</option>
                                        <option value="MAINTENANCE">Maintenance</option>
                                    </select>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label fw-bold">Description</label>
                                    <textarea class="form-control" name="description" id="editDescription"
                                        rows="2"></textarea>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-outline-secondary"
                                    data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-antigravity"><i
                                        class="fa-solid fa-save me-2"></i>Save Changes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                function populateEditModal(id, number, type, price, status, desc) {
                    document.getElementById('editRoomId').value = id;
                    document.getElementById('editRoomNumber').value = number;
                    document.getElementById('editRoomType').value = type;
                    document.getElementById('editPrice').value = price;
                    document.getElementById('editStatus').value = status;
                    document.getElementById('editDescription').value = desc;
                }
            </script>
        </body>

        </html>