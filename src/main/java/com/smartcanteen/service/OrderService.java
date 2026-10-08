package com.smartcanteen.service;

import com.smartcanteen.dao.FoodItemDAO;
import com.smartcanteen.dao.OrderDAO;
import com.smartcanteen.dao.PaymentDAO;
import com.smartcanteen.model.Cart;
import com.smartcanteen.model.CartItem;
import com.smartcanteen.model.FoodItem;
import com.smartcanteen.model.Order;
import com.smartcanteen.model.OrderItem;
import com.smartcanteen.model.OrderStatus;
import com.smartcanteen.model.Payment;
import com.smartcanteen.model.PickupSlot;
import com.smartcanteen.model.User;
import com.smartcanteen.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The most important service: all the business rules for orders are here.
 * (validate cart, calculate total, place order, cancellation rule, update status)
 */
public class OrderService {

    /** An order can be cancelled only during the first 5 minutes. */
    public static final int CANCELLATION_MINUTES = 5;

    /** A pickup slot must be at least this many minutes away. */
    private static final int PICKUP_LEAD_MINUTES = 10;

    private final OrderDAO orderDAO = new OrderDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final FoodItemDAO foodItemDAO = new FoodItemDAO();

    // COLLECTIONS + POLYMORPHISM: each payment method name maps to a PaymentService object
    private final Map<String, PaymentService> paymentServices = new HashMap<>();

    public OrderService() {
        paymentServices.put(Payment.METHOD_UPI, new DemoPaymentService(Payment.METHOD_UPI));
        paymentServices.put(Payment.METHOD_CARD, new DemoPaymentService(Payment.METHOD_CARD));
        paymentServices.put(Payment.METHOD_CASH, new CashPaymentService());
    }

    // ------------------------------------------------------------------
    //  Cart checks and total
    // ------------------------------------------------------------------

    /**
     * Checks the cart against the DATABASE (not against what the browser says).
     * Returns the order lines with the real prices. Throws if the cart is empty or an item is unavailable.
     */
    public List<OrderItem> validateCart(Cart cart) throws ServiceException {
        if (cart == null || cart.isEmpty()) {
            throw new ServiceException("Your cart is empty.");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        try {
            for (CartItem cartItem : cart.getItems()) {
                FoodItem food = foodItemDAO.findById(cartItem.getFoodId());
                if (food == null) {
                    throw new ServiceException("An item in your cart is no longer on the menu. Please check your cart.");
                }
                if (!food.isAvailable()) {
                    throw new ServiceException("This item is currently unavailable: " + food.getName() + ".");
                }
                if (cartItem.getQuantity() < 1 || cartItem.getQuantity() > Cart.MAX_QUANTITY_PER_ITEM) {
                    throw new ServiceException("Please enter a valid quantity.");
                }
                orderItems.add(new OrderItem(food.getFoodId(), food.getName(), cartItem.getQuantity(), food.getPrice()));
            }
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
        return orderItems;
    }

    /** Adds up price x quantity for every line. */
    public double calculateTotal(List<OrderItem> orderItems) {
        double total = 0;
        for (OrderItem item : orderItems) {
            total += item.getSubtotal();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    // ------------------------------------------------------------------
    //  Pickup slots
    // ------------------------------------------------------------------

    /** Minutes since midnight of the earliest slot that can still be booked today (1440 = none left). */
    public int getTodayCutoffMinutes() {
        LocalDateTime earliest = LocalDateTime.now().plusMinutes(PICKUP_LEAD_MINUTES);
        if (earliest.toLocalDate().isAfter(LocalDate.now())) {
            return 24 * 60;
        }
        return earliest.getHour() * 60 + earliest.getMinute();
    }

    public boolean hasSlotsToday() {
        int cutoff = getTodayCutoffMinutes();
        for (PickupSlot slot : PickupSlot.getAllSlots()) {
            if (slot.getMinutesOfDay() >= cutoff) {
                return true;
            }
        }
        return false;
    }

    private LocalDateTime parsePickupTime(String dateText, String slotValue) throws ServiceException {
        String errorMessage = "Please choose a valid pickup date and time.";
        if (dateText == null || slotValue == null) {
            throw new ServiceException(errorMessage);
        }

        LocalDate pickupDate;
        try {
            pickupDate = LocalDate.parse(dateText.trim());
        } catch (DateTimeParseException e) {
            throw new ServiceException(errorMessage);
        }
        PickupSlot slot = PickupSlot.findByValue(slotValue.trim());
        LocalDate today = LocalDate.now();
        if (slot == null || pickupDate.isBefore(today) || pickupDate.isAfter(today.plusDays(1))) {
            throw new ServiceException(errorMessage);
        }

        LocalDateTime pickupTime = LocalDateTime.of(pickupDate, slot.getTime());
        if (pickupTime.isBefore(LocalDateTime.now().plusMinutes(PICKUP_LEAD_MINUTES))) {
            throw new ServiceException("That pickup time has already passed or is too soon. Please choose a later slot.");
        }
        return pickupTime;
    }

    // ------------------------------------------------------------------
    //  Placing an order
    // ------------------------------------------------------------------

    /**
     * Validates everything, takes the (demo) payment and saves the order + its payment.
     * The order and the payment are saved in ONE database transaction:
     * either both are saved or nothing is saved.
     */
    public Order placeOrder(User user, Cart cart, String pickupDateText, String pickupSlotValue,
                            String paymentMethod) throws ServiceException {
        List<OrderItem> orderItems = validateCart(cart);
        LocalDateTime pickupTime = parsePickupTime(pickupDateText, pickupSlotValue);

        PaymentService paymentService = paymentServices.get(paymentMethod);
        if (paymentService == null) {
            throw new ServiceException("Please select a payment method.");
        }

        double totalAmount = calculateTotal(orderItems);
        if (!paymentService.processPayment(totalAmount)) {
            throw new ServiceException("Payment failed. Please try again.");
        }

        // Whole seconds only: the DATETIME column has no fractions, so MySQL would round them (maybe into the future)
        LocalDateTime now = LocalDateTime.now().withNano(0);
        Order order = new Order();
        order.setUserId(user.getId());
        order.setTotalAmount(totalAmount);
        order.setOrderDate(now);
        order.setPickupTime(pickupTime);
        order.setStatus(OrderStatus.PLACED);
        order.setItems(orderItems);

        Payment payment = new Payment(0, totalAmount, paymentService.getPaymentMethod(),
                paymentService.getStatusAfterPayment(), paymentService.createTransactionId(), now);

        try (Connection connection = DBConnection.getConnection()) {
            try {
                connection.setAutoCommit(false);          // start the transaction
                orderDAO.createOrder(connection, order);
                payment.setOrderId(order.getOrderId());
                paymentDAO.savePayment(connection, payment);
                connection.commit();                      // save everything
            } catch (SQLException e) {
                connection.rollback();                    // undo everything
                throw e;
            }
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }

        order.setPayment(payment);
        return order;
    }

    // ------------------------------------------------------------------
    //  Reading orders
    // ------------------------------------------------------------------

    public List<Order> getOrdersForUser(int userId) throws ServiceException {
        try {
            return orderDAO.findByUserId(userId);
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public List<Order> getAllOrders() throws ServiceException {
        try {
            return orderDAO.findAll();
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    /** A student can only open his or her own orders. */
    public Order getOrderForUser(int orderId, int userId) throws ServiceException {
        try {
            Order order = orderDAO.findById(orderId);
            if (order == null || order.getUserId() != userId) {
                throw new ServiceException("Order not found.");
            }
            return order;
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    /** The active order (not completed / cancelled) with the earliest pickup time, or null. */
    public Order getUpcomingPickup(List<Order> orders) {
        Order upcoming = null;
        for (Order order : orders) {
            boolean active = !order.getStatus().isFinished();
            if (active && (upcoming == null || order.getPickupTime().isBefore(upcoming.getPickupTime()))) {
                upcoming = order;
            }
        }
        return upcoming;
    }

    public int countPendingOrders() throws ServiceException {
        try {
            return orderDAO.countPending();
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    public int countCompletedOrders() throws ServiceException {
        try {
            return orderDAO.countByStatus(OrderStatus.COMPLETED);
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    // ------------------------------------------------------------------
    //  Cancellation rule
    // ------------------------------------------------------------------

    /** Seconds left in the cancellation period (0 when it is over). */
    public long getCancelSecondsLeft(Order order) {
        long allowedSeconds = CANCELLATION_MINUTES * 60L;
        long secondsPassed = Duration.between(order.getOrderDate(), LocalDateTime.now()).getSeconds();
        return Math.min(allowedSeconds, Math.max(0, allowedSeconds - secondsPassed));
    }

    public boolean isWithinCancellationPeriod(Order order) {
        return getCancelSecondsLeft(order) > 0;
    }

    /** Returns the reason why the order cannot be cancelled, or null if it CAN be cancelled. */
    public String getCancelBlockedReason(Order order) {
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return "This order has already been cancelled.";
        }
        if (order.getStatus() != OrderStatus.PLACED) {
            return "This order is already being prepared, so it can no longer be cancelled.";
        }
        if (!isWithinCancellationPeriod(order)) {
            return "Cancellation period has expired.";
        }
        return null;
    }

    public boolean canCancel(Order order) {
        return getCancelBlockedReason(order) == null;
    }

    /** Cancels the order of this user - but only if the cancellation rule allows it. */
    public void cancelOrder(int orderId, int userId) throws ServiceException {
        Order order = getOrderForUser(orderId, userId);
        String blockedReason = getCancelBlockedReason(order);
        if (blockedReason != null) {
            throw new ServiceException(blockedReason);
        }

        try (Connection connection = DBConnection.getConnection()) {
            try {
                connection.setAutoCommit(false);
                if (!orderDAO.cancelOrder(connection, orderId)) {
                    connection.rollback();
                    throw new ServiceException("This order can no longer be cancelled.");
                }
                refundPayment(connection, order.getPayment());
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    // ------------------------------------------------------------------
    //  Admin: update status
    // ------------------------------------------------------------------

    public void updateOrderStatus(int orderId, String statusText) throws ServiceException {
        OrderStatus newStatus = OrderStatus.fromText(statusText);
        if (newStatus == null) {
            throw new ServiceException("Please choose a valid order status.");
        }

        try (Connection connection = DBConnection.getConnection()) {
            Order order = orderDAO.findById(orderId);
            if (order == null) {
                throw new ServiceException("Order not found.");
            }
            if (order.getStatus().isFinished()) {
                throw new ServiceException("A completed or cancelled order cannot be changed.");
            }
            if (order.getStatus() == newStatus) {
                throw new ServiceException("The order already has this status.");
            }

            try {
                connection.setAutoCommit(false);
                if (!orderDAO.updateStatus(connection, orderId, newStatus)) {
                    connection.rollback();
                    throw new ServiceException("The order status could not be changed.");
                }
                updatePaymentForNewStatus(connection, order.getPayment(), newStatus);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    /** Keeps the payment in step with the order: cash is collected on COMPLETED, money goes back on CANCELLED. */
    private void updatePaymentForNewStatus(Connection connection, Payment payment, OrderStatus newStatus)
            throws SQLException {
        if (payment == null) {
            return;
        }
        if (newStatus == OrderStatus.CANCELLED) {
            refundPayment(connection, payment);
        } else if (newStatus == OrderStatus.COMPLETED && Payment.STATUS_PENDING.equals(payment.getPaymentStatus())) {
            paymentDAO.updateStatus(connection, payment.getOrderId(), Payment.STATUS_SUCCESS);   // cash collected
        }
    }

    private void refundPayment(Connection connection, Payment payment) throws SQLException {
        if (payment == null) {
            return;
        }
        if (Payment.STATUS_SUCCESS.equals(payment.getPaymentStatus())) {
            paymentDAO.updateStatus(connection, payment.getOrderId(), Payment.STATUS_REFUNDED);
        } else if (Payment.STATUS_PENDING.equals(payment.getPaymentStatus())) {
            paymentDAO.updateStatus(connection, payment.getOrderId(), Payment.STATUS_CANCELLED);
        }
    }
}
