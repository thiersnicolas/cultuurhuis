package be.vdab.servlets;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
 * Servlet implementation class ReservatieMandjeServlet
 */
@WebServlet("/reservatiemandje")
public class ReservatieMandjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String MANDJE = "mandje";
	private static final String VIEW = "/WEB-INF/JSP/reservatiemandje.jsp";
	private static final String URL_INDEX = "/index.htm";
	private final transient CultuurhuisRepository cultuurhuisRepository = new CultuurhuisRepository();
	
	@Resource(name = CultuurhuisRepository.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		cultuurhuisRepository.setDataSource(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session =  request.getSession(false);
		if (session != null) {
			@SuppressWarnings("unchecked")
			Map<Long, Long> mandje = (Map<Long, Long>) session.getAttribute(MANDJE);
			if (mandje != null) {
				List<Long> plaatsenLijst = new ArrayList<>(mandje.values());
				Set<Long> ids = mandje.keySet();
				Map<Voorstelling, Long> reservatieMandjeMap = new TreeMap<>();
				Set<Voorstelling> voorstellingenSet = cultuurhuisRepository.getVoorstellingen(ids);
				int i=0;
				BigDecimal totalePrijs = BigDecimal.ZERO;
				for (Voorstelling voorstelling:voorstellingenSet) {
					reservatieMandjeMap.put(voorstelling, plaatsenLijst.get(i));
					totalePrijs= totalePrijs.add(voorstelling.getPrijs().multiply(BigDecimal.valueOf(plaatsenLijst.get(i))));
					i++;
				}
				
				request.setAttribute("reservatiemandje", reservatieMandjeMap);
				request.setAttribute("totaleprijs", totalePrijs);
				request.getRequestDispatcher(VIEW).forward(request, response);
			}
		} else {
			request.setAttribute("fout", "U heeft geen reservatiemandje");
			request.getRequestDispatcher(VIEW).forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] voorstellingIdsTeVerwijderenArray = request.getParameterValues("idsverwijderen");
		HttpSession session = request.getSession(false);
		if (voorstellingIdsTeVerwijderenArray != null && session!=null) {
			Set<Long> voorstellingIdsTeVerwijderenSet = new LinkedHashSet<>();
			for (int i=0; i<voorstellingIdsTeVerwijderenArray.length; i++) {
				if (StringUtiles.isLong(voorstellingIdsTeVerwijderenArray[i])) {
					voorstellingIdsTeVerwijderenSet.add(Long.valueOf(voorstellingIdsTeVerwijderenArray[i]));
				}
			}
			@SuppressWarnings("unchecked")
			Map<Long, Long> mandje = (Map<Long, Long>) session.getAttribute(MANDJE);
			voorstellingIdsTeVerwijderenSet.stream().forEach(id -> mandje.remove(id));
			if (mandje.isEmpty()) {
				session.invalidate();
				response.sendRedirect(request.getContextPath() + URL_INDEX);
			} else {
				response.sendRedirect(request.getRequestURI());
			}
			
		}	else {
			response.sendRedirect(request.getRequestURI());
		}
		
	}

}
