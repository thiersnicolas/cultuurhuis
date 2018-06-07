package util;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

import be.vdab.entities.Voorstelling;

public class Util {

	public static Set<Voorstelling> enkelToekomstigeVoorstellingen(Set<Voorstelling> oudeSet){
		Set<Voorstelling> nieuweSet = new TreeSet<>();
		for (Voorstelling voorstelling:oudeSet) {
			if (voorstelling.getDatum().compareTo(LocalDateTime.now()) > 0){
				nieuweSet.add(voorstelling);
			}
		}
		return nieuweSet;
	}
	
}
