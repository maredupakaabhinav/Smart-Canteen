package com.smartcanteen.dao;

import com.smartcanteen.model.FoodItem;
import com.smartcanteen.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for the food_items table: get, add, update, delete.
 */
public class FoodItemDAO {

    private static final String SELECT_COLUMNS =
            "SELECT id, name, description, category, price, image, available FROM food_items";

    public List<FoodItem> getAllFood() throws SQLException {
        return queryList(SELECT_COLUMNS + " ORDER BY FIELD(category, 'Breakfast', 'Snacks', 'Meals', 'Beverages'), name");
    }

    public List<FoodItem> getAvailableFood() throws SQLException {
        return queryList(SELECT_COLUMNS + " WHERE available = TRUE ORDER BY FIELD(category, 'Breakfast', 'Snacks', 'Meals', 'Beverages'), name");
    }

    /** Returns null if no food item has this id. */
    public FoodItem findById(int foodId) throws SQLException {
        String sql = SELECT_COLUMNS + " WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, foodId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int addFood(FoodItem food) throws SQLException {
        String sql = "INSERT INTO food_items (name, description, category, price, image, available) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, food.getName());
            statement.setString(2, food.getDescription());
            statement.setString(3, food.getCategory());
            statement.setDouble(4, food.getPrice());
            statement.setString(5, food.getImage());
            statement.setBoolean(6, food.isAvailable());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    /** Returns true if a row was updated. */
    public boolean updateFood(FoodItem food) throws SQLException {
        String sql = "UPDATE food_items SET name = ?, description = ?, category = ?, price = ?, "
                + "image = ?, available = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, food.getName());
            statement.setString(2, food.getDescription());
            statement.setString(3, food.getCategory());
            statement.setDouble(4, food.getPrice());
            statement.setString(5, food.getImage());
            statement.setBoolean(6, food.isAvailable());
            statement.setInt(7, food.getFoodId());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean setAvailability(int foodId, boolean available) throws SQLException {
        String sql = "UPDATE food_items SET available = ? WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, available);
            statement.setInt(2, foodId);
            return statement.executeUpdate() > 0;
        }
    }

    public boolean deleteFood(int foodId) throws SQLException {
        String sql = "DELETE FROM food_items WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, foodId);
            return statement.executeUpdate() > 0;
        }
    }

    /** True if this food appears in any past order (such food cannot be deleted). */
    public boolean isFoodOrdered(int foodId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM order_items WHERE food_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, foodId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public int countAll() throws SQLException {
        return queryCount("SELECT COUNT(*) FROM food_items");
    }

    public int countAvailable() throws SQLException {
        return queryCount("SELECT COUNT(*) FROM food_items WHERE available = TRUE");
    }

    private List<FoodItem> queryList(String sql) throws SQLException {
        List<FoodItem> foodList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                foodList.add(mapRow(rs));
            }
        }
        return foodList;
    }

    private int queryCount(String sql) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private FoodItem mapRow(ResultSet rs) throws SQLException {
        return new FoodItem(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("category"),
                rs.getDouble("price"),
                rs.getString("image"),
                rs.getBoolean("available"));
    }
}
