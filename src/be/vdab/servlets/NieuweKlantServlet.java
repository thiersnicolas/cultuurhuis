package be.vdab.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import be.vdab.entities.Klant;
import be.vdab.repositories.CultuurhuisRepository;

/**
 * Servlet implementation class NieuweKlantServlet
 */
@WebServlet("/nieuweklant")
public class NieuweKlantServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/nieuweklant.jsp";
	private static final String REDIRECT_URL = "/bevestigingreservatie";
	private CultuurhuisRepository cultuurhuisRepository = new CultuurhuisRepository();

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
		request.getRequestDispatcher(VIEW).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<String> fouten = new ArrayList<>();
		Klant klant = new Klant();

		if (request.getParameter("voornaam") == null) {
			fouten.add("Voornaam niet ingevuld.");
		} else {
			klant.setVoornaam(String.valueOf(request.getParameter("voornaam")));
		}
		if (request.getParameter("familienaam") == null) {
			fouten.add("Familienaam niet ingevuld.");
		} else {
			klant.setFamilienaam(String.valueOf(request.getParameter("familienaam")));
		}
		if (request.getParameter("straat") == null) {
			fouten.add("Straat niet ingevuld.");
		} else {
			klant.setStraat(String.valueOf(request.getParameter("straat")));
		}
		if (request.getParameter("huisnr") == null) {
			fouten.add("Huisnr. niet ingevuld.");
		} else {
			klant.setHuisnr(String.valueOf(request.getParameter("huisnr")));
		}
		if (request.getParameter("postcode") == null) {
			fouten.add("Postcode niet ingevuld.");
		} else {
			klant.setPostcode(String.valueOf(request.getParameter("postcode")));
		}
		if (request.getParameter("gemeente") == null) {
			fouten.add("Gemeente niet ingevuld.");
		} else {
			klant.setGemeente(String.valueOf(request.getParameter("gemeente")));
		}
		if (request.getParameter("gebruikersnaam") == null) {
			fouten.add("Gebruikersnaam niet ingevuld.");
		} else {
			klant.setGebruikersnaam(String.valueOf(request.getParameter("gebruikersnaam")));
		}
		if (request.getParameter("paswoord") == null) {
			fouten.add("Paswoord niet ingevuld.");
		} else {
			if (request.getParameter("herhaalpaswoord") == null | (!(String.valueOf(request.getParameter("paswoord"))
					.equals(String.valueOf(request.getParameter("herhaalpaswoord")))))) {
				fouten.add("Paswoord en Herhaal paswoord zijn verschillend");
			} else {
				klant.setPaswoord(String.valueOf(request.getParameter("paswoord")));
			}
		}
		if (fouten.isEmpty() && cultuurhuisRepository.klantToevoegenDatabase(klant)) {
			HttpSession session = request.getSession();
			session.setAttribute("gebruikersnaam", klant.getGebruikersnaam());
			response.sendRedirect(request.getContextPath() + REDIRECT_URL);
		} else {
			if (fouten.isEmpty()) {
				fouten.add("Een klant met deze Gebruikersnaam komt al voor in de database");
			}
			request.setAttribute("fouten", fouten);
			request.getRequestDispatcher(VIEW).forward(request, response);
		}

	}
}
