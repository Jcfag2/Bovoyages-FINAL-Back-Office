package fr.gtm.servlets;//
//
import java.io.IOException;//
//
import java.io.InputStream;//
import java.nio.file.AccessDeniedException;//
import java.nio.file.FileAlreadyExistsException;//
import java.nio.file.FileSystems;//
import java.nio.file.Files;//
import java.nio.file.Path;//
import java.util.ArrayList;//
import java.util.List;//
import java.util.logging.Level;//
import java.util.logging.Logger;//
//
import javax.servlet.RequestDispatcher;//
import javax.servlet.ServletException;//
import javax.servlet.annotation.MultipartConfig;//
import javax.servlet.annotation.WebServlet;//
import javax.servlet.http.HttpServlet;//
import javax.servlet.http.HttpServletRequest;//
import javax.servlet.http.HttpServletResponse;//
import javax.servlet.http.Part;//
//
import fr.gtm.entities.DatesVoyages;//
import fr.gtm.entities.Destination;//
import fr.gtm.entities.Image;//
import fr.gtm.services.DestinationServices;//
//
/**
 * Servlet gerant l'ajout de destination à la base de données. Une destination doit posséder un nom de region associé. Si aucune
 * region n'est renseignée, il n'y a pas d'ajout de la destination. En plus de la region, il peut être renseigné un descriptif et une
 * image
 */
@WebServlet("/AjoutDestinationsServlet")//
@MultipartConfig()//
public class AjoutDestinationsServlet extends HttpServlet {//
	private static final long serialVersionUID = 1L;//
	/**
	 * Variable utilsee pour faire des logs afin d'aider a comprendre les problemes
	 */
	private final static Logger LOGGER = Logger.getLogger(AjoutDestinationsServlet.class.getCanonicalName());//
//       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjoutDestinationsServlet() {}// Constructeur par defaut de la Servlet

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//
		response.getWriter().append("Served at: ").append(request.getContextPath());//
	}//
//
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//
//		
		String folder = getServletContext().getInitParameter("upload-folder");//                          recuperation du nom du dossier ou se trouvent les images. Ce nom est parametre dans le web.xml contenu dans le dossier WEB-INF du webapp
		// getParameter fonctionne en enctype="multipart/form-data" grace à l'annotation @MultipartConfig
		String name = request.getParameter("name");//                                                     
		final Part filePart = request.getPart("simple-file");//
		String region = request.getParameter("region");//                                                 recuperation du nom de la region de la destination
		if(region.isBlank() || region.isEmpty()) {//......................................................si aucun nom de region n'a été entré ou qu'il n'y que des espaces, la condion est true
			response.sendRedirect("http://localhost:9090/bovoyage2/RenvoiToutesDestinations");//          redirection de la requete vers une autre servlet qui ne fera que de la récupération de données. Cela empêche de répéter la requête de la présente servlet en rafraichissant la page
		}//
		else {//                                                                     si la il y bien un nom de region, on entre dans ce bloc
			try {//
//				
				doTryAccessDeniedException(request,response,folder,name,filePart);// suite du script s'il n'y a pas de problèmes d'acces au dossier des images ou à l'image que l'on souhaite ajouter à la base
//			
			}catch(AccessDeniedException exception) {//         si le try a échoué, on entre dans ce bloque
//				
				doCatchAccessDeniedException(request,response);// appelle d'une methode presente dans la classe qui palie au problème d'acces
			}//
		}//
//		
		}//
//	
	/**
	 * @param part
	 * argument de class Part. Dans notre classe, c'est l'image qui est l'objet Part
	 * @return String :
	 *  retour de type String. Dans notre classe, c'est le nom de l'image que l'on souhaite renvoyer dans cette methode
	 */
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
//
	/**
	 * @param request
	 * argument de class HttpServletRequest. C'est un paramètre obligatoire dans les servlets correspondant à la requête que le client
	 * a formulé. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas où l'image ajouté est déjà présente dans le dossier des images.
	 * 
	 * @param response
	 * argument de class HttpServletResponse. C'est un paramètre obligatoire dans les servlets correspondant à la reponse de la servlet
	 * et qui va être envoyé au serveur. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas où l'image ajouté est déjà présente dans le dossier des images.
	 * 
	 *  @param in
	 *  argument de class InputStream. C'est le flux de donnees vers l'image que l'on souhaite ajouter. Dans la methode, il est immediatement
	 *  ferme car on a intercepté l'exception du fichier existant deja.
	 *  
	 *  @param fileName
	 *  argument de class String. C'est le nom de l'image que l'on souhaite ajouter en base. Comme le fichier existe deja dans le dossier, 
	 *  on ajoute simplement le nom dans la base de donnees.
	 *  
	 *  @param filePart
	 * argument de class Part. C'est le part de l'image que l'on souhaite ajouter à la base. Il est immediatement ferme car il est immediatement
	 *  ferme car on a intercepté l'exception du fichier existant deja.
	 *  
	 *  @throws ServletException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 *  @throws IOException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 */
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
	}
//	
	/**
	 * @param request
	 * argument de class HttpServletRequest. C'est un paramètre obligatoire dans les servlets correspondant à la requête que le client
	 * a formulé. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas où aucune image n'a été fournie. La destination est alors ajoutée mais sans avoir d'images
	 * 
	 * @param response
	 * argument de class HttpServletResponse. C'est un paramètre obligatoire dans les servlets correspondant à la reponse de la servlet
	 * et qui va être envoyé au serveur. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas où aucune image n'a été fournie. La destination est alors ajoutée mais sans avoir d'images
	 *  
	 *  @throws ServletException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 *  @throws IOException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 */
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
	}
//	
	/**
	 * @param request
	 * argument de class HttpServletRequest. C'est un paramètre obligatoire dans les servlets correspondant à la requête que le client
	 * a formulé. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas une image a été fournie pour l'ajouter en base de données
	 * 
	 * @param response
	 * argument de class HttpServletResponse. C'est un paramètre obligatoire dans les servlets correspondant à la reponse de la servlet
	 * et qui va être envoyé au serveur. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas une image a été fournie pour l'ajouter en base de données
	 * 
	 *  @param folder
	 *  argument de class String. C'est le nom du dossier où l'image que l'on souhaite ajouter va être ajouté.
	 *  
	 *  @param name
	 *  argument de class String.
	 *  
	 *  @param filePart
	 * argument de class Part. C'est le part de l'image que l'on souhaite ajouter à la base.
	 *  
	 *  @throws ServletException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 *  @throws IOException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 */
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
//	
	/**
	 * @param request
	 * argument de class HttpServletRequest. C'est un paramètre obligatoire dans les servlets correspondant à la requête que le client
	 * a formulé. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas une image a été fournie pour l'ajouter en base de données
	 * 
	 * @param response
	 * argument de class HttpServletResponse. C'est un paramètre obligatoire dans les servlets correspondant à la reponse de la servlet
	 * et qui va être envoyé au serveur. Pour notre classe, comme il y a deux try/catch, le code a été scindé en plusieurs méthodes pour rester lisible.
	 * Cette methode gère le cas une image a été fournie pour l'ajouter en base de données
	 * 
	 *  @param folder
	 *  argument de class String. C'est le nom du dossier où l'image que l'on souhaite ajouter va être ajouté.
	 *  
	 *  @param name
	 *  argument de class String.
	 *  
	 *  @param filePart
	 * argument de class Part. C'est le part de l'image que l'on souhaite ajouter à la base.
	 *  
	 *  @param fileName
	 *  argument de class String. C'est le nom de l'image que l'on souhaite ajouter en base.
	 *  
	 *  @param path
	 *  argument de class Path. C'est le chemin vers l'image que l'on souhaite ajouter en base.
	 *  
	 *  @param in
	 *  argument de class InputStream. C'est le flux de donnees correspondant a l'image que l'on veut placer dans le dossier des images
	 *  
	 *  @throws ServletException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 *  @throws IOException
	 *  C'est une interception obligatoire dans les servlets qui laisse la machine virtuelle gérer
	 *  
	 */
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
	}
}

