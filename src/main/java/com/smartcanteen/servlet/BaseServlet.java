package com.smartcanteen.servlet;

import com.smartcanteen.model.Cart;
import com.smartcanteen.model.FoodItem;
import com.smartcanteen.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * INHERITANCE: all our servlets extend this class, so the small helper methods
 * that every servlet needs are written only once.
 */
public abstract class BaseServlet extends HttpServlet {

    private static final String VIEWS_FOLDER = "/WEB-INF/views/";

    /** The user stored in the session at login, or null if nobody is logged in. */
    protected User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (User) session.getAttribute("loggedInUser");
    }

    /** The shopping cart of this session (created the first time it is needed). */
    protected Cart getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    /** Shows a JSP page. The JSPs are inside WEB-INF, so they can only be opened through a servlet. */
    protected void forward(HttpServletRequest request, HttpServletResponse response, String viewName)
            throws ServletException, IOException {
        request.getRequestDispatcher(VIEWS_FOLDER + viewName).forward(request, response);
    }

    protected void redirect(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.sendRedirect(request.getContextPath() + path);
    }

    /** A "flash" message is shown once on the next page (green box). */
    protected void flashSuccess(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flashSuccess", message);
    }

    /** A "flash" error is shown once on the next page (red box). */
    protected void flashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute("flashError", message);
    }

    /** Converts text to a number; returns the fallback if the text is not a valid number. */
    protected int parseInt(String text, int fallback) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return fallback;
        }
    }

    /** Reads the add-food / edit-food form into a FoodItem object. */
    protected FoodItem readFoodForm(HttpServletRequest request) {
        FoodItem food = new FoodItem();
        food.setName(request.getParameter("name"));
        food.setDescription(request.getParameter("description"));
        food.setCategory(request.getParameter("category"));
        food.setImage(request.getParameter("image"));
        food.setAvailable("true".equals(request.getParameter("available")));
        try {
            double price = Double.parseDouble(request.getParameter("price").trim());
            food.setPrice(Double.isFinite(price) ? price : 0);
        } catch (NumberFormatException | NullPointerException e) {
            food.setPrice(0);   // FoodService will report "Please enter a valid price"
        }
        return food;
    }
}
