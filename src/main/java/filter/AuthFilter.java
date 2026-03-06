package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet Filter for centralized authentication enforcement.
 * Intercepts requests to protected URLs and redirects unauthenticated
 * users to the login page.
 * 
 * Why Filter: Eliminates duplicated session-check boilerplate in every
 * servlet. All protected routes are secured in a single location.
 * 
 * @author Ocean View Resort Dev Team
 */
@WebFilter(urlPatterns = { "/dashboard", "/manage-reservations", "/manage-rooms", "/reports" })
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            // Redirect to login with unauthorized indicator
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/login.jsp?error=unauthorized");
            return;
        }

        // User is authenticated — proceed with the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup required
    }
}
