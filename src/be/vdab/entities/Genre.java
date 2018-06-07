package be.vdab.entities;

import java.lang.Comparable;

public class Genre implements Comparable<Genre> {
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

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((naam == null) ? 0 : naam.hashCode());
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
	Genre other = (Genre) obj;
	if (naam == null) {
		if (other.naam != null)
			return false;
	} else if (!naam.equals(other.naam))
		return false;
	return true;
}

@Override
public int compareTo(Genre g) {
// sorteren op naam: consistent met equals()
	if (this.equals(g)) {
		return 0;
	} else {
		return this.naam.compareToIgnoreCase(g.getNaam());
	}
}

}
