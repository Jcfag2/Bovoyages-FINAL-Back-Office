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
 * Servlet gerant l'authentification des commerciaux. L'identifiant et le mot de passe sont envoyes aux services qui les envoies à la DAO
 * qui fait une requête SQL à la base pour comparer le SHA-256 du mot de passe entre avec celui en base.
 * 
 * Un booleen est renvoye à true si l'utilisateur a rentre le bon identifiant et mot de passe et false autrement
 * 
 * Si l'authentification a reussi, la servlet recupère les destinations en bases ainsi que la première image qui leurs sont associees ou
 * une image par defaut s'il n'y a pas d'image associee et construit la page jsp show-destinations qu'elle envoit au client
 */
@WebServlet("/AuthentificationServlet")
@MultipartConfig()
public class AuthentificationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * Variable utilsée pour faire des logs afin d'aider à comprendre les problèmes
	 */
	private static final Logger LOG = Logger.getLogger("bovoyages");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DestinationServices service = (DestinationServices) getServletContext().getAttribute(Constantes.DESTINATIONS_SERVICE);// Liaison de la servlet d'authentification avec la couche de service
//		
		String identifiant = request.getParameter("identifiant");//                          recuperation de l'identifiant entre par l'utilisateur souhaitant se connecter
		String password = request.getParameter("password");//................................recuperation du mot de passe de l'utilisateur souhaitant s'authentifier
//		
		 try {//                                                                             bloc de code protégé d'un probleme d'absence d'algorithme 
			 String SHA256 = new Util().chiffrageSHA256(password);//.........................chiffrage SHA-256 du mot de passe entre par l'utilisateur souhaitant se connecter
			 boolean onContinue = service.authentification(identifiant,SHA256);//            appel du service pour vérifier que l'utilisateur a le droit de se connecter => renvoi true si le couple (identifiant,mot de passe) est valide, false sinon
//			 
			 if(onContinue) {//                                                              execution si la condition du if est valide
				    List<Destination> destinations =  service.getDestinations();//...........appel au service pour recuperer les destinations
					List<Image> images =   new ArrayList<Image>();//                         variable tampon pour recuperer les images en base => on parcourera cette liste pour prendre un image pour chaque destination
					List<Image> imagesDestination =   new ArrayList<Image>();//..............listes des images associees aux destinations
					Image image =   new Image();//                                           variable tampon utilisee pour recuperer la premiere image de chaque liste d'images associée à une destination
//					
					for(Destination destination : destinations) {//                          On boucle sur les destinations pour récuperer une image pour chacune
						images = service.getImages(destination.getId());//...................appel au service pour recuperer toutes les images de la destination courante 
						if(!images.isEmpty()) {//                                            bloc execute s'il y a au moins une image associée à la destination
							image = images.get(0);//.........................................récupération de la première image de la liste d'image de la destination courante. On affichera cette image dans la page jsp pour illustrer la destination
						}//
						else {//                                                             bloc executé s'il n'y a pas d'image associée à la destination
							image = new Image();//...........................................nouvelle instance pour la variable image (peut-être inutile, il suffirait peut-être directement d'écraser son unique attribut qui est un nom)
							image.setImage("defaut.jpg");//                                  on donne comme nom à la variable image le nom de l'image par défaut
						}//
						imagesDestination.add(image);//                                      ajout de l'image à la liste des images qui vont illustrer les destinations
					}//
					request.setAttribute("destinations", destinations);//                    ajout des destinations aux element à renvoyer à la jsp, les destinations sont stockées dans une variable nommée "destinations"
					request.setAttribute("imagesDestination", imagesDestination);//..........ajout des images illustrant les destinations aux element à renvoyer à la jsp, les images sont stockées dans une variable nommée "imagesDestination"
					String page = "/show-destinations.jsp";//                                declaration de la variable page contenant le nom de la jsp que l'on souhaite renvoyer
					RequestDispatcher rd = getServletContext().getRequestDispatcher(page);//.fabrication de la jsp
					rd.forward(request,response);//                                          envoi de la page jsp au server qui l'enverra à l'utilisateur
			 }//
			 else {//
				String page = "/index.jsp";//                                                declaration de la variable page contenant le nom de la jsp que l'on souhaite renvoyer, en l'occurence l'index 
				RequestDispatcher rd = getServletContext().getRequestDispatcher(page);//.....fabrication de la jsp
				rd.forward(request,response);//                                              envoi de la page jsp au server qui l'enverra à l'utilisateur 
			 }//
			  } catch (NoSuchAlgorithmException e) {//                                       interception de l'exception levée en cas d'algorithme introuvable
				  LOG.info(">>> \n \n \n \n \n \n \n \n \n \n ");//..........................affichage dans les logs en cas de problemes sur l'algorithme. En l'occurence, c'est pour faire un espace important pour mieux voir le problème
				  LOG.info(">>> probleme sur le mot de passe");//                            affichage clair du probleme
			  }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
		
	}

}
