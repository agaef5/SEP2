package server.persistence.user;

import client.ui.util.ErrorHandler;
import server.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@link UserRepositoryImpl} class provides an implementation of the {@link UserRepository} interface
 * for managing {@link User} objects. It simulates a user repository using an in-memory list.
 * In the future, this can be replaced with actual database interaction.
 *
 * This implementation provides methods for adding, retrieving, saving, and deleting users. It also
 * supports pagination for retrieving users in batches and allows for searching users by their username or email.
 */
public class UserRepositoryImpl implements UserRepository {

  private static UserRepositoryImpl instance;

  /**
   * Private constructor initializes the in-memory list of users.
   * Users are added as sample data for testing purposes.
   */
  private UserRepositoryImpl() throws SQLException {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * Retrieves the singleton instance of the {@link UserRepositoryImpl}.
   * Ensures thread-safety using double-checked locking.
   *
   * @return the singleton instance of the {@link UserRepositoryImpl}
   */
  public static UserRepository getInstance() {
    if (instance == null) {
      synchronized (UserRepositoryImpl.class) {
        if (instance == null) {
          try{
            instance = new UserRepositoryImpl();
          } catch (SQLException e) {
            ErrorHandler.handleError(e, "Issue with connecting to database");
          }
        }
      }
    }
    return instance;
  }

  /**
   * Establishes a connection to the PostgreSQL database.
   *
   * @return a {@link Connection} object to interact with the database
   */
  private Connection getConnection() throws SQLException {
      return DriverManager.getConnection(
              "jdbc:postgresql://localhost:5432/postgres?currentSchema=sep2",
              "postgres", "1234");
  }

  /**
   * Adds a new user to the repository.
   *
   * @param username the {@link User} object to be added
   */
  @Override
  public User createUser(String username, String email, String password, boolean isAdmin) throws SQLException{
    try (Connection connection = getConnection()) {
//      Create query and change it with parameters
      String query = "INSERT INTO Game_user (username, password_hash, email, isAdmin, balance) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(query);

      statement.setString(1, username);
      statement.setString(2, password);
      statement.setString(3, email);
      statement.setBoolean(4, isAdmin);
      statement.setInt(5, 1000);

//      Execute prepared query
      int rowsInserted = statement.executeUpdate();

//      Check if execution is successful
      if (rowsInserted != 1) {
        throw new SQLException("User insert failed");
      }

      // After successful insert, fetch the user back from DB
      return readByUsername(username);
    }
  }

  /**
   * Retrieves a single user by their or username.
   *
   * @param username the username of the user to retrieve
   * @return the {@link User} object matching the provided identifier, or {@code null} if not found
   */
  @Override
  public User readByUsername(String username) throws SQLException{
    //    Connect to database
    try (Connection connection = getConnection()) {

//      Prepare query
      String query = "SELECT * FROM Game_user  WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, username);

//      Execute query
      ResultSet resultSet = statement.executeQuery();

//      Return User object if execution is successful
      if (resultSet.next()) {
        return resultToUser(resultSet);
      } else {
        return null;
      }
    }
  }


  /**
   * Retrieves a single user by their email.
   * Searches email if the provided string contains an "@" symbol.
   *
   * @param email the username of the user to retrieve
   * @return the {@link User} object matching the provided identifier, or {@code null} if not found
   */
  @Override
  public User readByEmail(String email) throws SQLException{
//    Connect to database
    try (Connection connection = getConnection()) {

//      Prepare query
      String query = "SELECT * FROM Game_user WHERE email = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, email);

//      Execute query
      ResultSet resultSet = statement.executeQuery();

//      Return User object if execution is successful
      if (resultSet.next()) {
        return resultToUser(resultSet);
      } else {
        return null;
      }
    }
  }

  /**
   * Updates user balance.
   *
   * @param username - user whose balance has to be updated
   * @param newBalance - new balance worth
   * @throws SQLException - exception thrown, if the operation is not successful
   */
  @Override
  public void updateBalance(String username, int newBalance) throws SQLException{
//    Connect to database
    try (Connection connection = getConnection()) {

//      Prepare query
      String query = "UPDATE game_user SET balance = ? WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setInt(1, newBalance);
      statement.setString(2, username);

//      Execute query
      int rowsUpdated = statement.executeUpdate();

//      If update failed, throw an error
      if (rowsUpdated != 1) {
        throw new SQLException("Update failed");
      }

    }
  }

  /**
   * Method that creates {@link User} object from the
   * ResultSet received from database after executing a query
   *
   * @param resultSet - result set received from database
   * @return {@link User} object
   * @throws SQLException - error thrown, if the result set doesn't contain needed data
   */
  private User resultToUser(ResultSet resultSet) throws SQLException {
    String dbsUsername = resultSet.getString("username");
    String dbsEmail = resultSet.getString("email");
    String dbsPassword = resultSet.getString("password_hash");
    boolean dbsIsAdmin = resultSet.getBoolean("isAdmin");
    int balance = resultSet.getInt("balance");

    return new User(dbsUsername, dbsEmail, dbsPassword, dbsIsAdmin, balance);
  }

}

