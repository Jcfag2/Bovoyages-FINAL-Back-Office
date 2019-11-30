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
import fr.gtm.entities.Image;
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
 public ModifierDestinationServlets() {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		Destination destination = new Destination();
		String id = request.getParameter("id");
		destination.setId(Long.valueOf(id));
		String region = request.getParameter("region");
		if(region.isBlank() || region.isEmpty()) {
			response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDestinationParticuliere?id="+id);
		}
		else {
		String description = request.getParameter("description");
		destination.setRegion(region);
		destination.setDescription(description);
		
		String page = "";
//		service.modifyDestination(destination);
		service.modifyDestinationLazy(destination);
		response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDestinationParticuliere?id="+id);
		}
	}
}
