package fr.gtm.servlets;

import java.io.IOException;
import java.io.InputStream;
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
import fr.gtm.services.DestinationServices;

@WebServlet("/AjoutDestinationsServlet")
@MultipartConfig()
public class AjoutDestinationsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final static Logger LOGGER = Logger.getLogger(AjoutImageServlet.class.getCanonicalName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjoutDestinationsServlet() {
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
		// TODO Auto-generated method stub
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
		Destination destination = new Destination();
		String region = request.getParameter("region");
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
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.jsp");
		rd.forward(request, response);
		doGet(request, response);
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