package be.vdab.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import be.vdab.entities.Voorstelling;
import be.vdab.repositories.CultuurhuisRepository;
import util.StringUtiles;

/**
 * Servlet implementation class ReserverenServlet
 */
@WebServlet("/reserveren")
public class ReserverenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/reserveren.jsp";
	private static final String REDIRECT_URL = "/reservatiemandje";
	private CultuurhuisRepository cultuurhuisRepository = new CultuurhuisRepository();
	private static final String MANDJE = "mandje";

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

		if (StringUtiles.isLong(request.getParameter("id"))) {
			Voorstelling voorstelling;
			Long voorstellingid = Long.parseLong((request.getParameter("id")));
			voorstelling = cultuurhuisRepository.getVoorstelling(voorstellingid);
			if (voorstelling.getTitel() != null) {
				request.setAttribute("voorstelling", voorstelling);
				if (session != null) {
					@SuppressWarnings({ "unchecked" })
					Map<Long, Long> mandje = (Map<Long, Long>) session.getAttribute(MANDJE);
					if (mandje.containsKey(voorstellingid)) {
						request.setAttribute("gereserveerdeplaatsen", mandje.get(voorstellingid));
					}
				}
				request.getRequestDispatcher(VIEW).forward(request, response);
			} else {
				request.setAttribute("foutid", "voorstellingsid is niet correct");
				request.getRequestDispatcher(VIEW).forward(request, response);
			}
		} else {
			request.setAttribute("foutid", "voorstellingsid is niet correct");
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long voorstellingid = Long.parseLong((request.getParameter("id")));
		Voorstelling voorstelling = cultuurhuisRepository.getVoorstelling(voorstellingid);
		if (StringUtiles.isLong(request.getParameter("plaatsen"))) {
			reserveren(request, response, voorstelling);

		} else {
			request.setAttribute("foutplaatsen", "isLong = false");
			request.setAttribute("voorstelling", voorstelling);
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
	}

	private void reserveren(HttpServletRequest request, HttpServletResponse response, Voorstelling voorstelling)
			throws ServletException, IOException {
		Long voorstellingid = Long.parseLong((request.getParameter("id")));
		if (voorstelling.getAantalVrijePlaatsen() >= Long.parseLong(request.getParameter("plaatsen"))) {
			HttpSession session = request.getSession();
			Long plaatsen = Long.parseLong(request.getParameter("plaatsen"));
			if (session == null || session.getAttribute(MANDJE)==null) {
				Map<Long, Long> nieuwMandje = new TreeMap<>();
				nieuwMandje.put(voorstellingid, plaatsen);
				session.setAttribute(MANDJE, nieuwMandje);
			} else {
				@SuppressWarnings("unchecked")
				Map<Long, Long> oudMandje = (Map<Long, Long>) session.getAttribute(MANDJE);
				oudMandje.put(voorstellingid, plaatsen);
				session.setAttribute(MANDJE, oudMandje);
			}
			response.sendRedirect(request.getContextPath() + REDIRECT_URL);

		} else {
			request.setAttribute("foutplaatsen", "plaatsen > aantalvrijeplaatsen");
			request.setAttribute("voorstelling", voorstelling);
			request.getRequestDispatcher(VIEW).forward(request, response);
		}

	}

}
