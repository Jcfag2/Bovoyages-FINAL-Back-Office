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

@WebServlet("/AjoutDatesServlet")
public class AjoutDatesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("bovoyages");
	private final static Logger LOGGER = Logger.getLogger(AjoutDatesServlet.class.getCanonicalName());

    public AjoutDatesServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		boolean thereIsNoProbleme = true;
		try {
		thereIsNoProbleme = testDateTimeParseException(request);
		if(thereIsNoProbleme) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime dateDepartLocal = LocalDateTime.parse(request.getParameter("dateDepartLocal"), formatter);
		LocalDateTime dateRetourLocal = LocalDateTime.parse(request.getParameter("dateRetourLocal"), formatter);
		double prixHT=Double.valueOf(request.getParameter("prixHT"));
		int nbPlaces=Integer.valueOf(request.getParameter("nbPlaces"));
		int deleted=0;
		long id = Long.parseLong(request.getParameter("id"));
		DatesVoyages newDate=new DatesVoyages(dateDepartLocal, dateRetourLocal, prixHT, deleted, nbPlaces, id);
		Destination destination = service.getDestinationById(id);
		service.addDatesVoyages(id, newDate);
		response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDates?id="+id);
		}
		else {
			long id = Long.parseLong(request.getParameter("id"));
			response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDates?id="+id);
		}
		}catch(IllegalArgumentException exception) {
			long id = Long.parseLong(request.getParameter("id"));
			response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDates?id="+id);
		}
	}
	
	private boolean testDateTimeParseException(HttpServletRequest request) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		try {
			LocalDateTime dateDepartLocal = LocalDateTime.parse(request.getParameter("dateDepartLocal"), formatter);
			LocalDateTime dateRetourLocal = LocalDateTime.parse(request.getParameter("dateRetourLocal"), formatter);
			return true;
			
		}catch(DateTimeParseException exception) {
			return false;
		}
	}

}
