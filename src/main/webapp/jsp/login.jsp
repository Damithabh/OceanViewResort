<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/images/favicon.png">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login | Ocean View Resort</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body class="hero-bg">

        <div class="container animate-fade-in" style="z-index: 2; position: relative;">
            <div class="row justify-content-center">
                <div class="col-md-5">
                    <div class="glass-panel text-center">
                        <h2 class="mb-1" style="color: var(--primary-color); font-weight: 700;">
                            <i class="fa-solid fa-umbrella-beach me-2"></i>Ocean View Resort
                        </h2>
                        <p class="text-muted mb-4">Staff & Admin Portal — Secure Login</p>

                        <% String error=request.getParameter("error"); if ("invalid_credentials".equals(error)) { %>
                            <div class="alert alert-danger py-2" role="alert">
                                <i class="fa-solid fa-circle-exclamation me-2"></i>Invalid Username or Password.
                            </div>
                            <% } else if ("unauthorized".equals(error)) { %>
                                <div class="alert alert-warning py-2" role="alert">
                                    <i class="fa-solid fa-shield-exclamation me-2"></i>Please login to access the
                                    system.
                                </div>
                                <% } else if ("username_taken".equals(error)) { %>
                                    <div class="alert alert-danger py-2" role="alert">
                                        <i class="fa-solid fa-circle-exclamation me-2"></i>Username is already taken.
                                    </div>
                                    <% } %>

                                        <% String msg=request.getParameter("msg"); if ("logged_out".equals(msg)) { %>
                                            <div class="alert alert-success py-2" role="alert">
                                                <i class="fa-solid fa-check-circle me-2"></i>Successfully logged out.
                                            </div>
                                            <% } else if ("registered".equals(msg)) { %>
                                                <div class="alert alert-success py-2" role="alert">
                                                    <i class="fa-solid fa-check-circle me-2"></i>Registration
                                                    successful. Please login.
                                                </div>
                                                <% } %>

                                                    <form action="${pageContext.request.contextPath}/auth" method="POST"
                                                        id="loginForm">
                                                        <input type="hidden" name="action" value="login">

                                                        <div class="form-floating mb-3 text-start">
                                                            <input type="text" class="form-control" id="username"
                                                                name="username" placeholder="Username" required
                                                                autocomplete="username">
                                                            <label for="username"><i
                                                                    class="fa-regular fa-user me-2"></i>Username</label>
                                                        </div>

                                                        <div class="form-floating mb-4 text-start">
                                                            <input type="password" class="form-control" id="password"
                                                                name="password" placeholder="Password" required
                                                                autocomplete="current-password">
                                                            <label for="password"><i
                                                                    class="fa-solid fa-lock me-2"></i>Password</label>
                                                        </div>

                                                        <button type="submit"
                                                            class="btn btn-antigravity w-100 py-2 fs-5" id="loginBtn">
                                                            <i class="fa-solid fa-right-to-bracket me-2"></i>Secure
                                                            Login
                                                        </button>
                                                    </form>

                                                    <div class="mt-4 text-muted small">
                                                        <p class="mb-1">&copy; 2026 Ocean View Resort Enterprise. All
                                                            Rights Reserved.</p>
                                                        <p class="fst-italic opacity-75">Architecture: 3-Tier MVC |
                                                            Secure SHA-256 Auth</p>
                                                    </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

    </html>