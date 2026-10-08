package com.smartcanteen.servlet;

import com.smartcanteen.model.Order;
import com.smartcanteen.service.OrderService;
import com.smartcanteen.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * One order with its items, payment, status tracker and the cancel button (if cancelling is allowed).
 * The status is read from the database every time the page is opened.
 */
@WebServlet("/order-details")
public class OrderDetailsServlet extends BaseServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = parseInt(request.getParameter("id"), -1);
        try {
            Order order = orderService.getOrderForUser(orderId, getLoggedInUser(request).getId());

            long secondsLeft = orderService.getCancelSecondsLeft(order);
            request.setAttribute("order", order);
            request.setAttribute("canCancel", orderService.canCancel(order));
            request.setAttribute("cancelBlockedReason", orderService.getCancelBlockedReason(order));
            request.setAttribute("cancelMinutesLeft", (secondsLeft + 59) / 60);   // rounded up
            request.setAttribute("cancellationMinutes", OrderService.CANCELLATION_MINUTES);
            forward(request, response, "order-details.jsp");
        } catch (ServiceException e) {
            flashError(request, e.getMessage());
            redirect(request, response, "/orders");
        }
    }
}
