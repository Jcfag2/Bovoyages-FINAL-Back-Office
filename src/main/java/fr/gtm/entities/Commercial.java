package fr.gtm.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="commerciaux")
@NamedQueries({
	@NamedQuery(name = "Commercial.getCommerciaux", query = "SELECT c FROM Commercial c order by c.id asc")
//	@NamedQuery(name = "identificationCommercial.getCommercial", query = "SELECT c FROM Commercial c WHERE c.sha = ?1")
})
public class Commercial {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pk_commercial")
	private long id;
	@Column(name = "username")
	private String identifiant;

	public Commercial() {
	}

	public Commercial(String username) {
		this.identifiant = username;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}


}
