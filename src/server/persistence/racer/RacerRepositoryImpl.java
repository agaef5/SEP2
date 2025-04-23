package server.persistence.racer;

import server.model.Horse;
import server.model.Racer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RacerRepositoryImpl implements RacerRepository
{
  private static RacerRepositoryImpl instance;
  
  public RacerRepositoryImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }
  
  public static synchronized RacerRepositoryImpl getInstance ()
      throws SQLException
  {
    if ( instance == null )
    {
      instance = new RacerRepositoryImpl();
    }
    return instance;
  }
  
  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=jdbc",
        "postgres", "1234");
  }
  
  @Override public void create(String racerType, String name, int speedmMin, int speedMax) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          //      TODO: fix this, to insert all data, not only the name
          "INSERT INTO ? (name) VALUES (?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setString(1, racerType);
      statement.setString(2, name);
      statement.executeUpdate();
    }
  }

  public Racer createRacerObject(String racerType, ResultSet resultSet)
      throws SQLException
  {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int speedMin = resultSet.getInt("speedMin");
        int speedMax = resultSet.getInt("speedMax");

    switch (racerType) {
      case "horse" ->
      {
        return new Horse(id, name, speedMin, speedMax);
      }
      default -> {
        return null;
      }
    }
  }
  
  @Override public Racer readByID (String racerType, int id ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM ? WHERE id = ?");
      statement.setString(1, racerType.toLowerCase());
      statement.setInt(2, id);
      ResultSet resultSet = statement.executeQuery();
      if ( resultSet.next() )
      {
        return createRacerObject(racerType, resultSet);
      }
      else
      {
        return null;
      }
    }
  }
  
  @Override public ArrayList<Racer> readAll(String racerType) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM ?");
      statement.setString(1, racerType.toLowerCase());
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Racer> result = new ArrayList<>();
      while (resultSet.next())
      {
        Racer racer = createRacerObject(racerType, resultSet);
        result.add(racer);
      }
      return result;
    }
  }
  
  @Override public Racer readBySpeed_min (String racerType, int speedMin ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM ? WHERE speedMin = ?");
      statement.setString(1, racerType.toLowerCase());
      statement.setInt(2, speedMin);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        return createRacerObject(racerType, resultSet);
      }
      else
      {
        return null;
      }
    }
  }
  
  @Override public Racer readBySpeed_max (String racerType, int speedMax ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM ? WHERE speedMax = ?");
      statement.setString(1, racerType.toLowerCase());
      statement.setInt(2, speedMax);
      ResultSet resultSet = statement.executeQuery();
      if ( resultSet.next() )
      {
        return createRacerObject(racerType, resultSet);
      }
      else
      {
        return null;
      }
    }
  }
  
  @Override public List<Racer> readByName(String racerType, String searchName)
      throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM ? WHERE name LIKE ?");
      statement.setString(1, racerType);
      statement.setString(2, "%" + searchName + "%");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Racer> result = new ArrayList<>();
      while ( resultSet.next() )
      {
        Racer racer = createRacerObject(racerType , resultSet);
        result.add(racer);
      }
      return result;
    }
  }
  
  @Override public void updateRacer(Racer racer) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE ? SET name = ?, speedMin = ?, speedMax = ? WHERE id = ?");
      statement.setString(1, racer.getClass().getName().toLowerCase());
      statement.setString(2, racer.getName());
      statement.setInt(3, racer.getSpeedMin());
      statement.setInt(2, racer.getSpeedMax());
      statement.executeUpdate();
    }
  }
  
  @Override public void delete ( Racer racer) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM ? WHERE id = ?");
      statement.setString(1, racer.getClass().getName().toLowerCase());
      statement.setInt(2, racer.getId());
      statement.executeUpdate();
    }
    
  }
}
