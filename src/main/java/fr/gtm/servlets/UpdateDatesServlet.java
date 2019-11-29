package fr.gtm.servlets;

import java.io.IOException;
import java.sql.Date;

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
		long dateID = Long.parseLong(request.getParameter("dateID"));
		long destinationID = Long.parseLong(request.getParameter("destinationID"));
		DatesVoyages date=service.getDatesById(dateID);
		Destination destination=service.getDestinationById(destinationID);
		request.setAttribute("date", date);
		request.setAttribute("destination", destination);
		String page = "/edit-date.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
		rd.forward(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
