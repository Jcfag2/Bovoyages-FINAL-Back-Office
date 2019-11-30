package fr.gtm.servlets;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

@WebServlet("/RenvoiToutesDestinations")
public class RenvoiToutesDestinations  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("bovoyages");
	private final static Logger LOGGER = Logger.getLogger(AjoutDatesServlet.class.getCanonicalName());

    public RenvoiToutesDestinations() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
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
		String page = "/show-destinations.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
		rd.forward(request,response);
	}

}