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

import fr.gtm.entities.DatesVoyages;
import fr.gtm.entities.Destination;
import fr.gtm.entities.Image;
import fr.gtm.services.DestinationServices;

/**
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/PromotionServlet")
public class PromotionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
 /**
  * @see HttpServlet#HttpServlet()
  */
 public PromotionServlet() {}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		long dateID = Long.parseLong(request.getParameter("dateID"));
		long destinationID = Long.parseLong(request.getParameter("destinationID"));	
		DatesVoyages dateAPromouvoir=  service.getDatesById(dateID);
		service.promouvoirDestination(destinationID, dateAPromouvoir);
		Destination destination=service.getDestinationById(destinationID);
		List<DatesVoyages> datesVoyages=service.getDatesVoyages(destinationID);
		request.setAttribute("datesVoyages", datesVoyages);
		request.setAttribute("destination", destination);
		List<Destination> destinations =  service.getDestinations();
		request.setAttribute("destinations", destinations);
		String page = "/show-dates.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
		rd.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
