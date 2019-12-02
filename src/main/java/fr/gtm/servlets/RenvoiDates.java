package fr.gtm.servlets;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import fr.gtm.services.DestinationServices;

@WebServlet("/RenvoiDates")
public class RenvoiDates extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("bovoyages");
	private final static Logger LOGGER = Logger.getLogger(AjoutDatesServlet.class.getCanonicalName());

    public RenvoiDates() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		long id = Long.parseLong(request.getParameter("id"));
		Destination destination = service.getDestinationById(id);
		List<DatesVoyages> datesVoyages =  service.getDatesVoyages(id);
		request.setAttribute("datesVoyages", datesVoyages);
		request.setAttribute("destination", destination);
		List<Destination> destinations =  service.getDestinations();
		request.setAttribute("destinations", destinations);
		request.setAttribute("identifiant", request.getSession().getAttribute("identifiant"));//              ajout de l'identifiant de l'utilisateur connecte pour que ce soit visible partout et tout le temps
		String page = "/show-dates.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
		rd.forward(request,response);
	}

}
