package server.persistence.horses;

import server.model.Horse;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorseRepositoryImpl implements HorseRepository
{
  private static HorseRepositoryImpl instance;

  private HorseRepositoryImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public synchronized HorseRepositoryImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new HorseRepositoryImpl();
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
        "postgres", "1234");
  }

  @Override public Horse create(String name, int speedmMin, int speedMax)
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query =
          "INSERT INTO " + " (name, speedMin, speedMax) VALUES (?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(query,
          PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setString(1, name);
      statement.setInt(2, speedmMin);
      statement.setInt(3, speedMax);
      statement.executeUpdate();
      ResultSet resultSet = statement.getGeneratedKeys();
      if (resultSet.next())
      {
        return createHorseObject(resultSet);
      }
      else
      {
        throw new SQLException("Failed to generate key for new racer");
      }
    }
    catch (SQLException e)
    {
      // Log the exception with additional context
      System.err.println(
          "Error executing create query for racerType:name: " + name);
      throw new SQLException("Error creating horse", e);
    }
  }

  public Horse createHorseObject(ResultSet resultSet) throws SQLException
  {
    int id = resultSet.getInt("id");
    String name = resultSet.getString("name");
    int speedMin = resultSet.getInt("speedMin");
    int speedMax = resultSet.getInt("speedMax");

    return new Horse(id, name, speedMin, speedMax);

  }

  @Override public Horse readByID(int id) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query = "SELECT * FROM " + " WHERE id = ?;";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        return createHorseObject(resultSet);
      }
      else
      {
        return null;
      }
    }
  }

  @Override public ArrayList<Horse> readAll() throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query = "SELECT * FROM horse";
      PreparedStatement statement = connection.prepareStatement(query);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Horse> result = new ArrayList<>();
      while (resultSet != null && resultSet.next())
      {
        Horse horse = createHorseObject(resultSet);
        result.add(horse);
      }
      return result;
    }
  }

  @Override public Horse readBySpeed_min(int speedMin) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query = "SELECT * FROM horse " + " WHERE speedMin = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, speedMin);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet != null && resultSet.next())
      {
        return createHorseObject(resultSet);
      }
      else
      {
        return null;
      }
    }
  }

  @Override public Horse readBySpeed_max(int speedMax) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query = "SELECT * FROM horse" + " WHERE speedMin = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, speedMax);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet != null && resultSet.next())
      {
        return createHorseObject(resultSet);
      }
      else
      {
        return null;
      }
    }
  }

  @Override public List<Horse> readByName(String searchName) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query = "SELECT * FROM horse " + " WHERE name LIKE ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, "%" + searchName + "%");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Horse> result = new ArrayList<>();
      while (resultSet != null && resultSet.next())
      {
        Horse horse = createHorseObject(resultSet);
        result.add(horse);
      }
      return result;
    }
  }

  @Override public void updateHorse(Horse horse) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query = "UPDATE " + horse.getClass().getName().toLowerCase()
          + " name = ?, speedMin = ?, speedMax = ? WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, horse.getName());
      statement.setInt(2, horse.getSpeedMin());
      statement.setInt(3, horse.getSpeedMax());
      statement.setInt(4, horse.getId());
      statement.executeUpdate();
    }
  }

  @Override public void delete(Horse horse) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      String query = "DELETE FROM " + horse.getClass().getName().toLowerCase()
          + "WHERE id = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, horse.getId());
      statement.executeUpdate();
    }
  }
}


