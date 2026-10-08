package com.smartcanteen.servlet.admin;

import com.smartcanteen.model.Order;
import com.smartcanteen.service.FoodService;
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
 * Admin dashboard: four summary numbers and the latest orders.
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends BaseServlet {

    private final FoodService foodService = new FoodService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("totalFood", foodService.countAllFood());
            request.setAttribute("availableFood", foodService.countAvailableFood());
            request.setAttribute("pendingOrders", orderService.countPendingOrders());
            request.setAttribute("completedOrders", orderService.countCompletedOrders());

            List<Order> allOrders = orderService.getAllOrders();
            request.setAttribute("latestOrders",
                    new ArrayList<>(allOrders.subList(0, Math.min(5, allOrders.size()))));
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        forward(request, response, "admin/dashboard.jsp");
    }
}
