<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!-- 
  Ocean View Resort — Landing Page
  Acts as the public-facing entry point.
  Directs staff to the secure internal portal via /dashboard.
-->
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Welcome | Ocean View Resort</title>
        <meta name="description"
            content="Ocean View Resort — Experience luxury by the ocean. Staff portal for resort management.">
        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- FontAwesome -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <!-- Custom Styles -->
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        <style>
            .hero {
                height: 100vh;
                background: linear-gradient(rgba(10, 37, 64, 0.7), rgba(10, 37, 64, 0.7)),
                    url('https://images.unsplash.com/photo-1540541338287-41700207dee6?auto=format&fit=crop&q=80&w=2000') no-repeat center center/cover;
                display: flex;
                align-items: center;
            }

            .feature-icon {
                width: 60px;
                height: 60px;
                border-radius: 16px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.4rem;
            }
        </style>
    </head>

    <body>

        <!-- Transparent Navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark position-absolute w-100" style="z-index: 10;">
            <div class="container">
                <a class="navbar-brand fw-bold fs-3" href="#">
                    <i class="fa-solid fa-hotel me-2 text-info"></i>Ocean View
                </a>
                <div class="d-flex">
                    <a href="${pageContext.request.contextPath}/dashboard"
                        class="btn btn-outline-light px-4 rounded-pill fw-bold">
                        <i class="fa-solid fa-lock me-2"></i>Staff Portal
                    </a>
                </div>
            </div>
        </nav>

        <!-- Hero Section -->
        <section class="hero text-white">
            <div class="container animate-fade-in">
                <div class="row align-items-center justify-content-center">
                    <div class="col-lg-8 text-center">
                        <h1 class="display-3 fw-bold mb-3" style="font-family: 'Outfit', sans-serif;">
                            Experience Ultimate Luxury by the Ocean.
                        </h1>
                        <p class="fs-5 fw-light opacity-75 mb-5">
                            Immerse yourself in world-class amenities and breathtaking views.
                            Our staff is ready to serve you.
                        </p>

                        <div class="glass-panel p-4 mx-auto"
                            style="background: rgba(255,255,255,0.1); border-color: rgba(255,255,255,0.2); max-width: 500px;">
                            <h4 class="fw-bold mb-3">
                                <i class="fa-solid fa-bell-concierge me-2"></i>Enterprise Booking System
                            </h4>
                            <p class="small opacity-75 mb-4">
                                Internal staff use only. Please contact reception to authorize a booking session,
                                or login via the secure portal.
                            </p>
                            <a href="${pageContext.request.contextPath}/dashboard"
                                class="btn btn-info px-4 py-2 fw-bold w-100 text-dark" style="border-radius: 10px;">
                                Access Internal Dashboard <i class="fa-solid fa-arrow-right ms-2"></i>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>