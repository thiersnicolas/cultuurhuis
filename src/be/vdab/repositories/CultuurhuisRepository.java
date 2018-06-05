package be.vdab.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import be.vdab.entities.Reservatie;
import be.vdab.entities.Voorstelling;

public class CultuurhuisRepository extends AbstractRepository {

	private static final String SELECT_VOORSTELLINGEN_GENRE = "select voorstellingen.id as 'id', "
			+ "titel, datum, genreid, prijs, vrijeplaatsen "
			+ "from voorstellingen inner join genres voorstellingen.genreid = genres.id where genres.naam=?";

	// private static final String SELECT_VOORSTELLINGEN_VRIJEPLAATSEN = "select
	// vrijeplaatsen from voorstellingen where id=?";
	private static final String INSERT_RESERVATIE = "insert into reservaties (klantid, voorstellingsid, plaatsen) "
			+ "values (?,?,?)";
	private static final String UPDATE_VOORSTELLINGEN_VRIJEPLAATSEN_VOORWAARDE = "update voorstellingen "
			+ "set vrijeplaatsen = vrijeplaatsen-? where ( vrijeplaatsen>=? and id=?)";

	private static final String SELECT_KLANTEN_GEBRUIKERSNAAM = "select * from klanten " + "where gebruikersnaam=?";
	private static final String INSERT_KLANT = "insert into klanten "
			+ "(voornaam, familienaam, straat, huisnr, postcode, gemeente, gebruikersnaam, paswoord) "
			+ "values (?,?,?,?,?,?,?,?)";

	public List<Voorstelling> getVoorstellingenGenre(String naamGenre) {
		try (Connection connection = dataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement(SELECT_VOORSTELLINGEN_GENRE)) {
			connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			connection.setAutoCommit(false);
			statement.setString(1, naamGenre);
			List<Voorstelling> lijstVoorstellingen = new ArrayList<>();
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
					Arrays.stream(statement.executeBatch()).boxed().collect(Collectors.toList()));
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

			return reservatiesGeboektLijst;
		} catch (SQLException ex) {
			throw new RepositoryException(ex);
		}
	}

}
