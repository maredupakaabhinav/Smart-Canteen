package com.smartcanteen.servlet;

import com.smartcanteen.model.User;
import com.smartcanteen.service.ServiceException;
import com.smartcanteen.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * GET  /login -> shows the login page
 * POST /login -> checks the college id + password and starts the session
 */
@WebServlet("/login")
public class LoginServlet extends BaseServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getLoggedInUser(request);
        if (user != null) {
            redirect(request, response, user.getDashboardPath());   // already logged in
            return;
        }
        forward(request, response, "login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String collegeId = request.getParameter("collegeId");
        String password = request.getParameter("password");
        String loginType = request.getParameter("loginType");

        try {
            User user = userService.login(collegeId, password, loginType);

            // A new session id after login protects against session fixation
            if (request.getSession(false) != null) {
                request.changeSessionId();
            }
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);

            redirect(request, response, user.getDashboardPath());   // POLYMORPHISM: Student and Admin differ
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("enteredCollegeId", collegeId);
            request.setAttribute("enteredLoginType", loginType);
            forward(request, response, "login.jsp");
        }
    }
}
