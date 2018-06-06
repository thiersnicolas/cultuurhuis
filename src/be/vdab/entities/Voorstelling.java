package be.vdab.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Voorstelling {
private long id;
private String titel;
private String uitvoerders;
private LocalDateTime datum;
private long genreid;
private BigDecimal prijs;
private long aantalVrijePlaatsen;

public Voorstelling() {}

/***
	vanuit db
*/
private Voorstelling(long id, String titel, String uitvoerders, LocalDateTime datum, long genreid, BigDecimal prijs,
		long aantalVrijePlaatsen) {
	this.id = id;
	this.titel = titel;
	this.uitvoerders = uitvoerders;
	this.datum = datum;
	this.genreid = genreid;
	this.prijs = prijs;
	this.aantalVrijePlaatsen = aantalVrijePlaatsen;
}

public static Voorstelling voorstellingUitDatabase(long id, String titel, String uitvoerders, LocalDateTime datum, long genreid, BigDecimal prijs,
		long aantalVrijePlaatsen) {
	return new Voorstelling(id, titel, uitvoerders, datum, genreid, prijs, aantalVrijePlaatsen);
}

/***
	nieuwe voorstelling
*/
private Voorstelling(String titel, String uitvoerders, LocalDateTime datum, long genreid, BigDecimal prijs,
		long aantalVrijePlaatsen) {
	this.titel = titel;
	this.uitvoerders = uitvoerders;
	this.datum = datum;
	this.genreid = genreid;
	this.prijs = prijs;
	this.aantalVrijePlaatsen = aantalVrijePlaatsen;
}

public static Voorstelling voorstellingNieuw(String titel, String uitvoerders, LocalDateTime datum, long genreid, BigDecimal prijs,
		long aantalVrijePlaatsen) {
	return new Voorstelling(titel, uitvoerders, datum, genreid, prijs, aantalVrijePlaatsen);
}

public long getId() {
	return id;
}

public String getTitel() {
	return titel;
}

public String getUitvoerders() {
	return uitvoerders;
}

public LocalDateTime getDatum() {
	return datum;
}

public long getGenreid() {
	return genreid;
}

public BigDecimal getPrijs() {
	return prijs;
}

public long getAantalVrijePlaatsen() {
	return aantalVrijePlaatsen;
}

public void setId(long id) {
	this.id = id;
}

public void setTitel(String titel) {
	this.titel = titel;
}

public void setUitvoerders(String uitvoerders) {
	this.uitvoerders = uitvoerders;
}

public void setDatum(LocalDateTime datum) {
	this.datum = datum;
}

public void setGenreid(long genreid) {
	this.genreid = genreid;
}

public void setPrijs(BigDecimal prijs) {
	this.prijs = prijs;
}

public void setAantalVrijePlaatsen(long aantalVrijePlaatsen) {
	this.aantalVrijePlaatsen = aantalVrijePlaatsen;
}

}
