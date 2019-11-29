package fr.gtm.servlets;

import java.io.IOException;
import java.sql.Date;
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

@WebServlet("/AjoutDatesServlet")
public class AjoutDatesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public AjoutDatesServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		try {
		Date dateDepart=Date.valueOf(request.getParameter("dateDepart"));
		Date dateRetour=Date.valueOf(request.getParameter("dateRetour"));
		int prixHT=Integer.valueOf(request.getParameter("prixHT"));
		int nbPlaces=Integer.valueOf(request.getParameter("nbPlaces"));
		int deleted=0;
		long id = Long.parseLong(request.getParameter("id"));
		DatesVoyages newDate=new DatesVoyages(dateDepart, dateRetour, prixHT, deleted, nbPlaces, id);
		Destination destination = service.getDestinationById(id);
		service.addDatesVoyages(id, newDate);
		//les 3 instructions ci-dessous font bien persister en base
//		datesVoyages.add(newDate);
//		destination.setDates(datesVoyages);
//		service.modifyDestination(destination);
		//List<DatesVoyages> datesVoyages =  service.getDatesVoyages(id);
//		=====================================================================
//      Je ne sais pas à quoi sert le datesVoyages à envoyer en date à la jsp
//		=====================================================================
//		DatesVoyages datesVoyages=service.getDatesById(id);
//		request.setAttribute("date", datesVoyages);
//		================================================================
		List<DatesVoyages> datesVoyages =  service.getDatesVoyages(id);
		request.setAttribute("datesVoyages", datesVoyages);
		request.setAttribute("destination", destination);
		List<Destination> destinations =  service.getDestinations();
		request.setAttribute("destinations", destinations);
		String page = "/show-dates.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
		rd.forward(request,response);
		}catch(IllegalArgumentException exception) {
			long id = Long.parseLong(request.getParameter("id"));
			List<DatesVoyages> datesVoyages =  service.getDatesVoyages(id);
			Destination destination = service.getDestinationById(id);
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
