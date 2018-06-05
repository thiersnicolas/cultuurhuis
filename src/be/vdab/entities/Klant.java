package be.vdab.entities;

public class Klant {
	
private long id;
private String voornaam;
private String straat;
private String huisnr;
private String postcode;
private String gemeente;
private String gebruikersnaam;
private String paswoord;


/***
	vanuit db
*/
private Klant(long id, String voornaam, String straat, String huisnr, String postcode, String gemeente,
		String gebruikersnaam, String paswoord) {
	this.id = id;
	this.voornaam = voornaam;
	this.straat = straat;
	this.huisnr = huisnr;
	this.postcode = postcode;
	this.gemeente = gemeente;
	this.gebruikersnaam = gebruikersnaam;
	this.paswoord = paswoord;
}

public Klant klantUitDatabase(long id, String voornaam, String straat, String huisnr, String postcode, String gemeente,
		String gebruikersnaam, String paswoord) {
		return new Klant(id, voornaam, straat, huisnr, postcode, gemeente,
				gebruikersnaam, paswoord);
}

/**
	nieuwe klant
 */
private Klant(String voornaam, String straat, String huisnr, String postcode, String gemeente, String gebruikersnaam,
		String paswoord) {
	this.voornaam = voornaam;
	this.straat = straat;
	this.huisnr = huisnr;
	this.postcode = postcode;
	this.gemeente = gemeente;
	this.gebruikersnaam = gebruikersnaam;
	this.paswoord = paswoord;
}

public Klant klantNieuw(String voornaam, String straat, String huisnr, String postcode, String gemeente, String gebruikersnaam,
		String paswoord) {
	return new Klant(voornaam, straat, huisnr, postcode, gemeente,
				gebruikersnaam, paswoord);
}

public long getId() {
	return id;
}

public String getVoornaam() {
	return voornaam;
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

}
