package com.smartcanteen.servlet;

import com.smartcanteen.model.Cart;
import com.smartcanteen.model.Order;
import com.smartcanteen.model.OrderItem;
import com.smartcanteen.model.PickupSlot;
import com.smartcanteen.service.FoodService;
import com.smartcanteen.service.OrderService;
import com.smartcanteen.service.ServiceException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * GET  /checkout -> order summary + pickup time + payment method
 * POST /checkout -> "Pay & Place Order": the order is created by OrderService
 */
@WebServlet("/checkout")
public class CheckoutServlet extends BaseServlet {

    private static final DateTimeFormatter LABEL_FORMAT = DateTimeFormatter.ofPattern("dd MMM", Locale.ENGLISH);

    private final FoodService foodService = new FoodService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showCheckoutPage(request, response, null);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = getCart(request);
        try {
            Order order = orderService.placeOrder(
                    getLoggedInUser(request),
                    cart,
                    request.getParameter("pickupDate"),
                    request.getParameter("pickupSlot"),
                    request.getParameter("paymentMethod"));

            cart.clear();   // the cart is emptied only after the order is saved
            redirect(request, response, "/order-success?id=" + order.getOrderId());
        } catch (ServiceException e) {
            showCheckoutPage(request, response, e.getMessage());
        }
    }

    private void showCheckoutPage(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        Cart cart = getCart(request);
        try {
            foodService.refreshCart(cart);
            List<OrderItem> checkedItems = orderService.validateCart(cart);   // empty cart / unavailable food
            request.setAttribute("orderTotal", orderService.calculateTotal(checkedItems));
        } catch (ServiceException e) {
            flashError(request, e.getMessage());
            redirect(request, response, "/cart");
            return;
        }

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        request.setAttribute("slots", PickupSlot.getAllSlots());
        request.setAttribute("todayValue", today.toString());
        request.setAttribute("todayLabel", "Today (" + today.format(LABEL_FORMAT) + ")");
        request.setAttribute("tomorrowValue", tomorrow.toString());
        request.setAttribute("tomorrowLabel", "Tomorrow (" + tomorrow.format(LABEL_FORMAT) + ")");
        request.setAttribute("cutoffMinutes", orderService.getTodayCutoffMinutes());
        request.setAttribute("selectedDate", request.getParameter("pickupDate") != null
                ? request.getParameter("pickupDate")
                : (orderService.hasSlotsToday() ? today.toString() : tomorrow.toString()));
        request.setAttribute("error", errorMessage);
        forward(request, response, "checkout.jsp");
    }
}
