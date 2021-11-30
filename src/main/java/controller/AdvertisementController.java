package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

import domain.advertisment.dao.AdvertisementFacade;
import domain.advertisment.model.Advertisment;
import helper.ControllerHelper;

@WebServlet(name = "AdvertisementController", urlPatterns = { "/advertisement/*" })
public class AdvertisementController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdvertisementController() {
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
                deleteAdvertisement(request, response, helper.getId());
                break;
            case "create":
                // Show news item Form.
                showAdvertisementForm(request, response, -1);
            break;
            case "edit":
                // Show news item Form.
                showAdvertisementForm(request, response, helper.getId());
                break;
            case "view":
            case "list":
            default:
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
                createAdvertisement(request, response);
                break;
            case "edit":
                // Edit news item in the DB and redirect to its view.
                editAdvertisement(request, response, helper.getId());
                break;
            default:
        }

    }

    /**
     * Dispatch NewsItem form.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param id the identifier of the news item that will be edited (if any).
     * @throws ServletException
     * @throws IOException
     */
    private void showAdvertisementForm(HttpServletRequest request, HttpServletResponse response, int id)
            throws ServletException, IOException {
        System.out.println("Show News Item Form: " + id);
        HttpSession session = request.getSession(true);

        if(id > 0){
            AdvertisementFacade advertisementFacade = new AdvertisementFacade();
            Advertisment advertisment = advertisementFacade.loadAdvertisement(id);
            request.setAttribute("advertisement", advertisment);
        }

        request.setAttribute("langTag", getLocale(request, session).toLanguageTag());
        request.setAttribute("lang", getLocale(request, session));

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/advertisement/advertisement_form.jsp");
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
    private void deleteAdvertisement(HttpServletRequest request, HttpServletResponse response, int id) throws IOException {
        HttpSession session = request.getSession(true);

        AdvertisementFacade advertisementFacade = new AdvertisementFacade();

        if (id != -1 && advertisementFacade.deleteAdvertisement(id)) {
            getListOfAdvertisement(request);
            session.setAttribute("message", "message.deleteAdvertisement");
        } else {
            session.setAttribute("error", "error.deleteAdvertisement");
        }
        response.sendRedirect(request.getContextPath() + "/");
    }


    /**
     * Update NewsItem and redirect to its view.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param id the identifier of the news item that will be edited.
     * @throws ServletException
     * @throws IOException
     */
    private void editAdvertisement(HttpServletRequest request, HttpServletResponse response, int id) throws IOException {
        HttpSession session = request.getSession(true);

        AdvertisementFacade advertisementFacade = new AdvertisementFacade();
        Advertisment advertisment = advertisementFacade.loadAdvertisement(id);
        System.out.println("Edit advertisement: " + id);
        if (advertisment != null) {
            advertisment.setTitle(request.getParameter("title"));
            advertisment.setSrc(request.getParameter("src"));
            advertisment.setUrl(request.getParameter("url"));
            advertisment.setLocale(getLocale(request, session));

            advertisementFacade.saveAdvertisement(advertisment);
            if (advertisment.getAdvertismentId() > 0) {
                session.setAttribute("message", "message.editAdvertisement");
                // session.setAttribute("newsItem", newsItem);
                getListOfAdvertisement(request);
                response.sendRedirect(request.getContextPath() + "/");
            } else {
                session.setAttribute("error", "error.editAdvertisement");
                response.sendRedirect(request.getContextPath() + "/advertisement/" + id + "/edit");
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
    private void createAdvertisement(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        Advertisment advertisment = new Advertisment();
        advertisment.setTitle(request.getParameter("title"));
        advertisment.setSrc(request.getParameter("src"));
        advertisment.setUrl(request.getParameter("url"));
        advertisment.setLocale(getLocale(request, session));

        AdvertisementFacade advertisementFacade = new AdvertisementFacade();
        advertisementFacade.saveAdvertisement(advertisment);

        String redirectUrl = "";
        if (advertisment.getAdvertismentId() > 0) {
            session.setAttribute("message", "message.createAdvertisement");
        } else {
            session.setAttribute("error", "error.createAdvertisement");
        }
        redirectUrl = "/";

        getListOfAdvertisement(request);
        response.sendRedirect(request.getContextPath() + redirectUrl);
    }

    private void getListOfAdvertisement(HttpServletRequest request){
        ServletContext context = getServletContext();
        HttpSession session = request.getSession(true);
        session.setAttribute("locale", getLocale(request, session));
        AdvertisementFacade advertisementFacade = new AdvertisementFacade();
        ArrayList<Advertisment> allAdvertisements = advertisementFacade.loadAllNewsItems(/*getLocale(request, session)*/);
        context.setAttribute("advertisements", allAdvertisements);
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