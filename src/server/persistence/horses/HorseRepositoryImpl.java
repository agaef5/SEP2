package server.persistence.horses;

import server.model.Horse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link HorseRepository} interface for managing horse entities in the database.
 * Provides methods to create, read, update, and delete horse records.
 */
public class HorseRepositoryImpl implements HorseRepository {

  private static HorseRepositoryImpl instance;

  /**
   * Private constructor for initializing the repository. Registers the PostgreSQL driver.
   * @throws SQLException if an SQL error occurs while registering the driver.
   */
  private HorseRepositoryImpl() throws SQLException {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * Returns the singleton instance of {@link HorseRepositoryImpl}.
   * @return the singleton instance of the repository.
   * @throws SQLException if an SQL error occurs during initialization.
   */
  public static synchronized HorseRepositoryImpl getInstance() throws SQLException {
    if (instance == null) {
      instance = new HorseRepositoryImpl();
    }
    return instance;
  }

  /**
   * Establishes a connection to the PostgreSQL database.
   * @return a {@link Connection} object representing the connection to the database.
   * @throws SQLException if an error occurs while connecting to the database.
   */
  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
            "postgres", "1234");
  }

  /**
   * Creates a new horse record in the database.
   * @param name the name of the horse.
   * @param speedMin the minimum speed of the horse.
   * @param speedMax the maximum speed of the horse.
   * @return a {@link Horse} object representing the newly created horse.
   * @throws SQLException if an error occurs while creating the horse record.
   */
  @Override
  public Horse create(String name, int speedMin, int speedMax) throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "INSERT INTO horse (name, speedMin, speedMax) VALUES (?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setString(1, name);
      statement.setInt(2, speedMin);
      statement.setInt(3, speedMax);
      statement.executeUpdate();
      ResultSet resultSet = statement.getGeneratedKeys();
      if (resultSet.next()) {
        return createHorseObject(resultSet);
      } else {
        throw new SQLException("Failed to generate key for new horse");
      }
    } catch (SQLException e) {
      System.err.println("Error executing create query for horse:name: " + name);
      throw new SQLException("Error creating horse", e);
    }
  }

  /**
   * Creates a {@link Horse} object from a {@link ResultSet}.
   * @param resultSet the result set containing horse data from the database.
   * @return a {@link Horse} object representing the horse.
   * @throws SQLException if an error occurs while extracting data from the result set.
   */
  public Horse createHorseObject(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("id");
    String name = resultSet.getString("name");
    int speedMin = resultSet.getInt("speedMin");
    int speedMax = resultSet.getInt("speedMax");

    return new Horse(id, name, speedMin, speedMax);
  }

  /**
   * Reads a horse from the database by its ID.
   * @param id the ID of the horse to read.
   * @return a {@link Horse} object representing the horse, or null if no horse is found.
   * @throws SQLException if an error occurs while querying the database.
   */
  @Override
  public Horse readByID(int id) throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "SELECT * FROM horse WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return createHorseObject(resultSet);
      } else {
        return null;
      }
    }
  }

  /**
   * Reads all horses from the database.
   * @return a list of all {@link Horse} objects.
   * @throws SQLException if an error occurs while querying the database.
   */
  @Override
  public ArrayList<Horse> readAll() throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "SELECT * FROM horse";
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Horse> result = new ArrayList<>();
      while (resultSet != null && resultSet.next()) {
        Horse horse = createHorseObject(resultSet);
        result.add(horse);
      }
      return result;
    }
  }

  /**
   * Reads a horse from the database by its minimum speed.
   * @param speedMin the minimum speed of the horse to search for.
   * @return a {@link Horse} object representing the horse, or null if no horse is found.
   * @throws SQLException if an error occurs while querying the database.
   */
  @Override
  public Horse readBySpeed_min(int speedMin) throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "SELECT * FROM horse WHERE speedMin = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, speedMin);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet != null && resultSet.next()) {
        return createHorseObject(resultSet);
      } else {
        return null;
      }
    }
  }

  /**
   * Reads a horse from the database by its maximum speed.
   * @param speedMax the maximum speed of the horse to search for.
   * @return a {@link Horse} object representing the horse, or null if no horse is found.
   * @throws SQLException if an error occurs while querying the database.
   */
  @Override
  public Horse readBySpeed_max(int speedMax) throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "SELECT * FROM horse WHERE speedMax = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, speedMax);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet != null && resultSet.next()) {
        return createHorseObject(resultSet);
      } else {
        return null;
      }
    }
  }

  /**
   * Reads horses from the database by their name.
   * @param searchName the name of the horse to search for.
   * @return a list of {@link Horse} objects that match the name search.
   * @throws SQLException if an error occurs while querying the database.
   */
  @Override
  public List<Horse> readByName(String searchName) throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "SELECT * FROM horse WHERE name LIKE ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, "%" + searchName + "%");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Horse> result = new ArrayList<>();
      while (resultSet != null && resultSet.next()) {
        Horse horse = createHorseObject(resultSet);
        result.add(horse);
      }
      return result;
    }
  }

  /**
   * Updates a horse record in the database.
   * @param horse the {@link Horse} object with updated data.
   * @throws SQLException if an error occurs while updating the horse record.
   */
  @Override
  public void updateHorse(Horse horse) throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "UPDATE horse SET name = ?, speedMin = ?, speedMax = ? WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, horse.getName());
      statement.setInt(2, horse.getSpeedMin());
      statement.setInt(3, horse.getSpeedMax());
      statement.setInt(4, horse.getId());
      statement.executeUpdate();
    }
  }

  /**
   * Deletes a horse record from the database.
   * @param horse the {@link Horse} object to delete.
   * @throws SQLException if an error occurs while deleting the horse record.
   */
  @Override
  public void delete(Horse horse) throws SQLException {
    try (Connection connection = getConnection()) {
      String query = "DELETE FROM horse WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, horse.getId());
      statement.executeUpdate();
    }
  }
}
