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
 * GET  /admin/edit-food?id=3 -> form filled with the current values
 * POST /admin/edit-food      -> saves the changes
 */
@WebServlet("/admin/edit-food")
public class EditFoodServlet extends BaseServlet {

    private final FoodService foodService = new FoodService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            FoodItem food = foodService.getFoodById(parseInt(request.getParameter("id"), -1));
            request.setAttribute("food", food);
            request.setAttribute("categories", FoodItem.CATEGORIES);
            forward(request, response, "admin/edit-food.jsp");
        } catch (ServiceException e) {
            flashError(request, e.getMessage());
            redirect(request, response, "/admin/menu");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        FoodItem food = readFoodForm(request);
        food.setFoodId(parseInt(request.getParameter("foodId"), -1));
        try {
            foodService.updateFood(food);
            flashSuccess(request, "\"" + food.getName() + "\" was updated.");
            redirect(request, response, "/admin/menu");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("food", food);
            request.setAttribute("categories", FoodItem.CATEGORIES);
            forward(request, response, "admin/edit-food.jsp");
        }
    }
}
