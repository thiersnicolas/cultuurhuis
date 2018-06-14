package be.vdab.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import be.vdab.entities.Genre;
import be.vdab.entities.Klant;
import be.vdab.entities.Reservatie;
import be.vdab.entities.Voorstelling;

public class CultuurhuisRepository extends AbstractRepository {
	
	private static final String SELECT_GENRES = "select id, naam from genres";

	private static final String SELECT_VOORSTELLINGEN_GENRE = "select voorstellingen.id as 'id', "
			+ "titel, uitvoerders, datum, genreid, prijs, vrijeplaatsen "
			+ "from voorstellingen inner join genres on voorstellingen.genreid = genres.id where genres.id=?";

	private static final String INSERT_RESERVATIE = "insert into reservaties (klantid, voorstellingsid, plaatsen) "
			+ "values (?,?,?)";
	private static final String UPDATE_VOORSTELLINGEN_VRIJEPLAATSEN_VOORWAARDE = "update voorstellingen "
			+ "set vrijeplaatsen = vrijeplaatsen-? where ( vrijeplaatsen>=? and id=?)";

	private static final String SELECT_KLANTEN_GEBRUIKERSNAAM = "select id, voornaam, familienaam, straat, "
			+ "huinr, postcode, gemeente, gebruikersnaam, paswoord from klanten where gebruikersnaam=?";
	private static final String INSERT_KLANT = "insert into klanten "
			+ "(voornaam, familienaam, straat, huisnr, postcode, gemeente, gebruikersnaam, paswoord) "
			+ "values (?,?,?,?,?,?,?,?)";
	
	private static final String SELECT_VOORSTELLING_ID = "select id, titel, uitvoerders, datum, genreid, prijs, vrijeplaatsen"
			+ " from voorstellingen where id=?";
	
	public Set<Genre> getGenres(){
		try(Connection connection = dataSource.getConnection();
				Statement statement = connection.createStatement()){
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			Set<Genre> lijstGenres = new TreeSet<>();
			try(ResultSet resultSet = statement.executeQuery(SELECT_GENRES)){
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
	
	private Genre resultSetRijNaarGenre(ResultSet resultSet) throws SQLException{
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
		return Voorstelling.voorstellingUitDatabase(
				resultSet.getLong("id"), 
				resultSet.getString("titel"),
				resultSet.getString("uitvoerders"), 
				resultSet.getTimestamp("datum").toLocalDateTime(),
				resultSet.getLong("genreid"), 
				resultSet.getBigDecimal("prijs"), 
				resultSet.getLong("vrijeplaatsen"));
	}

	public List<Integer> reservatiesBoeken(ArrayList<Reservatie> reservatieLijst) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection
						.prepareStatement(UPDATE_VOORSTELLINGEN_VRIJEPLAATSEN_VOORWAARDE)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			for (Reservatie reservatie : reservatieLijst) {
				statement.setLong(1, reservatie.getAantalPlaatsen());
				statement.setLong(2, reservatie.getAantalPlaatsen());
				statement.setLong(3, reservatie.getId());
				statement.addBatch();
			}
			List<Integer> reservatiesGeboektLijst = new ArrayList<Integer>(
					Arrays.stream(statement.executeBatch())
							.boxed()
							.collect(Collectors.toList()));
			if (reservatiesGeboektLijst.contains((Integer) 1)) {
				try (PreparedStatement statement2 = connection.prepareStatement(INSERT_RESERVATIE)) {
					for (Integer geboekt : reservatiesGeboektLijst) {
						int i = 0;
						if (geboekt == (Integer) 1) {
							statement2.setLong(1, reservatieLijst.get(i).getKlantid());
							statement2.setLong(2, reservatieLijst.get(i).getVoorstellingsid());
							statement2.setLong(3, reservatieLijst.get(i).getAantalPlaatsen());
							statement2.addBatch();
						}
						i++;
						statement2.executeBatch();
					}
				}
			}
			connection.commit();
			return reservatiesGeboektLijst;
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}
	
	public Klant zoekKlant(String gebruikersnaam) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_KLANTEN_GEBRUIKERSNAAM)){
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			statement.setString(1, gebruikersnaam);
			Klant klant = new Klant();
			try (ResultSet resultSet = statement.executeQuery()){
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
		return Klant.klantUitDatabase(
				resultSet.getLong("id"), 
				resultSet.getString("voornaam"),
				resultSet.getString("familienaam"),
				resultSet.getString("straat"), 
				resultSet.getString("huisnr"), 
				resultSet.getString("postcode"), 
				resultSet.getString("gemeente"), 
				resultSet.getString("gebruikersnaam"), 
				resultSet.getString("paswoord"));
	}

	public boolean klantToevoegenDatabase(Klant klant) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_KLANTEN_GEBRUIKERSNAAM)){
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			connection.setAutoCommit(false);
			statement.setString(1, klant.getGebruikersnaam());
			try (ResultSet resultSet = statement.executeQuery()){
				if (resultSet.next()) {
					connection.commit();
					return false;
				} else {
					try(PreparedStatement statement2 = connection.prepareStatement(INSERT_KLANT)){
						statement2.setString(1, klant.getVoornaam());
						statement2.setString(2, klant.getFamilienaam());
						statement2.setString(3, klant.getStraat());
						statement2.setString(4, klant.getHuisnr());
						statement2.setString(5, klant.getPostcode());
						statement2.setString(6, klant.getGemeente());
						statement2.setString(7, klant.getGebruikersnaam());
						statement2.setString(8, klant.getPaswoord());
						statement2.executeQuery();
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
		try(Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_VOORSTELLING_ID)){
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			Voorstelling voorstelling = new Voorstelling();
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery()){
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
		try(Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_VOORSTELLING_ID)){
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			Set<Voorstelling> voorstellingSet = new LinkedHashSet<>();
			for (Long id:ids) {
				statement.setLong(1, id);
				try (ResultSet resultSet = statement.executeQuery()){
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
