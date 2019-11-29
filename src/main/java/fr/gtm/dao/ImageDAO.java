package fr.gtm.dao;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import fr.gtm.entities.Image;

public class ImageDAO  extends AbstractDAO<Image, String> {

	
	public ImageDAO(EntityManagerFactory emf) {
		super(emf, Image.class);
	}
	
}
