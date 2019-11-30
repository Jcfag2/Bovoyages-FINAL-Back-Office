package fr.gtm.servlets;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.gtm.entities.DatesVoyages;
import fr.gtm.entities.Destination;
import fr.gtm.services.DestinationServices;

@WebServlet("/UpdateDatesServlet")
public class UpdateDatesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UpdateDatesServlet() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		boolean thereIsNoProblemes = true;
		thereIsNoProblemes = testDateTimeParseException(request);
		if(thereIsNoProblemes) {
		long dateID = Long.parseLong(request.getParameter("dateID"));
		long destinationID = Long.parseLong(request.getParameter("destinationID"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime dateDepartLocal = LocalDateTime.parse(request.getParameter("dateDepartLocal"), formatter);
		LocalDateTime dateRetourLocal = LocalDateTime.parse(request.getParameter("dateRetourLocal"), formatter);
		String prixHTString = request.getParameter("prixHT");
		double prixHT = Double.valueOf(prixHTString);
		int nbPlaces=Integer.valueOf(request.getParameter("nbPlaces"));
		int deleted=0;
		int promotion=Integer.valueOf(request.getParameter("promotion"));
		DatesVoyages newDate=new DatesVoyages(dateDepartLocal, dateRetourLocal, prixHT, deleted, nbPlaces,dateID,promotion);
		DatesVoyages dateToRemove=  service.getDatesById(dateID);
		service.deleteDatesVoyages(destinationID, dateToRemove);
		service.addDatesVoyages(destinationID, newDate);
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
		else {
			long destinationID = Long.parseLong(request.getParameter("destinationID"));
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
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
