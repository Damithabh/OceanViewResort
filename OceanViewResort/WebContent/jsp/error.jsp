<%@ page isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Error Encountered | Ocean View</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
        <style>
            .error-bg {
                background: linear-gradient(-45deg, var(--primary-color), var(--text-main));
                height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
            }
        </style>
    </head>

    <body class="error-bg">

        <div class="container text-center animate-fade-in">
            <div class="row justify-content-center">
                <div class="col-md-6">
                    <div class="glass-panel text-white py-5 px-4"
                        style="background: rgba(255,255,255,0.1); border: 1px solid rgba(255,255,255,0.2);">

                        <i class="fa-solid fa-triangle-exclamation mb-4"
                            style="font-size: 5rem; color: var(--accent-color);"></i>

                        <h1 class="fw-bold mb-3 display-4">System Stop</h1>
                        <p class="fs-5 opacity-75 mb-4">
                            An unexpected request parameters exception occurred, or the backend failed to fulfill
                            constraints.
                        </p>

                        <!-- Purposefully NOT printing exception.printStackTrace() to obscure architecture details -->

                        <!-- Simple redirection home loop -->
                        <a href="${pageContext.request.contextPath}/dashboard"
                            class="btn btn-light btn-lg px-5 mt-3 fw-bold shadow">
                            <i class="fa-solid fa-house-chimney me-2"></i>Return to Sanctuary
                        </a>
                    </div>
                    <div class="mt-4 text-white-50 small">
                        <p>// Security Note: Error traces are deliberately concealed.</p>
                    </div>
                </div>
            </div>
        </div>

    </body>

    </html>