package fr.gtm.servlets;

import java.io.IOException;
import java.util.ArrayList;
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
 * Servlet implementation class GetOptionsDestinationsServlet
 */
@WebServlet("/GetOptionsDestinationsServlet")
public class GetOptionsDestinationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		Destination destination = new Destination();
		String ids = request.getParameter("id");
		String region = request.getParameter("region");
		String description = request.getParameter("description");
		long id = Long.parseLong(ids);
		destination.setId(id);
		destination.setRegion(region);
		destination.setDescription(description);
		String page = "";
		request.setAttribute("destination", destination);
		List<Destination> destinations =  service.getDestinations();
		request.setAttribute("destinations", destinations);
		page = "/show-options.jsp";
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
