package fr.gtm.servlets;

import java.io.IOException;
import java.io.InputStream;
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

import fr.gtm.entities.Destination;
import fr.gtm.entities.Image;
import fr.gtm.services.DestinationServices;

@WebServlet("/AjoutImageServlet")
@MultipartConfig()
public class AjoutImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(AjoutImageServlet.class.getCanonicalName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjoutImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

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
//		
		String folder = getServletContext().getInitParameter("upload-folder");
		// getParameter fonctionne en enctype="multipart/form-data" grace à l'annotation @MultipartConfig
		String name = request.getParameter("name");
		LOGGER.info("Paramètre 'name' == "+name);
		final Part filePart = request.getPart("simple-file");
		final String fileName = getFileName(filePart);
		// copie le fichier reçu vers son emplacement définitif
		Path path = FileSystems.getDefault().getPath(folder, fileName);
		InputStream in = filePart.getInputStream();
		try {
		Files.copy(in, path);
		in.close();
		// pour supprimer le fichier temporaire
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
		String ids = request.getParameter("id");
		long id = Long.parseLong(ids);
		service.addImage(id, fileName);
		filePart.delete();
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
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/show-destinations.jsp");
		rd.forward(request, response);
		doGet(request, response);
		}
		catch(FileAlreadyExistsException exception) {
			
			DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);
			String ids = request.getParameter("id");
			long id = Long.parseLong(ids);
			service.addImage(id, fileName);
			filePart.delete();
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
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/show-destinations.jsp");
			rd.forward(request, response);
			doGet(request, response);
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
	}