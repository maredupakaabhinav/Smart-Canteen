package com.smartcanteen.servlet;

import com.smartcanteen.model.Order;
import com.smartcanteen.service.OrderService;
import com.smartcanteen.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * "My Orders": the list of all orders of the logged-in student.
 */
@WebServlet("/orders")
public class OrdersServlet extends BaseServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orders = new ArrayList<>();
        try {
            orders = orderService.getOrdersForUser(getLoggedInUser(request).getId());
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.setAttribute("orders", orders);
        forward(request, response, "orders.jsp");
    }
}
