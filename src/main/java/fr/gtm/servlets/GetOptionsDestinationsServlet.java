package fr.gtm.servlets;//
//
import java.io.IOException;//
//
import java.util.ArrayList;//
import java.util.List;//
//
import javax.servlet.RequestDispatcher;//
import javax.servlet.ServletException;//
import javax.servlet.annotation.WebServlet;//
import javax.servlet.http.HttpServlet;//
import javax.servlet.http.HttpServletRequest;//
import javax.servlet.http.HttpServletResponse;//
//
import fr.gtm.entities.Destination;//
import fr.gtm.services.DestinationServices;//
//
/**
 * Servlet gerant la recuperation des details d'une destination et renvoi a la page jsp affichant le detail et permettant la gestion  
 */
@WebServlet("/GetOptionsDestinationsServlet")//
public class GetOptionsDestinationsServlet extends HttpServlet {//
	private static final long serialVersionUID = 1L;//
//
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);// Liaison de la servlet d'authentification avec la couche de service
//		
		Destination destination = new Destination();//                           Declaration et instanciation de la destination que l'on souhaite renvoyer
		String ids = request.getParameter("id");//...............................recuperation de l'identifiant de la destination
		String region = request.getParameter("region");//                        recuperation du nom de la region de la destination
		String description = request.getParameter("description");//..............recuperation de la description de la region de la destination
//		
		long id = Long.parseLong(ids);//                                         conversion de l'identifiant de chaine de caractere a long car ce sont des long qui sont geres par la DAO
		destination.setId(id);//.................................................on definit l'identifiant de la destination que l'on souhaite revoyer : cela permet de gerer les requetes de la jsp par la suite
		destination.setRegion(region);//                                         on definit la region de la destination que l'on souhaite revoyer : cela permet de gerer les requetes de la jsp par la suite
		destination.setDescription(description);//...............................on definit la description de la destination que l'on souhaite revoyer : cela permet de gerer les requetes de la jsp par la suite
//		
		request.setAttribute("destination", destination);//......................ajout de la destination aux element a renvoyer a la jsp, les destinations sont stockees dans une variable nommee "destination"
		List<Destination> destinations =  service.getDestinations();//           appel au service pour recuperer l'ensemble des destinations pour les envoyer avec la jsp : cela permet de pouvoir gerer par la suite les requetes sur la jsp
		request.setAttribute("destinations", destinations);//....................ajout des destinations aux elements a renvoyer a la jsp, les destinations sont stockees dans une variable nommee "destinations"
		String page = "/show-options.jsp";//                                     declaration de la variable page contenant le nom de la jsp que l'on souhaite renvoyer
		RequestDispatcher rd = getServletContext().getRequestDispatcher(page);//.fabrication du chemin de la requete vers laquelle le serveur va renvoyer le client avec la jsp
		rd.forward(request,response);//..........................................envoi au server des donnees pour faire la jsp et la renvoyer a l'utilisateur
	}//
//
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {//
		doGet(request, response);//
	}//
//
}//