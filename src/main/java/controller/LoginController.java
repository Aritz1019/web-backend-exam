package controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.user.dao.UserFacade;
import domain.user.model.User;
import helper.ControllerHelper;

@WebServlet(name = "LoginController", urlPatterns = { "/login/*" })
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        ControllerHelper helper = new ControllerHelper(request);
        String action = helper.getAction();
        HttpSession session = request.getSession(true);

        switch (action) {
            case "login":
                login(session, request, response);
                break;
            case "logout":
            default:
                logout(session, request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void login(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String username = Optional.ofNullable(request.getParameter("username")).orElse("");
        String password = Optional.ofNullable(request.getParameter("password")).orElse("");
        System.out.println("Username: " + username);
        System.out.println("Password: " + password
                + " (This should never be done in real projects! Printing passwords in logs is a bad practice.)");
        User user = null;

        // Check if the user exists in the database/properties file
        UserFacade uf = new UserFacade();
        user = uf.loadUser(username, password);

        // Save login result in session
        if (user != null) {
            session.setAttribute("user", user);
            session.setAttribute("message", "message.login");
        } else {
            session.removeAttribute("user");
            session.setAttribute("username", username);
            session.setAttribute("error", "error.login");
        }
        response.sendRedirect("/");
    }

    private void logout(HttpSession session, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        session.removeAttribute("user");
        session.setAttribute("message", "message.logout");

        response.sendRedirect("/");
    }

}