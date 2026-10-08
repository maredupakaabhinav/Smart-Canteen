package com.smartcanteen.servlet;

import com.smartcanteen.model.FoodItem;
import com.smartcanteen.model.User;
import com.smartcanteen.service.FoodService;
import com.smartcanteen.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The food menu. Anyone can look at it; only logged-in students get the "Add to Cart" button.
 */
@WebServlet("/menu")
public class MenuServlet extends BaseServlet {

    private final FoodService foodService = new FoodService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedInUser(request);
        if (user != null && User.ROLE_ADMIN.equals(user.getRole())) {
            redirect(request, response, "/admin/menu");   // admins manage food in the admin panel
            return;
        }

        List<FoodItem> foodItems = new ArrayList<>();
        try {
            foodItems = foodService.getFoodItems();
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }
        request.setAttribute("foodItems", foodItems);
        request.setAttribute("categories", FoodItem.CATEGORIES);
        forward(request, response, "menu.jsp");
    }
}
