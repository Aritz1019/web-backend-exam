package controller;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.*;

@WebServlet(name = "LocaleController", urlPatterns = { "/lang" })
public class LocaleController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LocaleController() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String language = Optional.ofNullable(request.getParameter("language")).orElse("en");
		String country = Optional.ofNullable(request.getParameter("country")).orElse("UK");
		Locale newLocale;
		if (!language.isBlank() && !country.isBlank()) {
			newLocale = new Locale(language, country);
			Config.set(session, javax.servlet.jsp.jstl.core.Config.FMT_LOCALE, newLocale);
			session.setAttribute("locale", newLocale);
		}

		response.sendRedirect(request.getHeader("referer"));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
