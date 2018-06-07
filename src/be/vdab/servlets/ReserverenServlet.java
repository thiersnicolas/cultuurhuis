package be.vdab.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	private static final String VIEW_SLECHTE_ID = "/WEB-INF/JSP/index.jsp";
	//private static final String VIEW_GERESERVEERD = "/WEB-INF/JSP/reservatiemandje.jsp";
	private CultuurhuisRepository cultuurhuisRepository = new CultuurhuisRepository();
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Voorstelling voorstelling = new Voorstelling();
		if (StringUtiles.isLong(request.getParameter("id"))) {
			String id = request.getParameter("id");
			Long voorstellingid = Long.parseLong(id);
			voorstelling = cultuurhuisRepository.getVoorstelling(voorstellingid);
			if (voorstelling != null) {
				HttpSession session = request.getSession();
				session.setAttribute("voorstellingid", voorstelling.getId());
				request.setAttribute("voorstelling", voorstelling);
				request.getRequestDispatcher(VIEW).forward(request, response);
			}
		}
		request.setAttribute("fout", "voorstellingsid is niet correct");
		request.getRequestDispatcher(VIEW_SLECHTE_ID).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("plaatsen") != null) {
			reserveren(request, response);
		}
	}
	
	private void reserveren(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (StringUtiles.isLong(request.getParameter("plaatsen"))){
			HttpSession session = request.getSession();
			Long voorstellingId = (Long) session.getAttribute("voorstellingid");
			Voorstelling voorstelling = cultuurhuisRepository.getVoorstelling(voorstellingId);
			Long plaatsen = Long.parseLong(request.getParameter("plaatsen"));
			if (voorstelling != null && plaatsen<=voorstelling.getAantalVrijePlaatsen()) {
				
			}
		} else {
			
		}
		
	}

}
