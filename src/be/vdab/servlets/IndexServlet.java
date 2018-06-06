package be.vdab.servlets;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import be.vdab.entities.Genre;
import be.vdab.entities.Voorstelling;
import be.vdab.repositories.CultuurhuisRepository;
import util.StringUtiles;

/**
 * Servlet implementation class IndexServlet
 */
@WebServlet("/index.htm")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String VIEW = "/WEB-INF/JSP/index.jsp";
	private final CultuurhuisRepository cultuurhuisRepository = new CultuurhuisRepository();
	
	@Resource(name = CultuurhuisRepository.JNDI_NAME)
	void setDataSource(DataSource dataSource) {
		cultuurhuisRepository.setDataSource(dataSource);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	List<Genre> lijstGenres = cultuurhuisRepository.getGenres();
	request.setAttribute("genres", lijstGenres);
	
	String id = request.getParameter("id");
	System.out.println(id);
	if (id!=null) {
		if (StringUtiles.isLong(id)) {
			List <Voorstelling> lijstVoorstellingenGenre = cultuurhuisRepository.getVoorstellingenGenre(Long.parseLong(id));
			request.setAttribute("lijstvoorstellingengenre", lijstVoorstellingenGenre);
			for (Genre genre:lijstGenres) {
				if (genre.getId()==Long.parseLong(id)) {
					request.setAttribute("genrenaam", genre.getNaam());
				}
			}
		} else {
			request.setAttribute("fout", "id is niet correct");
		}
	}
	
	request.getRequestDispatcher(VIEW).forward(request, response);
	}
}
