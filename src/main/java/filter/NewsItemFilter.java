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

import domain.news.dao.NewsItemFacade;
import domain.news.model.NewsItem;
import domain.user.model.User;
import helper.ControllerHelper;

@WebFilter("/news/*")
public class NewsItemFilter implements Filter {
    public NewsItemFilter() {}

    public void destroy() {}

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("NewsItem Filter");
        HttpServletRequest request = (HttpServletRequest) req;
        request.setCharacterEncoding("UTF-8");
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);

        ControllerHelper helper = new ControllerHelper(request);
        String action = helper.getAction();
        int code;

        switch (action) {
            case "create":
                code = filterCreation(session);
                break;
            case "edit":
            case "delete":
                code = filterModification(helper, session, request);
                break;
            case "view":
            case "list":
                System.out.println("Even if no user in session, view and list permited: " + action);
                code = HttpServletResponse.SC_OK;
                break;
            default:
                System.out.println("Unknown News Item action: " + action);
                session.setAttribute("error", "error.400.unknown_action");
                code = HttpServletResponse.SC_BAD_REQUEST;
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
        System.out.println("Init NewsItem filter.");
    }

    private int filterCreation(HttpSession session) {
        int code;
        User user = (User) session.getAttribute("user");
        if (user != null) {
            System.out.println("News Item creation allowed for users: " + user.getUserId());
            code = HttpServletResponse.SC_OK;
        } else {
            System.out.println("News Item creation needs a user in session.");
            code = HttpServletResponse.SC_FORBIDDEN;
            session.setAttribute("error", "error.403.not_session_user");
        }
        return code;
    }

    private int filterModification(ControllerHelper helper, HttpSession session, HttpServletRequest request) {
        int code;
        User user = (User) session.getAttribute("user");
        int newsItemId = helper.getId();
        NewsItemFacade nif = new NewsItemFacade();
        NewsItem newsItem = nif.loadNewsItem(newsItemId);

        if (newsItem.getNewsItemId() < 0) {
            System.out.println("News item does not exist: "+ newsItemId);
            code = HttpServletResponse.SC_NOT_FOUND;
            session.setAttribute("error", "error.404.news_item_not_found");
        } else if (user != null && user.getUserId() == newsItem.getAuthor().getUserId()) {
            System.out.println("Session user is the author.");
            code = HttpServletResponse.SC_OK;
            request.setAttribute("newsItem", newsItem);
        } else {
            System.out.println("Session user is not the author.");
            code = HttpServletResponse.SC_FORBIDDEN;
            session.setAttribute("error", "error.403.user_not_author");
        }
        return code;
    }
}
