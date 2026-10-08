package com.smartcanteen.dao;

import com.smartcanteen.model.Payment;
import com.smartcanteen.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * DAO for the payments table.
 * Methods that take a Connection are used inside a transaction started by OrderService.
 */
public class PaymentDAO {

    public int savePayment(Connection connection, Payment payment) throws SQLException {
        String sql = "INSERT INTO payments (order_id, amount, payment_method, payment_status, transaction_id, payment_date) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, payment.getOrderId());
            statement.setDouble(2, payment.getAmount());
            statement.setString(3, payment.getPaymentMethod());
            statement.setString(4, payment.getPaymentStatus());
            statement.setString(5, payment.getTransactionId());
            statement.setTimestamp(6, Timestamp.valueOf(payment.getPaymentDate()));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    /** Returns null if the order has no payment. */
    public Payment findByOrderId(Connection connection, int orderId) throws SQLException {
        String sql = "SELECT id, order_id, amount, payment_method, payment_status, transaction_id, payment_date "
                + "FROM payments WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Payment payment = new Payment();
                payment.setPaymentId(rs.getInt("id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setPaymentMethod(rs.getString("payment_method"));
                payment.setPaymentStatus(rs.getString("payment_status"));
                payment.setTransactionId(rs.getString("transaction_id"));
                payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
                return payment;
            }
        }
    }

    public Payment findByOrderId(int orderId) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            return findByOrderId(connection, orderId);
        }
    }

    public void updateStatus(Connection connection, int orderId, String newStatus) throws SQLException {
        String sql = "UPDATE payments SET payment_status = ? WHERE order_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newStatus);
            statement.setInt(2, orderId);
            statement.executeUpdate();
        }
    }
}
