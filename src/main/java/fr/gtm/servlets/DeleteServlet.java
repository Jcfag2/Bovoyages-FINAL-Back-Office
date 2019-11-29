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
 * Servlet implementation class DeleteServlet
 */
@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
 /**
  * @see HttpServlet#HttpServlet()
  */
 public DeleteServlet() {
     super();
     // TODO Auto-generated constructor stub
 }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		String ids = request.getParameter("id");
		String page = "";
		service.deleteDestination(Long.parseLong(ids));
		List<Destination> destinations =  service.getDestinations();
		List<Image> images =   new ArrayList<Image>();
		List<Image> imagesDestination =   new ArrayList<Image>();
		Image image =   new Image();
		for(Destination d : destinations) {
			images = service.getImages(d.getId());
			if(!images.isEmpty()) {
				image = images.get(0);
			}
			else {
				image = new Image();
				image.setImage("defaut.jpg");
			}
			imagesDestination.add(image);
		}
		request.setAttribute("destinations", destinations);
		request.setAttribute("imagesDestination", imagesDestination);
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
