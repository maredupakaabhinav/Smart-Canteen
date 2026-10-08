package com.smartcanteen.servlet;

import com.smartcanteen.service.OrderService;
import com.smartcanteen.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Shows the profile of the logged-in student. The password is never shown.
 */
@WebServlet("/profile")
public class ProfileServlet extends BaseServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("orderCount", orderService.getOrdersForUser(getLoggedInUser(request).getId()).size());
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        forward(request, response, "profile.jsp");
    }
}
