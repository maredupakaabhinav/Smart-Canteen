package com.smartcanteen.servlet;

import com.smartcanteen.model.FoodItem;
import com.smartcanteen.model.Order;
import com.smartcanteen.model.User;
import com.smartcanteen.service.FoodService;
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
 * Student dashboard: summary cards, a few menu items and the recent orders.
 */
@WebServlet("/dashboard")
public class DashboardServlet extends BaseServlet {

    private static final int MENU_PREVIEW_SIZE = 4;
    private static final int RECENT_ORDERS_SIZE = 5;

    private final FoodService foodService = new FoodService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedInUser(request);

        try {
            List<FoodItem> availableFood = foodService.getAvailableFoodItems();
            List<Order> orders = orderService.getOrdersForUser(user.getId());

            request.setAttribute("availableCount", availableFood.size());
            request.setAttribute("todaysMenu",
                    new ArrayList<>(availableFood.subList(0, Math.min(MENU_PREVIEW_SIZE, availableFood.size()))));
            request.setAttribute("orderCount", orders.size());
            request.setAttribute("recentOrders",
                    new ArrayList<>(orders.subList(0, Math.min(RECENT_ORDERS_SIZE, orders.size()))));
            request.setAttribute("upcomingPickup", orderService.getUpcomingPickup(orders));
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        forward(request, response, "dashboard.jsp");
    }
}
