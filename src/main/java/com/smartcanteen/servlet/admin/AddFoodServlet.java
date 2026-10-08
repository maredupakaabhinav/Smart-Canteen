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

/**
 * GET  /admin/add-food -> empty form
 * POST /admin/add-food -> saves the new food item
 */
@WebServlet("/admin/add-food")
public class AddFoodServlet extends BaseServlet {

    private final FoodService foodService = new FoodService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("food", new FoodItem());
        request.setAttribute("categories", FoodItem.CATEGORIES);
        forward(request, response, "admin/add-food.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FoodItem food = readFoodForm(request);
        try {
            foodService.addFood(food);
            flashSuccess(request, "\"" + food.getName() + "\" was added to the menu.");
            redirect(request, response, "/admin/menu");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("food", food);   // keep what the admin typed
            request.setAttribute("categories", FoodItem.CATEGORIES);
            forward(request, response, "admin/add-food.jsp");
        }
    }
}
