package be.vdab.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import be.vdab.entities.Klant;
import be.vdab.entities.Voorstelling;
import be.vdab.repositories.CultuurhuisRepository;

/**
 * Servlet implementation class ReservatieBevestiging
 */
@WebServlet("/bevestigingreservatie")
public class BevestigingReservatieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/bevestigingreservatie.jsp";
	private static final String URL_NIEUWE_KLANT = "/nieuweklant";
	private static final String VIEW_OVERZICHT = "/WEB-INF/JSP/overzicht.jsp";
	private static final String MANDJE = "mandje";;
	private transient CultuurhuisRepository cultuurhuisRepository = new CultuurhuisRepository();

	@Resource(name = CultuurhuisRepository.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		cultuurhuisRepository.setDataSource(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			if (session.getAttribute("gebruikersnaam") != null) {
				String gebruikersnaam = session.getAttribute("gebruikersnaam").toString();
				request.setAttribute("gebruiker", cultuurhuisRepository.zoekKlant(gebruikersnaam));
			}
		} else {
			request.setAttribute("fout", "U heeft geen reservatiemandje");
		}
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("opzoeken") != null) {
			zoekGebruikerOp(request, response);
			request.getRequestDispatcher(VIEW).forward(request, response);
		} else {
			if (request.getParameter("nieuw") != null) {
				response.sendRedirect(request.getContextPath() + URL_NIEUWE_KLANT);
			}
			if (request.getParameter("bevestigen") != null) {
				bevestigen(request, response);
			}
		}
	}

	private void bevestigen(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session != null) {
			@SuppressWarnings("unchecked")
			Map<Long, Long> mandje = (Map<Long, Long>) session.getAttribute(MANDJE);
			String gebruikersnaam = session.getAttribute("gebruikersnaam").toString();
			Map<Long, Boolean> reservatiesGeboektMap = cultuurhuisRepository.reservatiesBoeken(mandje, gebruikersnaam);
			Set<Voorstelling> voorstellingenSet = cultuurhuisRepository.getVoorstellingen(mandje.keySet());
			Map<Voorstelling, Long> mislukteReserveringen = new HashMap<>();
			Map<Voorstelling, Long> gelukteReserveringen = new HashMap<>();
			for (Voorstelling voorstelling : voorstellingenSet) {
				if (reservatiesGeboektMap.get(voorstelling.getId()) == true) {
					gelukteReserveringen.put(voorstelling, mandje.get(voorstelling.getId()));
				} else {
					mislukteReserveringen.put(voorstelling, mandje.get(voorstelling.getId()));
				}
			}
			request.setAttribute("geluktereserveringen", gelukteReserveringen);
			request.setAttribute("misluktereserveringen", mislukteReserveringen);
			session.invalidate();
			request.getRequestDispatcher(VIEW_OVERZICHT).forward(request, response);
			;

		} else {
			request.setAttribute("fout", "U heeft geen reservatiemandje");
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
	}

	private void zoekGebruikerOp(HttpServletRequest request, HttpServletResponse response) {
		Klant gebruiker = cultuurhuisRepository.zoekKlant(request.getParameter("gebruikersnaam"));
		if (gebruiker.getGebruikersnaam() != null) {
			if (request.getParameter("paswoord").equals(gebruiker.getPaswoord())) {
				HttpSession session = request.getSession();
				session.setAttribute("gebruikersnaam", gebruiker.getGebruikersnaam());
				request.setAttribute("gebruiker", gebruiker);
			} else {
				request.setAttribute("fouten", "Verkeerde gebruikersnaam of paswoord");
			}
		} else {
			request.setAttribute("fouten", "Verkeerde gebruikersnaam of paswoord");
		}
	}
}
