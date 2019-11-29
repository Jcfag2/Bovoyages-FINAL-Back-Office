package fr.gtm.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
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
import fr.gtm.Util;
import fr.gtm.services.DestinationServices;


/**
 * Servlet implementation class GetAllDestinationsServlet
 */
@WebServlet("/AuthentificationServlet")
@MultipartConfig()
public class AuthentificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger("bovoyages");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		String page = "";
//		
		String identifiant = request.getParameter("identifiant");
		String password = request.getParameter("password");
		 try {
			 String SHA256 = new Util().chiffrageSHA256(password);
			 boolean onContinue = service.authentification(identifiant,SHA256);
			 if(onContinue) {
				    List<Destination> destinations =  service.getDestinations();
					List<Image> images =   new ArrayList<Image>();
					List<Image> imagesDestination =   new ArrayList<Image>();
					Image image =   new Image();
					for(Destination destination : destinations) {
						images = service.getImages(destination.getId());
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
			 else {
				page = "/index.jsp";
				RequestDispatcher rd = getServletContext().getRequestDispatcher(page);
				rd.forward(request,response);
			 }
			  } catch (NoSuchAlgorithmException e) {
				  LOG.info(">>>                             ");
				  LOG.info(">>> probleme sur le mot de passe");
			  }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		
	}

}
