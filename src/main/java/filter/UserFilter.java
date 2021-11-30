package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import domain.user.model.User;
import helper.ControllerHelper;

/**
 * Servlet Filter implementation class UserFilter
 */
@WebFilter("/user/*")
public class UserFilter implements Filter {

    public UserFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("User Filter");
        HttpServletRequest request = (HttpServletRequest) req;
        request.setCharacterEncoding("UTF-8");
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);

        ControllerHelper helper = new ControllerHelper(request);
        String action = helper.getAction();

        int code;

        switch (action) {
            case "create":
                System.out.println("User creation allowed for anybody.");
                code = HttpServletResponse.SC_OK;
                break;
            case "edit":
            case "delete":
                code = filterModification(helper, session);
                break;
            case "view":
            case "list":
                code = filterShow(session);
                break;
            default:
                System.out.println("Unknown User action: " + action);
                code = HttpServletResponse.SC_BAD_REQUEST;
                session.setAttribute("error", "error.400.unknown_action");
        }

        if (code == HttpServletResponse.SC_OK) {
            chain.doFilter(req, res);
        } else {
            session.setAttribute("errorCode", code);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/view/error.jsp");
            response.setStatus(code);
            dispatcher.forward(request, response);
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

    private int filterModification(ControllerHelper helper, HttpSession session) {
        int code;
        User user = (User) session.getAttribute("user");

        if (user != null && user.getUserId() == helper.getId()) {
            System.out.println("User modifies it's own user.");
            code = HttpServletResponse.SC_OK;
        } else {
            System.out.println("User trying to modify other user.");
            code = HttpServletResponse.SC_FORBIDDEN;
            session.setAttribute("error", "error.403.not_own_user");
        }
        return code;
    }

    private int filterShow(HttpSession session) {
        int code;
        User user = (User) session.getAttribute("user");

        if (user != null) {
            System.out.println("User in session. They can view/list user(s).");
            code = HttpServletResponse.SC_OK;
        } else {
            System.out.println("View/List actions need the user to be in session.");
            code = HttpServletResponse.SC_FORBIDDEN;
            session.setAttribute("error", "error.403.not_session_user");
        }
        return code;
    }
}
