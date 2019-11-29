package fr.gtm.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.gtm.entities.Destination;
import fr.gtm.services.DestinationServices;

/**
 * Servlet implementation class ModifierDestinationServlets
 */
@WebServlet("/ModifierDestinationServlets")
public class ModifierDestinationServlets extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
 /**
  * @see HttpServlet#HttpServlet()
  */
 public ModifierDestinationServlets() {
     super();
     // TODO Auto-generated constructor stub
 }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		Destination destination = new Destination();
		String id = request.getParameter("id");
		destination.setId(Long.valueOf(id));
		String region = request.getParameter("region");
		String description = request.getParameter("description");
		destination.setRegion(region);
		destination.setDescription(description);
		
		String page = "";
		service.modifyDestination(destination);
		List<Destination> destinations =  service.getDestinations();
		request.setAttribute("destinations", destinations);
		page = "/show-destinations.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
		rd.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
