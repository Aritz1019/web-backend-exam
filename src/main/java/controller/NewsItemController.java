package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import domain.news.dao.NewsItemFacade;
import domain.news.model.NewsItem;
import domain.user.model.User;
import helper.ControllerHelper;

@WebServlet(name = "NewsItemController", urlPatterns = { "/news/*" })
public class NewsItemController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public NewsItemController() {
        super();
    }

    /**
     * Executed if GET request 
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        ControllerHelper helper = new ControllerHelper(request);
        String action = helper.getAction();

        switch (action) {
            case "delete":
                // Remove news item from DB.
                deleteNewsItem(request, response, helper.getId());
                break;
            case "create":
                // Show news item Form.
                showNewsItemForm(request, response, -1);
            break;
            case "edit":
                // Show news item Form.
                showNewsItemForm(request, response, helper.getId());
                break;
            case "view":
                // Show single news item view.
                showNewsItem(request, response, helper.getId());
                break;
            case "list":
            default:
                // Show news item list view.
                listNews(request, response);
        }
    }

    /**
     * Executed if POST request.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        ControllerHelper helper = new ControllerHelper(request);
        String action = helper.getAction();

        switch (action) {
            case "create":
                // Create news item in the DB and redirect to its view.
                createNewsItem(request, response);
                break;
            case "edit":
                // Edit news item in the DB and redirect to its view.
                editNewsItem(request, response, helper.getId());
                break;
            default:
                listNews(request, response);
        }

    }

    /**
     * Default value if something is not correct or user wants to list NewsItems.
     */
    private void listNews(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        NewsItemFacade nif = new NewsItemFacade();
        Locale locale = this.getLocale(request, session);

        ArrayList<NewsItem> news = nif.loadAllNewsItems(locale);
        request.setAttribute("news", news);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/news_item/news_item_list.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Load NewsItem and dispatch single NewsItem view.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param id the identifier of the news item that will be shown.
     * @throws ServletException
     * @throws IOException
     */
    private void showNewsItem(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        NewsItemFacade nif = new NewsItemFacade();
        NewsItem newsItem = nif.loadNewsItem(id);
        request.setAttribute("newsItem", newsItem);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/news_item/news_item.jsp");
        dispatcher.forward(request, response);

    }


    /**
     * Dispatch NewsItem form.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param id the identifier of the news item that will be edited (if any).
     * @throws ServletException
     * @throws IOException
     */
    private void showNewsItemForm(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        System.out.println("Show News Item Form: " + id);

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/news_item/news_item_form.jsp");
        dispatcher.forward(request, response);
    }


    /**
     * Delete NewsItem from Database and dispatch NewsItem list.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param id the identifier of the news item that will be deleted.
     * @throws ServletException
     * @throws IOException
     */
    private void deleteNewsItem(HttpServletRequest request, HttpServletResponse response, int id) throws IOException {
        HttpSession session = request.getSession(true);

        NewsItemFacade nif = new NewsItemFacade();
        if (id != -1 && nif.deleteNewsItem(id)) {
            session.setAttribute("message", "message.deleteNewsItem");
        } else {
            session.setAttribute("error", "error.deleteNewsItem");
        }
        response.sendRedirect(request.getContextPath() + "/news/list");
    }


    /**
     * Update NewsItem and redirect to its view.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param id the identifier of the news item that will be edited.
     * @throws ServletException
     * @throws IOException
     */
    private void editNewsItem(HttpServletRequest request, HttpServletResponse response, int id) throws IOException {
        HttpSession session = request.getSession(true);

        NewsItemFacade nif = new NewsItemFacade();
        NewsItem newsItem = (NewsItem) request.getAttribute("newsItem");
        System.out.println("Edit News Item: " + id);
        if (newsItem != null) {
            newsItem.setTitle(request.getParameter("title"));
            newsItem.setBody(request.getParameter("body"));
            nif.saveNewsItem(newsItem);
            if (newsItem.getNewsItemId() > 0) {
                session.setAttribute("message", "message.editNewsItem");
                session.setAttribute("newsItem", newsItem);
                response.sendRedirect(request.getContextPath() + "/news/" + id + "/view");
            } else {
                session.setAttribute("error", "error.editNewsItem");
                response.sendRedirect(request.getContextPath() + "/news/" + id + "/edit");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

    }


    /**
     * Create NewsItem and redirect to its view.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException
     * @throws IOException
     */
    private void createNewsItem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        NewsItem newsItem = new NewsItem();
        newsItem.setAuthor(user);
        newsItem.setTitle(request.getParameter("title"));
        newsItem.setBody(request.getParameter("body"));
        newsItem.setLang(getLocale(request, session));

        NewsItemFacade nif = new NewsItemFacade();
        nif.saveNewsItem(newsItem);

        String redirectUrl = "";
        if (newsItem.getNewsItemId() > 0) {
            // If news item has been created, redirect to its view.
            request.setAttribute("newsItem", newsItem);
            session.setAttribute("message", "message.createNewsItem");
            redirectUrl = "/news/" + newsItem.getNewsItemId() + "/view";
        } else {
            // If news item could not be created, redirect to list news items.
            session.setAttribute("error", "error.createNewsItem");
            redirectUrl = "/news/list";
        }
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }


    /**
     * Get locale from FMT or Browser (request).
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param id the identifier of the news item that will be shown.
     * @throws ServletException
     * @throws IOException
     */
    private Locale getLocale(HttpServletRequest request, HttpSession session) {
        Locale returnLocale = Locale.forLanguageTag("en-UK"); // Default locale.
        Locale fmtLocale = (Locale) Config.get(session, Config.FMT_LOCALE); // Locale from FMT library
        Locale browserLocale = request.getLocale(); // Browser locale

        if (fmtLocale != null)
            returnLocale = fmtLocale;
        else if (browserLocale != null)
            returnLocale = browserLocale;

        return returnLocale;
    }
}