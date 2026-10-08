package com.smartcanteen.dao;

import com.smartcanteen.model.Order;
import com.smartcanteen.model.OrderItem;
import com.smartcanteen.model.OrderStatus;
import com.smartcanteen.model.Payment;
import com.smartcanteen.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO for the orders and order_items tables.
 * Methods that take a Connection are used inside a transaction started by OrderService.
 */
public class OrderDAO {

    // One query that gets the order, the customer and the payment together
    private static final String SELECT_ORDERS =
            "SELECT o.id, o.user_id, o.total_amount, o.order_date, o.pickup_time, o.status, "
            + "u.name AS customer_name, u.college_id, "
            + "p.id AS payment_id, p.amount, p.payment_method, p.payment_status, p.transaction_id, p.payment_date "
            + "FROM orders o "
            + "JOIN users u ON o.user_id = u.id "
            + "LEFT JOIN payments p ON p.order_id = o.id ";

    /** Saves the order and all its items. Sets the generated order id on the Order object. */
    public void createOrder(Connection connection, Order order) throws SQLException {
        String orderSql = "INSERT INTO orders (user_id, total_amount, order_date, pickup_time, status) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getUserId());
            statement.setDouble(2, order.getTotalAmount());
            statement.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
            statement.setTimestamp(4, Timestamp.valueOf(order.getPickupTime()));
            statement.setString(5, order.getStatus().name());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setOrderId(keys.getInt(1));
                }
            }
        }

        String itemSql = "INSERT INTO order_items (order_id, food_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(itemSql)) {
            for (OrderItem item : order.getItems()) {
                statement.setInt(1, order.getOrderId());
                statement.setInt(2, item.getFoodId());
                statement.setInt(3, item.getQuantity());
                statement.setDouble(4, item.getPrice());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    /** Returns null if there is no such order. */
    public Order findById(int orderId) throws SQLException {
        List<Order> orders = queryOrders(SELECT_ORDERS + "WHERE o.id = ?", orderId);
        return orders.isEmpty() ? null : orders.get(0);
    }

    /** All orders of one user, newest first. */
    public List<Order> findByUserId(int userId) throws SQLException {
        return queryOrders(SELECT_ORDERS + "WHERE o.user_id = ? ORDER BY o.id DESC", userId);
    }

    /** Every order (for the admin), newest first. */
    public List<Order> findAll() throws SQLException {
        return queryOrders(SELECT_ORDERS + "ORDER BY o.id DESC LIMIT 500");
    }

    /**
     * Changes the status. Orders that are already COMPLETED or CANCELLED are never changed.
     * Returns true if a row was updated.
     */
    public boolean updateStatus(Connection connection, int orderId, OrderStatus newStatus) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE id = ? AND status NOT IN ('COMPLETED', 'CANCELLED')";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus.name());
            statement.setInt(2, orderId);
            return statement.executeUpdate() > 0;
        }
    }

    /**
     * Cancels an order, but only if it is still PLACED.
     * (If the canteen already started preparing it a moment ago, nothing is changed.)
     */
    public boolean cancelOrder(Connection connection, int orderId) throws SQLException {
        String sql = "UPDATE orders SET status = 'CANCELLED' WHERE id = ? AND status = 'PLACED'";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            return statement.executeUpdate() > 0;
        }
    }

    public int countByStatus(OrderStatus status) throws SQLException {
        return queryCount("SELECT COUNT(*) FROM orders WHERE status = ?", status.name());
    }

    /** Orders that are still active: PLACED, PREPARING or READY. */
    public int countPending() throws SQLException {
        return queryCount("SELECT COUNT(*) FROM orders WHERE status IN ('PLACED', 'PREPARING', 'READY')");
    }

    // ---------- private helpers ----------

    private int queryCount(String sql, Object... parameters) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private List<Order> queryOrders(String sql, Object... parameters) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        orders.add(mapOrder(rs));
                    }
                }
            }
            loadItems(connection, orders);
        }
        return orders;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        order.setPickupTime(rs.getTimestamp("pickup_time").toLocalDateTime());
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerCollegeId(rs.getString("college_id"));

        if (rs.getObject("payment_id") != null) {
            Payment payment = new Payment();
            payment.setPaymentId(rs.getInt("payment_id"));
            payment.setOrderId(order.getOrderId());
            payment.setAmount(rs.getDouble("amount"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setPaymentStatus(rs.getString("payment_status"));
            payment.setTransactionId(rs.getString("transaction_id"));
            payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
            order.setPayment(payment);
        }
        return order;
    }

    /** Loads the food items of all the given orders with ONE query and hands them to the right order. */
    private void loadItems(Connection connection, List<Order> orders) throws SQLException {
        if (orders.isEmpty()) {
            return;
        }

        Map<Integer, Order> ordersById = new HashMap<>();
        StringBuilder placeholders = new StringBuilder();
        for (Order order : orders) {
            ordersById.put(order.getOrderId(), order);
            placeholders.append(placeholders.length() == 0 ? "?" : ", ?");
        }

        String sql = "SELECT oi.id, oi.order_id, oi.food_id, oi.quantity, oi.price, f.name "
                + "FROM order_items oi JOIN food_items f ON oi.food_id = f.id "
                + "WHERE oi.order_id IN (" + placeholders + ") ORDER BY oi.id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int position = 1;
            for (Order order : orders) {
                statement.setInt(position++, order.getOrderId());
            }
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setOrderItemId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setFoodId(rs.getInt("food_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("price"));
                    item.setFoodName(rs.getString("name"));
                    ordersById.get(item.getOrderId()).getItems().add(item);
                }
            }
        }
    }
}
