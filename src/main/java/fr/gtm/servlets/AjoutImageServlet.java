package fr.gtm.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    public AjoutImageServlet() {}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
		try {
		try {
		String folder = getServletContext().getInitParameter("upload-folder");
		// getParameter fonctionne en enctype="multipart/form-data" grace à l'annotation @MultipartConfig
		String name = request.getParameter("name");
		LOGGER.info("Paramètre 'name' == "+name);
		final Part filePart = request.getPart("simple-file");
		final String fileName = getFileName(filePart);
		// copie le fichier reçu vers son emplacement définitif
		Path path = FileSystems.getDefault().getPath(folder, fileName);
		InputStream in = filePart.getInputStream();
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
		response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDestinationParticuliere?id="+id);
		}
		catch(FileAlreadyExistsException exception) {
			
			String id = request.getParameter("id");
			response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDestinationParticuliere?id="+id);
		}
		}catch(AccessDeniedException exception) {
			String id = request.getParameter("id");
			response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiDestinationParticuliere?id="+id);
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