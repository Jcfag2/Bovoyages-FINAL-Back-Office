package fr.gtm.services;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import fr.gtm.dao.DestinationDAO;
import fr.gtm.entities.DatesVoyages;
import fr.gtm.entities.Destination;
import fr.gtm.entities.Image;

public class DestinationServices {
	private DestinationDAO dao;
	
	public DestinationServices(EntityManagerFactory emf) {
		dao=new DestinationDAO(emf);
	}
	
public List<Destination> getDestinations(){
	return dao.getDestinations();
}

public Destination getDestinationById(long id) {
	return dao.getDestinationById(id);
}

public DatesVoyages getDatesById(long id) {
	return dao.getDatesById(id);
}

public void deleteDestination(long id) {
	dao.delete(id);
}

public void modifyDestination(Destination destination) {
	dao.update(destination);
}
public List<DatesVoyages> getDatesVoyages(long id){
	return dao.getDestinationDates(id);
}

public void addDatesVoyages(long destinationID, DatesVoyages dates) {
	dao.addDestinationDate(destinationID, dates);
}

public void deleteDatesVoyages(long destinationID, DatesVoyages dates) {
	dao.deleteDestinationDate(destinationID, dates);
}

public void modifyDatesVoyages(long destinationID, DatesVoyages newDatesVoyages,long dateID) {
	dao.modifyDestinationDate(destinationID, newDatesVoyages, dateID);
}

public void addImage(long destinationID, String nomImage) {
	dao.addDestinationImage(destinationID, nomImage);
}

public List<Image> getImages(long id) {
	return dao.getImages(id);
}

public void addDestination(Destination destination){
	dao.create(destination);
}

public boolean authentification(String identifiant,String password) {
	return dao.authentification(identifiant,password);
}

public void promouvoirDestination(long destinationID, DatesVoyages dateAPromouvoir) {
	dao.promouvoirDestination(destinationID, dateAPromouvoir);
}

public void modifyDestinationLazy(Destination destination) {
	dao.updateLazy(destination);
}

}