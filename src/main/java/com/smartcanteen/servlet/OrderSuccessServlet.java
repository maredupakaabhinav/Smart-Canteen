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
 * The confirmation page shown after "Pay & Place Order". Everything shown comes from the database.
 */
@WebServlet("/order-success")
public class OrderSuccessServlet extends BaseServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int orderId = parseInt(request.getParameter("id"), -1);
        try {
            Order order = orderService.getOrderForUser(orderId, getLoggedInUser(request).getId());
            request.setAttribute("order", order);
            forward(request, response, "order-success.jsp");
        } catch (ServiceException e) {
            flashError(request, e.getMessage());
            redirect(request, response, "/orders");
        }
    }
}
