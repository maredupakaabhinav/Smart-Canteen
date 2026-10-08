package com.smartcanteen.service;

import com.smartcanteen.dao.UserDAO;
import com.smartcanteen.model.Student;
import com.smartcanteen.model.User;
import com.smartcanteen.util.PasswordUtil;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Pattern;

/**
 * Business logic for users: login and registration.
 */
public class UserService {

    private static final Pattern COLLEGE_ID_PATTERN = Pattern.compile("[A-Za-z0-9]{3,20}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final String INVALID_LOGIN_MESSAGE = "Invalid college ID or password.";

    private final UserDAO userDAO = new UserDAO();

    /**
     * Checks the college id, the password and the chosen login type ("student" or "admin").
     * Returns the logged-in user (without the password hash).
     */
    public User login(String collegeId, String password, String loginType) throws ServiceException {
        if (isBlank(collegeId) || isBlank(password)) {
            throw new ServiceException(INVALID_LOGIN_MESSAGE);
        }
        String expectedRole = "admin".equals(loginType) ? User.ROLE_ADMIN : User.ROLE_STUDENT;

        try {
            User user = userDAO.findByCollegeId(collegeId.trim().toUpperCase());
            boolean passwordCorrect = user != null && PasswordUtil.verifyPassword(password, user.getPassword());
            boolean roleCorrect = user != null && expectedRole.equals(user.getRole());

            if (!passwordCorrect || !roleCorrect) {
                // Same message for every failure, so nobody can find out which college ids exist
                throw new ServiceException(INVALID_LOGIN_MESSAGE);
            }
            user.setPassword(null);   // the hash is not needed after login
            return user;
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    /** Creates a new student account. */
    public User register(String name, String collegeId, String email, String password, String confirmPassword)
            throws ServiceException {
        if (isBlank(name) || name.trim().length() < 2 || name.trim().length() > 100) {
            throw new ServiceException("Please enter your name (2 to 100 characters).");
        }
        if (isBlank(collegeId) || !COLLEGE_ID_PATTERN.matcher(collegeId.trim()).matches()) {
            throw new ServiceException("College ID must be 3 to 20 letters or digits.");
        }
        if (isBlank(email) || email.trim().length() > 120 || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ServiceException("Please enter a valid email address.");
        }
        if (password == null || password.length() < 6 || password.length() > 72) {
            throw new ServiceException("Password must be 6 to 72 characters long.");
        }
        if (!password.equals(confirmPassword)) {
            throw new ServiceException("The two passwords do not match.");
        }

        String cleanCollegeId = collegeId.trim().toUpperCase();
        try {
            if (userDAO.findByCollegeId(cleanCollegeId) != null) {
                throw new ServiceException("This college ID is already registered.");
            }
            Student student = new Student(0, name.trim(), cleanCollegeId, PasswordUtil.hashPassword(password), email.trim());
            student.setId(userDAO.createUser(student));
            student.setPassword(null);
            return student;
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new ServiceException("This college ID is already registered.");
        } catch (SQLException e) {
            throw ServiceException.fromDatabaseError(e);
        }
    }

    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
