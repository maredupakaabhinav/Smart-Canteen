package com.smartcanteen.filter;

import com.smartcanteen.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Runs BEFORE the servlets listed below. It checks two things:
 *   1. is somebody logged in?        (if not -> go to the login page)
 *   2. does the role match the page? (students cannot open /admin/..., admins cannot open student pages)
 */
@WebFilter(urlPatterns = {
        "/dashboard", "/cart", "/checkout", "/order-success", "/orders",
        "/order-details", "/cancel-order", "/profile", "/admin/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession(false);
        User user = session == null ? null : (User) session.getAttribute("loggedInUser");

        if (user == null) {
            request.getSession().setAttribute("flashError", "Please log in to continue.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        boolean isAdminPage = request.getServletPath().startsWith("/admin");
        boolean isAdminUser = User.ROLE_ADMIN.equals(user.getRole());
        if (isAdminPage != isAdminUser) {
            session.setAttribute("flashError", "You are not allowed to open that page.");
            response.sendRedirect(request.getContextPath() + user.getDashboardPath());
            return;
        }

        // Stop the browser from showing these pages again from its cache after logout (Back button)
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        chain.doFilter(request, response);
    }
}
