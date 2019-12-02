package fr.gtm.servlets;//
//
import java.io.IOException;//
import java.io.InputStream;//
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
import fr.gtm.servlets.Constantes;//
import fr.gtm.entities.Destination;//
import fr.gtm.entities.Image;//
import fr.gtm.services.DestinationServices;//
//
/**
 * Servlet gerant la recuperation des destinations disponibles dans la base de donnees. Elle renvoit a la page principale qui est 
 * la liste des destinations avec une image illustrant la destination et le texte descriptif.
 */
@WebServlet("/GetAllDestinationsServlet")//
@MultipartConfig()//
public class GetAllDestinationsServlet extends HttpServlet {//
	private static final long serialVersionUID = 1L;//
	/**
	 * Variable utilsee pour faire des logs afin d'aider a comprendre les problemes
	 */
	private static final Logger LOG = Logger.getLogger("bovoyages");//

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);// Liaison de la servlet d'authentification avec la couche de service
//
		List<Destination> destinations =  service.getDestinations();//                                        appel au service pour recuperer les destinations 
		List<Image> images =   new ArrayList<Image>();//......................................................variable tampon pour recuperer les images en base => on parcourera cette liste pour prendre un image pour chaque destination
		List<Image> imagesDestination =   new ArrayList<Image>();//                                           listes des images associees aux destinations
		Image image =   new Image();//........................................................................variable tampon utilisee pour recuperer la premiere image de chaque liste d'images associee a une destination
//		
		for(Destination destination : destinations) {//                                                       On boucle sur les destinations pour recuperer une image pour chacune
			images = service.getImages(destination.getId());//................................................appel au service pour recuperer toutes les images de la destination courante 
			if(!images.isEmpty()) {//                                                                         bloc execute s'il y a au moins une image associee a la destination
				image = images.get(0);//......................................................................recuperation de la premiere image de la liste d'image de la destination courante. On affichera cette image dans la page jsp pour illustrer la destination
			}//                                                                                               
			else {//                                                                                          bloc execute s'il n'y a pas d'image associee a la destination
				image = new Image();//........................................................................nouvelle instance pour la variable image (peut-etre inutile, il suffirait peut-etre directement d'ecraser son unique attribut qui est un nom)
				image.setImage("defaut.jpg");//                                                               on donne comme nom a la variable image le nom de l'image par defaut
			}//
			imagesDestination.add(image);//...................................................................ajout de l'image a la liste des images qui vont illustrer les destinations
		}//
		request.setAttribute("destinations", destinations);//                                                 ajout des destinations aux element a renvoyer a la jsp, les destinations sont stockees dans une variable nommee "destinations"
		request.setAttribute("imagesDestination", imagesDestination);//.......................................ajout des images illustrant les destinations aux element a renvoyer a la jsp, les images sont stockees dans une variable nommee "imagesDestination"
		String page = "/show-destinations.jsp";//                                                             declaration de la variable page contenant le nom de la jsp que l'on souhaite renvoyer
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);//..............................fabrication de la jsp
		rd.forward(request,response);//                                                                       envoi de la page jsp au server qui l'enverra a l'utilisateur
//		
	}//
//
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//
//
		doGet(request, response);//
//		
	}//
//
}//
