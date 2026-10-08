package com.smartcanteen.servlet;

import com.smartcanteen.model.Cart;
import com.smartcanteen.model.CartItem;
import com.smartcanteen.model.FoodItem;
import com.smartcanteen.service.FoodService;
import com.smartcanteen.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * GET  /cart -> shows the cart page
 * POST /cart -> add / update / remove an item. JavaScript (fetch) calls this and gets a small JSON answer.
 * The cart lives in the HttpSession on the server, so it is not just a browser trick.
 */
@WebServlet("/cart")
public class CartServlet extends BaseServlet {

    private final FoodService foodService = new FoodService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = getCart(request);
        try {
            foodService.refreshCart(cart);   // latest prices + availability from the database
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
        }

        boolean hasUnavailableItem = false;
        for (CartItem item : cart.getItems()) {
            if (!item.getFoodItem().isAvailable()) {
                hasUnavailableItem = true;
            }
        }
        request.setAttribute("hasUnavailableItem", hasUnavailableItem);
        forward(request, response, "cart.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = getCart(request);
        String action = request.getParameter("action");
        int foodId = parseInt(request.getParameter("foodId"), -1);
        int quantity = parseInt(request.getParameter("quantity"), -1);

        try {
            String message;
            if ("add".equals(action)) {
                FoodItem food = foodService.getFoodById(foodId);
                if (!food.isAvailable()) {
                    throw new ServiceException("This item is currently unavailable.");
                }
                cart.addItem(food, quantity);
                message = food.getName() + " added to your cart.";
            } else if ("update".equals(action)) {
                foodService.refreshCart(cart);
                cart.updateQuantity(foodId, quantity);
                message = "Cart updated.";
            } else if ("remove".equals(action)) {
                cart.removeItem(foodId);
                message = "Item removed.";
            } else {
                throw new ServiceException("Invalid request.");
            }
            writeJson(response, true, message, cart, foodId);

        } catch (ServiceException | IllegalArgumentException e) {
            // IllegalArgumentException comes from Cart (invalid quantity)
            writeJson(response, false, e.getMessage(), cart, foodId);
        }
    }

    /** Builds the small JSON answer by hand, e.g. {"success":true,"message":"Cart updated.","cartCount":3,...} */
    private void writeJson(HttpServletResponse response, boolean success, String message, Cart cart, int foodId)
            throws IOException {
        CartItem item = cart.getItem(foodId);
        int itemQuantity = item == null ? 0 : item.getQuantity();
        double itemSubtotal = item == null ? 0 : item.getSubtotal();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        PrintWriter out = response.getWriter();
        out.print(String.format(Locale.ROOT,
                "{\"success\":%b,\"message\":\"%s\",\"cartCount\":%d,\"itemQuantity\":%d,"
                        + "\"itemSubtotal\":\"%.2f\",\"total\":\"%.2f\"}",
                success, escapeJson(message), cart.getTotalQuantity(), itemQuantity, itemSubtotal, cart.getTotalAmount()));
    }

    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
