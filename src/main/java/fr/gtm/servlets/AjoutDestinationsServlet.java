package fr.gtm.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
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

import fr.gtm.entities.DatesVoyages;
import fr.gtm.entities.Destination;
import fr.gtm.entities.Image;
import fr.gtm.services.DestinationServices;

@WebServlet("/AjoutDestinationsServlet")
@MultipartConfig()
public class AjoutDestinationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(AjoutDestinationsServlet.class.getCanonicalName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjoutDestinationsServlet() {}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String folder = getServletContext().getInitParameter("upload-folder");
		// getParameter fonctionne en enctype="multipart/form-data" grace à l'annotation @MultipartConfig
		String name = request.getParameter("name");
		final Part filePart = request.getPart("simple-file");
		String region = request.getParameter("region");
		if(region.isBlank() || region.isEmpty()) {
			response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiToutesDestinations");
		}
		else {
			try {
				
				doTryAccessDeniedException(request,response,folder,name,filePart);
			
			}catch(AccessDeniedException exception) {
				
				doCatchAccessDeniedException(request,response);
			}
		}
		
		}
	
	private String getFileName(Part part) {
		final String partHeader = part.getHeader("content-disposition");
		LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
		if (content.trim().startsWith("filename")) {
		return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
		}
		}
		return null;
		}
	
	private void doCatchFileAlreadyExistsException(HttpServletRequest request, HttpServletResponse response,InputStream in, String fileName,Part filePart)  throws ServletException, IOException {
//		fermeture du fichier pour ne pas avoir de fuite de memoire
		in.close();
		// pour supprimer le fichier temporaire
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		Destination destination = new Destination();
		String region = request.getParameter("region");
		String description = request.getParameter("description");
		destination.setRegion(region);
		destination.setDescription(description);
		service.addDestination(destination);
		List<Destination> destinations= service.getDestinations();
		long id = 0;
		for(Destination d : destinations) {
			if(d.getId() > id) {
				id = d.getId();
			}
		}
		service.addImage(id, fileName);
		response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiToutesDestinations");
//		filePart.delete();
////		Recuperation informations pour ajouter les destinations
//		destinations =  service.getDestinations();
//		List<Image> images =   new ArrayList<Image>();
//		List<Image> imagesDestination =   new ArrayList<Image>();
//		Image image =   new Image();
//		for(Destination d : destinations) {
//			images = service.getImages(d.getId());
//			if(!images.isEmpty()) {
//				image = images.get(0);
//			}
//			else {
//				image = new Image();
//				image.setImage("defaut.jpg");
//			}
//			imagesDestination.add(image);
//		}
//		request.setAttribute("destinations", destinations);
//		request.setAttribute("imagesDestination", imagesDestination);
//		RequestDispatcher rd = getServletContext().getRequestDispatcher("/show-destinations.jsp");
//		rd.forward(request, response);
//		doGet(request, response);
	}
	
	private void doCatchAccessDeniedException(HttpServletRequest request, HttpServletResponse response)   throws ServletException, IOException {
//      ajout de la region			
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		Destination destination = new Destination();
		String region = request.getParameter("region");
		String description = request.getParameter("description");
		destination.setRegion(region);
		destination.setDescription(description);
		service.addDestination(destination);
		response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiToutesDestinations");
//		recuperation des destinations pour renvoyer sur la page principale
//		List<Destination> destinations= service.getDestinations();
//		List<Image> images =   new ArrayList<Image>();
//		List<Image> imagesDestination =   new ArrayList<Image>();
//		Image image =   new Image();
//		for(Destination d : destinations) {
//			images = service.getImages(d.getId());
//			if(!images.isEmpty()) {
//				image = images.get(0);
//			}
//			else {
//				image = new Image();
//				image.setImage("defaut.jpg");
//			}
//			imagesDestination.add(image);
//		}
//		request.setAttribute("destinations", destinations);
//		request.setAttribute("imagesDestination", imagesDestination);
//		RequestDispatcher rd = getServletContext().getRequestDispatcher("/show-destinations.jsp");
//		rd.forward(request, response);
//		doGet(request, response);
	}
	
	private void doTryAccessDeniedException(HttpServletRequest request, HttpServletResponse response,String folder, String name, Part filePart)  throws ServletException, IOException {
		final String fileName = getFileName(filePart);
		// copie le fichier reçu vers son emplacement définitif
				Path path = FileSystems.getDefault().getPath(folder, fileName);
				InputStream in = filePart.getInputStream();
				try {
					
					doTryFileAlreadyExistsException(request,response,folder,name,filePart,fileName,path,in);
					
				}
				catch(FileAlreadyExistsException exception) {
					
					doCatchFileAlreadyExistsException(request,response,in,fileName,filePart);
					
				}
	}
	
	private void doTryFileAlreadyExistsException(HttpServletRequest request, HttpServletResponse response,String folder, String name, Part filePart, String fileName, Path path, InputStream in)  throws ServletException, IOException {
		Files.copy(in, path);
		in.close();
		// pour supprimer le fichier temporaire
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		Destination destination = new Destination();
		String region = request.getParameter("region");
//		========================================================
//		Test du champs region
//		========================================================
		String description = request.getParameter("description");
		destination.setRegion(region);
		destination.setDescription(description);
		service.addDestination(destination);
		List<Destination> destinations=new ArrayList<Destination>();
		destinations=service.getDestinations();
		long id = 0;
		for(Destination d : destinations) {
			if(d.getId() > id) {
				id = d.getId();
			}
		}
		service.addImage(id, fileName);
		filePart.delete();
		response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiToutesDestinations");
//		Recuperation informations pour ajouter les destinations
//		destinations =  service.getDestinations();
//		List<Image> images =   new ArrayList<Image>();
//		List<Image> imagesDestination =   new ArrayList<Image>();
//		Image image =   new Image();
//		for(Destination d : destinations) {
//			images = service.getImages(d.getId());
//			if(!images.isEmpty()) {
//				image = images.get(0);
//			}
//			else {
//				image = new Image();
//				image.setImage("defaut.jpg");
//			}
//			imagesDestination.add(image);
//		}
//		request.setAttribute("destinations", destinations);
//		request.setAttribute("imagesDestination", imagesDestination);
//		RequestDispatcher rd = getServletContext().getRequestDispatcher("/show-destinations.jsp");
//		rd.forward(request, response);
//		doGet(request, response);
		
	}
}

