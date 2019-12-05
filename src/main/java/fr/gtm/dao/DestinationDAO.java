package fr.gtm.dao;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;

import fr.gtm.entities.Commercial;
import fr.gtm.entities.DatesVoyages;
import fr.gtm.entities.Destination;
import fr.gtm.entities.Image;

public class DestinationDAO extends AbstractDAO<Destination, Long> {
	private static final Logger LOG = Logger.getLogger("bovoyages");
	
	public DestinationDAO(EntityManagerFactory emf) {
		super(emf, Destination.class);
		
	}

	public Destination getDestinationById(long id) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Destination destination = em.find(Destination.class, id);
		em.close();
		return destination;
	}
	
	public void delete(long id) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Destination destination = em.find(Destination.class, id);
		destination.setDeleted(1);
		update(destination);
		em.close();
	}

	public List<Destination> getDestinations() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		List<Destination> destinations = em.createNamedQuery("Destination.getDestinations", Destination.class)
				.getResultList();
		em.close();
		List<Destination> destinationsValid = new ArrayList<Destination>();
		for(Destination d : destinations) {
			if(d.getDeleted() == 0) destinationsValid.add(d);
		}
		return destinationsValid;
	}

	public Destination addDestinationDate(long destinationID, DatesVoyages newDate) {
		EntityManager em=getEntityManagerFactory().createEntityManager();
		Destination d=em.find(Destination.class, destinationID);
		List<DatesVoyages> dates=new ArrayList<DatesVoyages>();
		for(DatesVoyages date:d.getDates()) {
			dates.add(date);
		}
		dates.add(newDate);
		d.setDates(dates);
		this.update(d);
		em.close();
		return d;
	}

	public Destination deleteDestinationDate(long destinationID, DatesVoyages datesToRemove) {
		DatesVoyagesDAO daoDate=new DatesVoyagesDAO(getEntityManagerFactory());
		EntityManager em=getEntityManagerFactory().createEntityManager();
		Destination d=em.find(Destination.class, destinationID);
		List<DatesVoyages> dates=new ArrayList<DatesVoyages>();
		for(DatesVoyages date:d.getDates()) {
			if(date.getId() == datesToRemove.getId()) {
				date.setDeleted(1);
			}
			dates.add(date);
		}
//		Iterator<DatesVoyages> iterator = dates.iterator();
//		while (iterator.hasNext()) {
//			if (iterator.next().getId() == datesToRemove.getId()) {
//				daoDate.delete(datesToRemove.getId());
//				iterator.remove();
//				
//			}
//		}
		d.setDates(dates);
		this.update(d);
		em.close();
		return d;
	}

	public Destination modifyDestinationDate(long destinationID, DatesVoyages newDates, long dateID) {
		DatesVoyagesDAO daoDate=new DatesVoyagesDAO(getEntityManagerFactory());
		EntityManager em=getEntityManagerFactory().createEntityManager();
		Destination d=em.find(Destination.class, destinationID);
		List<DatesVoyages> dates=new ArrayList<DatesVoyages>();
		for(DatesVoyages date:d.getDates()) {
			dates.add(date);
		}
		Iterator<DatesVoyages> iterator = dates.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getId() == dateID) {
				iterator.remove();
			}
		}
		newDates.setId(dateID);
		dates.add(newDates);
		d.setDates(dates);
		this.update(d);
		em.close();
		return d;
	}
	
	public DatesVoyages getDatesById(long id) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		DatesVoyages dates= em.find(DatesVoyages.class, id);
		em.close();
		return dates;
	}
	
	public List<DatesVoyages> getDestinationDates(long id) {
		EntityManager em=getEntityManagerFactory().createEntityManager();
		Destination d=em.find(Destination.class, id);
		List<DatesVoyages> dates=new ArrayList<DatesVoyages>();
		for(DatesVoyages date:d.getDates()) {
			if(date.getDeleted() == 0) dates.add(date);
		}
		em.close();
		return dates;
	}
	
	public void addDestinationImage(long destinationID, String nomImage) {
		EntityManager em=getEntityManagerFactory().createEntityManager();
		Destination d=em.find(Destination.class, destinationID);
		Image imgToAdd=new Image(nomImage,destinationID);
		List<Image> images=new ArrayList<Image>();
		for(Image img:d.getImage()) {
			if(!img.getImage().equals(nomImage)) images.add(img);
		}
		images.add(imgToAdd);
		d.setImage(images);
		this.update(d);
		em.close();
	}
	
	public List<Image> getImages(Long id) {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		Destination destination = em.find(Destination.class, id);
		List<Image> images = new ArrayList<Image>();
		for(Image image : destination.getImage()) {
			images.add(image);
		}
		em.close();
		return images;
	}
	
	public List<Commercial> getCommerciaux() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		List<Commercial> commerciaux = em.createNamedQuery("Commercial.getCommerciaux", Commercial.class)
				.getResultList();
		em.close();
		return commerciaux;
	}
	
	public boolean authentification(String identifiant,String password) {
//		List<Commercial> commercialAuthentifie;
		List<Commercial> commerciaux = getCommerciaux();
		for(Commercial commercial : commerciaux) {
			if(commercial.getIdentifiant().toLowerCase().equals(identifiant.toLowerCase())) {
				EntityManager em = getEntityManagerFactory().createEntityManager();
//				List<String> commercialAuthentifie = em.createNativeQuery("SELECT c.username FROM commerciaux c WHERE c.sha = ?1", Commercial.class).setParameter(1, password).getResultList();
				try {Object commercialAuthentifie = em.createNativeQuery("SELECT c.username FROM commerciaux c WHERE c.sha = ?1 AND c.username = ?2").setParameter(1, password).setParameter(2,  identifiant).getSingleResult();
				LOG.info(">>> \n \n \n"
						+ ">>> "+ commercialAuthentifie);
				     em.close();
					return true;
				}catch (NoResultException n) {
					em.close();
					return false;
				  }
			}
		}
		return false;
	}
	
	public Destination promouvoirDestination(long destinationID, DatesVoyages dateAPromouvoir) {
		DatesVoyagesDAO daoDate=new DatesVoyagesDAO(getEntityManagerFactory());
		EntityManager em=getEntityManagerFactory().createEntityManager();
		Destination d=em.find(Destination.class, destinationID);
		List<DatesVoyages> dates=new ArrayList<DatesVoyages>();
		for(DatesVoyages date:d.getDates()) {
			if(date.getId() == dateAPromouvoir.getId()) {
				date.setPromotion(1);
			}
			dates.add(date);
		}
//		Iterator<DatesVoyages> iterator = dates.iterator();
//		while (iterator.hasNext()) {
//			if (iterator.next().getId() == datesToRemove.getId()) {
//				daoDate.delete(datesToRemove.getId());
//				iterator.remove();
//				
//			}
//		}
		d.setDates(dates);
		this.update(d);
		em.close();
		return d;
	}
	
	public void updateLazy(Destination destination) {
		DatesVoyagesDAO daoDate=new DatesVoyagesDAO(getEntityManagerFactory());
		EntityManager em=getEntityManagerFactory().createEntityManager();
		Destination d=em.find(Destination.class, destination.getId());
		d.setRegion(destination.getRegion());
		d.setDescription(destination.getDescription());
		List<DatesVoyages> dates=new ArrayList<DatesVoyages>();
		for(DatesVoyages date:d.getDates()) {
			dates.add(date);
		}
		d.setDates(dates);
		this.update(d);
		em.close();
	}

}
