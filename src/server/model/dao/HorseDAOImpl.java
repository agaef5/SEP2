package server.model.dao;

import server.model.Horse;

import java.sql.*;
import java.util.List;

public class HorseDAOImpl implements HorseDAO
{
  private static HorseDAOImpl instance;
  
  public HorseDAOImpl () throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }
  
  public static synchronized HorseDAOImpl getInstance () throws SQLException
  {
    if ( instance == null )
    {
      instance = new HorseDAOImpl();
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
        return new Horse(keys.getInt(1), name) // constructor in horse class is kinda wrong. public Horse (Int id, String name,int speedMin, int speedMax)
      }else {
        throw new SQLException("No keys generated");
      }
    }
  }
  
  @Override public Horse readByID ( int id ) throws SQLException
  {
    try(Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM Horse WHERE id = ?");
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if ( resultSet.next() ){
        String name = resultSet.getString("name");
        int speedMin = resultSet.getInt("speedMin"); // speed is int/float?
        int speedMax = resultSet.getInt("speedMax");
        return new Horse(name, speedMin, speedMax);
      } else {
        return null;
      }
    }
  }
  
  @Override public Horse readBySpeed_min ( float speedMin ) throws SQLException
  {
  
  }
  
  @Override public Horse readBySpeed_max ( float speedMax ) throws SQLException
  {
    return null;
  }
  
  @Override public List<Horse> readByName ( String searchName )
      throws SQLException
  {
    return List.of();
  }
  
  @Override public void updateName ( Horse horse ) throws SQLException
  {
    try(Connection connection = getConnection()){
      PreparedStatement statement = connection.prepareStatement("UPDATE Horse SET name = ? WHERE id = ?");
      statement.setString(1, horse.getName());
      statement.setInt(2, horse.getId());
      statement.executeUpdate();
    }
  }
  
  @Override public void updateSpeedMin ( Horse horse ) throws SQLException
  {
    try(Connection connection = getConnection()){
      PreparedStatement statement = connection.prepareStatement("UPDATE Horse SET speedMin = ? WHERE id = >");
      statement.setFloat(1, horse.getSpeedMin());
      statement.setFloat(2, horse.getSpeedMin());
      statement.executeUpdate();
    }
  }
  
  @Override public void updateSpeedMax ( Horse horse ) throws SQLException
  {
    try(Connection connection = getConnection()){
      PreparedStatement statement = connection.prepareStatement("UPDATE Horse SET speedMax = ? WHERE id = ?");
      statement.setFloat(1, horse.getSpeedMax());
      statement.setFloat(2, horse.getSpeedMax());
      statement.executeUpdate();
    }
    
  }
  
  @Override public void delete ( Horse horse ) throws SQLException
  {
    try(Connection connection = getConnection()){
    PreparedStatement statement = connection.prepareStatement("DELETE FROM Horse WHERE id = ?");
    statement.setInt(1, horse.getId());
    statement.executeUpdate();
    }
  
  }
}
