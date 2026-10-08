package com.smartcanteen.servlet;

import com.smartcanteen.model.User;
import com.smartcanteen.service.ServiceException;
import com.smartcanteen.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Registration for new students / faculty. Admin accounts cannot be created here.
 */
@WebServlet("/register")
public class RegisterServlet extends BaseServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedInUser(request);
        if (user != null) {
            redirect(request, response, user.getDashboardPath());
            return;
        }
        forward(request, response, "register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            userService.register(
                    request.getParameter("name"),
                    request.getParameter("collegeId"),
                    request.getParameter("email"),
                    request.getParameter("password"),
                    request.getParameter("confirmPassword"));

            flashSuccess(request, "Account created. You can log in now.");
            redirect(request, response, "/login");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            // Give the typed values back to the form (never the passwords)
            request.setAttribute("enteredName", request.getParameter("name"));
            request.setAttribute("enteredCollegeId", request.getParameter("collegeId"));
            request.setAttribute("enteredEmail", request.getParameter("email"));
            forward(request, response, "register.jsp");
        }
    }
}
