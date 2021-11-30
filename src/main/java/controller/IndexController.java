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

/**
 * This class will show the main page (as home.jsp is not directly accessible).
 * Be careful, becouse an index.jsp or index.html file will make this controller
 * unaccessible.
 * 
 * @author aperez
 *
 */
@WebServlet(name = "IndexController", urlPatterns = { "/index.html" })
public class IndexController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public IndexController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        System.out.println("Index Controller");
        ServletContext context = getServletContext();

        if(context.getAttribute("advertisements") == null){
            HttpSession session = request.getSession(true);
            session.setAttribute("locale", getLocale(request, session));
            AdvertisementFacade advertisementFacade = new AdvertisementFacade();
            ArrayList<Advertisment> allAdvertisements = advertisementFacade.loadAllNewsItems(/*getLocale(request, session)*/);
            context.setAttribute("advertisements", allAdvertisements);
        }

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/view/home.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

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
