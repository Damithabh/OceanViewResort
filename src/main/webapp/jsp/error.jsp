<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Error | Ocean View Resort</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body class="hero-bg">
        <div class="container animate-fade-in" style="z-index: 2; position: relative;">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="glass-panel text-center">
                        <i class="fa-solid fa-triangle-exclamation fa-3x text-warning mb-3"></i>
                        <h2 class="fw-bold mb-2" style="color: var(--primary-color);">Something Went Wrong</h2>
                        <p class="text-muted mb-4">We encountered an unexpected error. Please try again.</p>

                        <% if (request.getAttribute("jakarta.servlet.error.status_code") !=null) { %>
                            <div class="card p-3 text-start mb-3" style="background: rgba(239, 68, 68, 0.05);">
                                <small class="text-muted"><strong>Status Code:</strong>
                                    <%= request.getAttribute("jakarta.servlet.error.status_code") %>
                                </small><br>
                                <small class="text-muted"><strong>URI:</strong>
                                    <%= request.getAttribute("jakarta.servlet.error.request_uri") %>
                                </small>
                            </div>
                            <% } %>

                                <% if (exception !=null) { %>
                                    <div class="card p-3 text-start mb-3"
                                        style="background: rgba(239, 68, 68, 0.05); font-size: 0.85rem;">
                                        <strong class="text-danger">Exception:</strong>
                                        <code class="d-block mt-1"><%= exception.getMessage() %></code>
                                    </div>
                                    <% } %>

                                        <div class="d-flex gap-2 justify-content-center">
                                            <a href="${pageContext.request.contextPath}/" class="btn btn-antigravity">
                                                <i class="fa-solid fa-house me-2"></i>Home
                                            </a>
                                            <a href="${pageContext.request.contextPath}/dashboard"
                                                class="btn btn-outline-antigravity">
                                                <i class="fa-solid fa-chart-pie me-2"></i>Dashboard
                                            </a>
                                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>