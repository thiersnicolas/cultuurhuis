package be.vdab.entities;

public class Klant {
	
private long id;
private String voornaam;
private String familienaam;
private String straat;
private String huisnr;
private String postcode;
private String gemeente;
private String gebruikersnaam;
private String paswoord;

public Klant() {}

/***
	vanuit db
*/
private Klant(long id, String familienaam, String voornaam, String straat, String huisnr, String postcode, String gemeente,
		String gebruikersnaam, String paswoord) {
	this.id = id;
	this.voornaam = voornaam;
	this.familienaam = familienaam;
	this.straat = straat;
	this.huisnr = huisnr;
	this.postcode = postcode;
	this.gemeente = gemeente;
	this.gebruikersnaam = gebruikersnaam;
	this.paswoord = paswoord;
}

public static Klant klantUitDatabase(long id, String voornaam, String familienaam, String straat, String huisnr, String postcode, String gemeente,
		String gebruikersnaam, String paswoord) {
		return new Klant(id, voornaam, familienaam,  straat, huisnr, postcode, gemeente,
				gebruikersnaam, paswoord);
}

/**
	nieuwe klant
 */
private Klant(String voornaam, String familienaam, String straat, String huisnr, String postcode, String gemeente, String gebruikersnaam,
		String paswoord) {
	this.voornaam = voornaam;
	this.familienaam = familienaam;
	this.straat = straat;
	this.huisnr = huisnr;
	this.postcode = postcode;
	this.gemeente = gemeente;
	this.gebruikersnaam = gebruikersnaam;
	this.paswoord = paswoord;
}

public static Klant klantNieuw(String voornaam, String familienaam, String straat, String huisnr, String postcode, String gemeente, String gebruikersnaam,
		String paswoord) {
	return new Klant(voornaam, familienaam,  straat, huisnr, postcode, gemeente,
				gebruikersnaam, paswoord);
}

public long getId() {
	return id;
}

public String getVoornaam() {
	return voornaam;
}

public String getFamilienaam() {
	return familienaam;
}


public String getStraat() {
	return straat;
}


public String getHuisnr() {
	return huisnr;
}


public String getPostcode() {
	return postcode;
}


public String getGemeente() {
	return gemeente;
}


public String getGebruikersnaam() {
	return gebruikersnaam;
}


public String getPaswoord() {
	return paswoord;
}

public void setId(long id) {
	this.id = id;
}

public void setVoornaam(String voornaam) {
	this.voornaam = voornaam;
}

public void setFamilienaam(String familienaam) {
	this.familienaam = familienaam;
}

public void setStraat(String straat) {
	this.straat = straat;
}

public void setHuisnr(String huisnr) {
	this.huisnr = huisnr;
}

public void setPostcode(String postcode) {
	this.postcode = postcode;
}

public void setGemeente(String gemeente) {
	this.gemeente = gemeente;
}

public void setGebruikersnaam(String gebruikersnaam) {
	this.gebruikersnaam = gebruikersnaam;
}

public void setPaswoord(String paswoord) {
	this.paswoord = paswoord;
}

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((gebruikersnaam == null) ? 0 : gebruikersnaam.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Klant other = (Klant) obj;
	if (gebruikersnaam == null) {
		if (other.gebruikersnaam != null)
			return false;
	} else if (!gebruikersnaam.equals(other.gebruikersnaam))
		return false;
	return true;
}



}
