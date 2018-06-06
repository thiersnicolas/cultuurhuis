package be.vdab.entities;

public class Reservatie {
private long id;
private long klantid;
private long voorstellingsid;
private long aantalPlaatsen;

public Reservatie() {}

/***
	vanuit db
*/
private Reservatie(long id, long klantid, long voorstellingsid, long aantalPlaatsen) {
	this.id = id;
	this.klantid = klantid;
	this.voorstellingsid = voorstellingsid;
	this.aantalPlaatsen = aantalPlaatsen;
}

public Reservatie reservatieUitDatabase(long id, long klantid, long voorsellingsid, long aantalPlaatsen) {
	return new Reservatie(id, klantid, voorstellingsid, aantalPlaatsen);
}

/***
	nieuwe reservatie
*/
private Reservatie(long klantid, long voorsellingsid, long aantalPlaatsen) {
	this.klantid = klantid;
	this.voorstellingsid = voorsellingsid;
	this.aantalPlaatsen = aantalPlaatsen;
}

public Reservatie reservatieNieuw(long klantid, long voorstellingsid, long aantalPlaatsen) {
	return new Reservatie(klantid, voorstellingsid, aantalPlaatsen);
}

public long getId() {
	return id;
}

public long getKlantid() {
	return klantid;
}

public long getVoorstellingsid() {
	return voorstellingsid;
}

public long getAantalPlaatsen() {
	return aantalPlaatsen;
}

public void setId(long id) {
	this.id = id;
}

public void setKlantid(long klantid) {
	this.klantid = klantid;
}

public void setVoorstellingsid(long voorstellingsid) {
	this.voorstellingsid = voorstellingsid;
}

public void setAantalPlaatsen(long aantalPlaatsen) {
	this.aantalPlaatsen = aantalPlaatsen;
}

}
