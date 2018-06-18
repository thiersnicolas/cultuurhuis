package be.vdab.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.IntStream;

import be.vdab.entities.Genre;
import be.vdab.entities.Klant;
import be.vdab.entities.Voorstelling;

public class CultuurhuisRepository extends AbstractRepository {

	private static final String SELECT_GENRES = "select id, naam from genres";

	private static final String SELECT_VOORSTELLINGEN_GENRE = "select voorstellingen.id as 'id', "
			+ "titel, uitvoerders, datum, genreid, prijs, vrijeplaatsen "
			+ "from voorstellingen inner join genres on voorstellingen.genreid = genres.id where genres.id=?";

	private static final String INSERT_RESERVATIE = "insert into reservaties (klantid, voorstellingsid, plaatsen) "
			+ "select klanten.id,?,? from klanten " + "where (klanten.gebruikersnaam=?)";
	private static final String UPDATE_VOORSTELLINGEN_VRIJEPLAATSEN_VOORWAARDE = "update voorstellingen "
			+ "set vrijeplaatsen = vrijeplaatsen-? where ( vrijeplaatsen>=? and id=?)";

	private static final String SELECT_KLANTEN_GEBRUIKERSNAAM = "select id, voornaam, familienaam, straat, "
			+ "huisnr, postcode, gemeente, gebruikersnaam, paswoord from klanten where gebruikersnaam=?";
	private static final String INSERT_KLANT = "insert into klanten "
			+ "(voornaam, familienaam, straat, huisnr, postcode, gemeente, gebruikersnaam, paswoord) "
			+ "values (?,?,?,?,?,?,?,?)";

	private static final String SELECT_VOORSTELLING_ID = "select id, titel, uitvoerders, datum, genreid, prijs, vrijeplaatsen"
			+ " from voorstellingen where id=?";

	/*
	 * private static final String
	 * INSERT_RESERVATIE_IF_VRIJEPLAATSEN_GROTER_DAN_PLAATSEN =
	 * "insert into reservaties (klantid, voorstellingsid, plaatsen)" +
	 * " select klanten.id, 2, 10" + " from klanten" +
	 * " where (klanten.gebruikersnaam='hans') and " +
	 * "((select voorstellingen.vrijeplaatsen from voorstellingen where voorstellingen.id=2)>10)"
	 * ;
	 */

	public Set<Genre> getGenres() {
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			Set<Genre> lijstGenres = new TreeSet<>();
			try (ResultSet resultSet = statement.executeQuery(SELECT_GENRES)) {
				while (resultSet.next()) {
					lijstGenres.add(resultSetRijNaarGenre(resultSet));
				}
			}
			connection.commit();
			return lijstGenres;
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	private Genre resultSetRijNaarGenre(ResultSet resultSet) throws SQLException {
		return Genre.genreUitDatabase(resultSet.getLong("id"), resultSet.getString("naam"));
	}

	public Set<Voorstelling> getVoorstellingenGenre(long idGenre) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_VOORSTELLINGEN_GENRE)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			statement.setLong(1, idGenre);
			Set<Voorstelling> lijstVoorstellingen = new TreeSet<>();
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					lijstVoorstellingen.add(resultSetRijNaarVoorstelling(resultSet));
				}
			}
			connection.commit();
			return lijstVoorstellingen;

		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	private Voorstelling resultSetRijNaarVoorstelling(ResultSet resultSet) throws SQLException {
		return Voorstelling.voorstellingUitDatabase(resultSet.getLong("id"), resultSet.getString("titel"),
				resultSet.getString("uitvoerders"), resultSet.getTimestamp("datum").toLocalDateTime(),
				resultSet.getLong("genreid"), resultSet.getBigDecimal("prijs"), resultSet.getLong("vrijeplaatsen"));
	}

	public Map<Long, Boolean> reservatiesBoeken(Map<Long, Long> mandje, String gebruikersnaam) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(UPDATE_VOORSTELLINGEN_VRIJEPLAATSEN_VOORWAARDE)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			for (Long voorstellingsId : mandje.keySet()) {
				statement.setLong(1, mandje.get(voorstellingsId));
				statement.setLong(2, mandje.get(voorstellingsId));
				statement.setLong(3, voorstellingsId);
				statement.addBatch();
			}
			Map<Long, Boolean> reservatiesGeboektMap = new TreeMap<>();
			int[] reservatiesGeboektArray = statement.executeBatch();
			if (IntStream.of(reservatiesGeboektArray).anyMatch(x->x==1)) {
				try (PreparedStatement statement2 = connection.prepareStatement(INSERT_RESERVATIE)) {
					int i = 0;
					for (Long voorstellingsId : mandje.keySet()) {
						if (reservatiesGeboektArray[i] == 1) {
							statement2.setLong(1, voorstellingsId);
							statement2.setLong(2, mandje.get(voorstellingsId));
							statement2.setString(3, gebruikersnaam);
							statement2.addBatch();
							reservatiesGeboektMap.put(voorstellingsId, true);
						} else {
							reservatiesGeboektMap.put(voorstellingsId, false);
						}
						i++;
						statement2.executeBatch();
					}
				}
			}
			connection.commit();
			return reservatiesGeboektMap;
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	public Klant zoekKlant(String gebruikersnaam) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_KLANTEN_GEBRUIKERSNAAM)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			statement.setString(1, gebruikersnaam);
			Klant klant = new Klant();
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					klant = resultSetRijNaarKlant(resultSet);
				}
			}
			connection.commit();
			return klant;

		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	private Klant resultSetRijNaarKlant(ResultSet resultSet) throws SQLException {
		return Klant.klantUitDatabase(resultSet.getLong("id"), resultSet.getString("voornaam"),
				resultSet.getString("familienaam"), resultSet.getString("straat"), resultSet.getString("huisnr"),
				resultSet.getString("postcode"), resultSet.getString("gemeente"), resultSet.getString("gebruikersnaam"),
				resultSet.getString("paswoord"));
	}

	public boolean klantToevoegenDatabase(Klant klant) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_KLANTEN_GEBRUIKERSNAAM)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement.setString(1, klant.getGebruikersnaam());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					connection.commit();
					return false;
				} else {
					try (PreparedStatement statement2 = connection.prepareStatement(INSERT_KLANT)) {
						statement2.setString(1, klant.getVoornaam());
						statement2.setString(2, klant.getFamilienaam());
						statement2.setString(3, klant.getStraat());
						statement2.setString(4, klant.getHuisnr());
						statement2.setString(5, klant.getPostcode());
						statement2.setString(6, klant.getGemeente());
						statement2.setString(7, klant.getGebruikersnaam());
						statement2.setString(8, klant.getPaswoord());
						statement2.execute();
						connection.commit();
						return true;
					}
				}
			}
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	public Voorstelling getVoorstelling(Long id) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_VOORSTELLING_ID)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			Voorstelling voorstelling = new Voorstelling();
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					voorstelling = resultSetRijNaarVoorstelling(resultSet);
				}
			}
			connection.commit();
			return voorstelling;
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

	public Set<Voorstelling> getVoorstellingen(Set<Long> ids) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_VOORSTELLING_ID)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			Set<Voorstelling> voorstellingSet = new LinkedHashSet<>();
			for (Long id : ids) {
				statement.setLong(1, id);
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						voorstellingSet.add(resultSetRijNaarVoorstelling(resultSet));

					}
				}

			}
			connection.commit();
			return voorstellingSet;
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

}
