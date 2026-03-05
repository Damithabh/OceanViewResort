<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login | Ocean View Resort</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Antigravity Custom Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="hero-bg">
    <!-- Pseudo-background gradient overlay for contrast -->
    <div style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: rgba(10, 37, 64, 0.6); z-index: 0;"></div>

    <div class="container animate-fade-in" style="z-index: 1;">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="glass-panel text-center">
                    <h2 class="mb-4" style="color: var(--primary-color); font-weight: 700;">
                        <i class="fa-solid fa-umbrella-beach me-2"></i>Ocean View Resort
                    </h2>
                    <p class="text-muted mb-4">Staff & Admin Portal Secure Login</p>
                    
                    <% 
                        String error = request.getParameter("error");
                        if(error != null && error.equals("invalid_credentials")) { 
                    %>
                        <div class="alert alert-danger" role="alert">
                            <i class="fa-solid fa-circle-exclamation me-2"></i>Invalid Username or Password.
                        </div>
                    <% } %>
                    
                    <% 
                        String msg = request.getParameter("msg");
                        if(msg != null && msg.equals("logged_out")) { 
                    %>
                        <div class="alert alert-success" role="alert">
                            <i class="fa-solid fa-check-circle me-2"></i>Successfully logged out.
                        </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/auth" method="POST">
                        <input type="hidden" name="action" value="login">
                        
                        <div class="form-floating mb-3 text-start">
                            <input type="text" class="form-control" id="username" name="username" placeholder="Username" required>
                            <label for="username"><i class="fa-regular fa-user me-2"></i>Username</label>
                        </div>
                        
                        <div class="form-floating mb-4 text-start">
                            <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                            <label for="password"><i class="fa-solid fa-lock me-2"></i>Password</label>
                        </div>
                        
                        <button type="submit" class="btn btn-antigravity w-100 py-2 fs-5">Secure Login</button>
                    </form>
                    
                    <div class="mt-4 text-muted small">
                        <p>&copy; 2026 Ocean View Resort Enterprise. All Rights Reserved.</p>
                        <p class="fst-italic opacity-75">Architecture: 3-Tier MVC | Secure SHA-256 Auth</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
