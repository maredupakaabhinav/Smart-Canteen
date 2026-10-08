package com.smartcanteen.servlet.admin;

import com.smartcanteen.model.FoodItem;
import com.smartcanteen.service.FoodService;
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
 * Admin food menu: list of all food items with the Available/Unavailable switch, Edit and Delete.
 */
@WebServlet("/admin/menu")
public class AdminMenuServlet extends BaseServlet {

    private final FoodService foodService = new FoodService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<FoodItem> foodItems = new ArrayList<>();
        try {
            foodItems = foodService.getFoodItems();
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.setAttribute("foodItems", foodItems);
        forward(request, response, "admin/menu.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        int foodId = parseInt(request.getParameter("foodId"), -1);

        try {
            if ("availability".equals(action)) {
                boolean makeAvailable = "true".equals(request.getParameter("available"));
                foodService.setAvailability(foodId, makeAvailable);
                flashSuccess(request, makeAvailable ? "Food item marked as available."
                                                    : "Food item marked as unavailable.");
            } else if ("delete".equals(action)) {
                foodService.deleteFood(foodId);
                flashSuccess(request, "Food item deleted.");
            } else {
                flashError(request, "Invalid request.");
            }
        } catch (ServiceException e) {
            flashError(request, e.getMessage());
        }
        redirect(request, response, "/admin/menu");
    }
}
