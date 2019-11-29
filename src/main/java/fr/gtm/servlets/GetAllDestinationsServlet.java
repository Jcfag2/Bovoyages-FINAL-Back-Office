package fr.gtm.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import fr.gtm.servlets.Constantes;
import fr.gtm.entities.Destination;
import fr.gtm.entities.Image;
import fr.gtm.services.DestinationServices;


/**
 * Servlet implementation class GetAllDestinationsServlet
 */
@WebServlet("/GetAllDestinationsServlet")
@MultipartConfig()
public class GetAllDestinationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("bovoyages");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		String page = "";
		List<Destination> destinations =  service.getDestinations();
		LOG.info(">>>>>>>>>>                       ");
		LOG.info(">>>>>>>>>>>>>>>>>>>>>> "+destinations.get(0).getId()+" "+destinations.get(0).getRegion());
		List<Image> images =   new ArrayList<Image>();
		List<Image> imagesDestination =   new ArrayList<Image>();
		Image image =   new Image();
		for(Destination destination : destinations) {
			images = service.getImages(destination.getId());
			LOG.info(">>>>>>>>>>>>>>>>>>>>>> "+destination.getId()+" "+destination.getRegion()+" "+images);
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

		doGet(request, response);
		
	}

}
