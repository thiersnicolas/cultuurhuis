package be.vdab.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import be.vdab.entities.Voorstelling;
import be.vdab.repositories.CultuurhuisRepository;

/**
 * Servlet implementation class ReservatieMandjeServlet
 */
@WebServlet("/reservatiemandje")
public class ReservatieMandjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String MANDJE = "mandje";
	private static final String VIEW = "/WEB-INF/JSP/reservatiemandje.jsp";
	private final transient CultuurhuisRepository cultuurhuisRepository = new CultuurhuisRepository();
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session =  request.getSession(false);
		if (session != null) {
			@SuppressWarnings("unchecked")
			Map<Long, Long> mandje = (Map<Long, Long>) session.getAttribute(MANDJE);
			if (mandje != null) {
				List<Long> plaatsenLijst = (List<Long>) mandje.values();
				Set<Long> ids = mandje.keySet();
				Map<Voorstelling, Long> reservatieMandjeMap = new TreeMap<>();
				Set<Voorstelling> voorstellingenSet = cultuurhuisRepository.getVoorstellingen(ids);
				int i=0;
				BigDecimal totalePrijs = BigDecimal.ZERO;
				for (Voorstelling voorstelling:voorstellingenSet) {
					reservatieMandjeMap.put(voorstelling, plaatsenLijst.get(i));
					totalePrijs= voorstelling.getPrijs().multiply(BigDecimal.valueOf(plaatsenLijst.get(i)));
					i++;
				}
				
				request.setAttribute("reservatiemandje", reservatieMandjeMap);
				request.setAttribute("totalePrijs", totalePrijs);
				request.getRequestDispatcher(VIEW).forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
