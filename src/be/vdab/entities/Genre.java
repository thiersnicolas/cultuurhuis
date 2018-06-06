package be.vdab.entities;

public class Genre {
private long id;
private String naam;


public Genre() {}

/***
	vanuit db
 */
private Genre(long id, String naam) {
	this.id = id;
	this.naam = naam;
}

public static Genre genreUitDatabase(long id, String naam) {
	return new Genre(id, naam);
}

/***
	nieuw genre
*/
private Genre(String naam) {
	this.naam = naam;
}

public static Genre genreNieuw(String naam) {
	return new Genre(naam);
}

public long getId() {
	return id;
}

public String getNaam() {
	return naam;
}

public void setId(long id) {
	this.id = id;
}

public void setNaam(String naam) {
	this.naam = naam;
}

}
