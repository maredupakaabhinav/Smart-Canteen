package com.smartcanteen.servlet.admin;

import com.smartcanteen.model.Order;
import com.smartcanteen.model.OrderStatus;
import com.smartcanteen.service.OrderService;
import com.smartcanteen.service.ServiceException;
import com.smartcanteen.servlet.BaseServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin order management: see all orders and change their status (saved in the database).
 */
@WebServlet("/admin/orders")
public class AdminOrdersServlet extends BaseServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Order> orders = new ArrayList<>();
        try {
            orders = orderService.getAllOrders();
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.setAttribute("orders", orders);
        request.setAttribute("statuses", OrderStatus.values());
        forward(request, response, "admin/orders.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = parseInt(request.getParameter("orderId"), -1);
        try {
            String statusText = request.getParameter("status");
            orderService.updateOrderStatus(orderId, statusText);
            flashSuccess(request, "Order SC" + orderId + " is now \"" + OrderStatus.fromText(statusText).getLabel() + "\".");
        } catch (ServiceException e) {
            flashError(request, e.getMessage());
        }
        redirect(request, response, "/admin/orders");
    }
}
