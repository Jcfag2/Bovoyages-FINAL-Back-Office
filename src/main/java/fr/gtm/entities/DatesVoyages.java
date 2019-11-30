package fr.gtm.entities;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dates_voyages")
public class DatesVoyages {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_date_voyage")
	private long id;
//	@Column(name = "date_depart")
//	private Date dateDepart;
//	@Column(name = "date_retour")
//	private Date dateRetour;
	@Column(name = "date_depart")
	private LocalDateTime dateDepart;
	@Column(name = "date_retour")
	private LocalDateTime dateRetour;
	@Column(name = "prixHT")
	private double prixHT;
	@Column(name = "deleted")
	private int deleted;
	@Column(name = "nb_places")
	private int nbPlaces;
	@Column(name = "fk_destination")
	private long fkDestination;
	@Column(name = "promotion")
	private long promotion;

public DatesVoyages() {}

	public DatesVoyages(LocalDateTime dateDepart, LocalDateTime dateRetour, double prixHT, int deleted, int nbPlaces,
			long fkDestination) {
		super();
		this.dateDepart = dateDepart;
		this.dateRetour = dateRetour;
		this.prixHT = prixHT;
		this.deleted = deleted;
		this.nbPlaces = nbPlaces;
		this.fkDestination = fkDestination;
	}
	
	public DatesVoyages(LocalDateTime dateDepart, LocalDateTime dateRetour, double prixHT, int deleted, int nbPlaces,
			long fkDestination, long promotion) {
		super();
		this.dateDepart = dateDepart;
		this.dateRetour = dateRetour;
		this.prixHT = prixHT;
		this.deleted = deleted;
		this.nbPlaces = nbPlaces;
		this.fkDestination = fkDestination;
		this.promotion = promotion;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getDateDepart() {
		return dateDepart;
	}

	public void setDateDepart(LocalDateTime dateDepart) {
		this.dateDepart = dateDepart;
	}

	public LocalDateTime getDateRetour() {
		return dateRetour;
	}

	public void setDateRetour(LocalDateTime dateRetour) {
		this.dateRetour = dateRetour;
	}

	public double getPrixHT() {
		return prixHT;
	}

	public void setPrixHT(float prixHT) {
		this.prixHT = prixHT;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public int getNbPlaces() {
		return nbPlaces;
	}

	public void setNbPlaces(int nbPlaces) {
		this.nbPlaces = nbPlaces;
	}

	public long getFkDestination() {
		return fkDestination;
	}

	public void setFkDestination(long fkDestination) {
		this.fkDestination = fkDestination;
	}

	public long getPromotion() {
		return promotion;
	}

	public void setPromotion(long promotion) {
		this.promotion = promotion;
	}

}
