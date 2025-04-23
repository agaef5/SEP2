package server.persistence.racer;

import server.model.Horse;

import java.security.Key;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RacerRepositoryImpl implements RacerRepository
{
  private static RacerRepositoryImpl instance;
  
  public RacerRepositoryImpl () throws SQLException
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
  
  private Connection getConnection () throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=jdbc",
        "postgres", "1234");
  }
  
  @Override public Horse create ( String name ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO Horse (name) VALUES (?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setString(1, name);
      statement.executeUpdate();
      ResultSet keys = statement.getGeneratedKeys();
      if ( keys.next() )
      {
        //TODO: constructor in horse class is kinda wrong. public Horse (Int id, String name,int speedMin, int speedMax)
        return new Horse(keys.getInt(1), name, 1, 10);
      }
      else
      {
        throw new SQLException("No keys generated");
      }
    }
  }
  
  @Override public Horse readByID ( int id ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM Horse WHERE id = ?");
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if ( resultSet.next() )
      {
        String name = resultSet.getString("name");
        int speedMin = resultSet.getInt("speedMin"); // speed is int/float?
        int speedMax = resultSet.getInt("speedMax");
        return new Horse(id, name, speedMin, speedMax);
      }
      else
      {
        return null;
      }
    }
  }
  
  @Override public ArrayList<Horse> readAll () throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM Horse");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Horse> result = new ArrayList<>();
      while ( resultSet.next() )
      {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int speedMin = resultSet.getInt("speedMin");
        int speedMax = resultSet.getInt("speedMax");
        //        horseArrayList.add(new Horse(id, name, speedMin, speedMax));
        Horse horse = new Horse(id, name, speedMin, speedMax);
        result.add(horse);
      }
      return result;
    }
  }
  
  @Override public Horse readBySpeed_min ( int speedMin ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM Horse WHERE speedMin = ?");
      statement.setInt(1, speedMin);
      ResultSet resultSet = statement.executeQuery();
      if ( resultSet.next() )
      {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int speedMax = resultSet.getInt("speedMax");
        return new Horse(id, name, speedMin, speedMax);
      }
      else
      {
        return null;
      }
    }
  }
  
  @Override public Horse readBySpeed_max ( int speedMax ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM Horse WHERE speedMax = ?");
      statement.setInt(1, speedMax);
      ResultSet resultSet = statement.executeQuery();
      if ( resultSet.next() )
      {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int speedMin = resultSet.getInt("speedMin");
        return new Horse(id, name, speedMin, speedMax);
      }
      else
      {
        return null;
      }
    }
  }
  
  @Override public List<Horse> readByName ( String searchName )
      throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM Horse WHERE name LIKE ?");
      statement.setString(1, "%" + searchName + "%");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Horse> result = new ArrayList<>();
      while ( resultSet.next() )
      {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        int speedMin = resultSet.getInt("speedMin");
        int speedMax = resultSet.getInt("speedMax");
        Horse horse = new Horse(id, name, speedMin, speedMax);
        result.add(horse);
      }
      return result;
    }
  }
  
  @Override public void updateName ( Horse horse ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE Horse SET name = ? WHERE id = ?");
      statement.setString(1, horse.getName());
      statement.setInt(2, horse.getId());
      statement.executeUpdate();
    }
  }
  
  @Override public void updateSpeedMin ( Horse horse ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE Horse SET speedMin = ? WHERE id = >");
      statement.setFloat(1, horse.getSpeedMin());
      statement.setFloat(2, horse.getId());
      statement.executeUpdate();
    }
  }
  
  @Override public void updateSpeedMax ( Horse horse ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE Horse SET speedMax = ? WHERE id = ?");
      statement.setFloat(1, horse.getSpeedMax());
      statement.setFloat(2, horse.getId());
      statement.executeUpdate();
    }
    
  }
  
  @Override public void delete ( Horse horse ) throws SQLException
  {
    try ( Connection connection = getConnection() )
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM Horse WHERE id = ?");
      statement.setInt(1, horse.getId());
      statement.executeUpdate();
    }
    
  }
}
