package com.smartcanteen.dao;

import com.smartcanteen.model.Admin;
import com.smartcanteen.model.Student;
import com.smartcanteen.model.User;
import com.smartcanteen.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DAO = Data Access Object. Only this class talks to the users table.
 * Every query uses PreparedStatement (the ? marks), so user input can never change the SQL.
 */
public class UserDAO {

    /** Finds a user by college id (or admin id). Returns null if there is no such user. */
    public User findByCollegeId(String collegeId) throws SQLException {
        String sql = "SELECT id, name, college_id, email, password, role FROM users WHERE college_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, collegeId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT id, name, college_id, email, password, role FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /** Saves a new user and returns the generated id. The password must already be hashed. */
    public int createUser(User user) throws SQLException {
        String sql = "INSERT INTO users (name, college_id, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getCollegeId());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getRole());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : 0;
            }
        }
    }

    /** Turns one database row into a Student or an Admin object (POLYMORPHISM: both are Users). */
    private User mapRow(ResultSet rs) throws SQLException {
        User user;
        if (User.ROLE_ADMIN.equals(rs.getString("role"))) {
            user = new Admin();
        } else {
            user = new Student();
        }
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setCollegeId(rs.getString("college_id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    }
}
