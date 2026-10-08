package com.smartcanteen.servlet;

import com.smartcanteen.service.OrderService;
import com.smartcanteen.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * POST /cancel-order -> cancels an order. The 5-minute rule is checked in OrderService (Java),
 * so hiding the button is only for looks - the server always checks again.
 */
@WebServlet("/cancel-order")
public class CancelOrderServlet extends BaseServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        redirect(request, response, "/orders");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = parseInt(request.getParameter("orderId"), -1);
        try {
            orderService.cancelOrder(orderId, getLoggedInUser(request).getId());
            flashSuccess(request, "Order SC" + orderId + " has been cancelled.");
        } catch (ServiceException e) {
            flashError(request, e.getMessage());
        }
        redirect(request, response, "/order-details?id=" + orderId);
    }
}
