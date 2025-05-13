package server.persistence.user;

import client.ui.util.ErrorHandler;
//import server.model.Admin;
import server.model.Balance;
import server.model.User;
import shared.DTO.UserDTO;

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

  private static final Lock lock = new ReentrantLock();
  private static UserRepositoryImpl instance;
  private ArrayList<User> users;

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
      synchronized (lock) {
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
  public User createUser(String username, String email, String password, boolean isAdmin) {
    try (Connection connection = getConnection()) {
      String query = "INSERT INTO game_user (username, password_hash, email, isAdmin, balance) VALUES (?, ?, ?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, username);
      statement.setString(2, password);
      statement.setString(3, email);
      statement.setBoolean(4, isAdmin);
      statement.setInt(5, 1000);

      int rowsInserted = statement.executeUpdate();

      if (rowsInserted != 1) {
        throw new SQLException("User insert failed");
      }

      // After successful insert, fetch the user back from DB
      return readByUsername(username);
    } catch (SQLException e) {
      ErrorHandler.handleError(e, "Cannot connect to database");
    }
    return null;
  }

  /**
   * Retrieves a single user by their email or username.
   * Searches both email and username if the provided string contains an "@" symbol.
   *
   * @param username the username or email of the user to retrieve
   * @return the {@link User} object matching the provided identifier, or {@code null} if not found
   */
  @Override
  public User readByUsername(String username) {
    try (Connection connection = getConnection()) {
      String query = "SELECT * FROM game_user  WHERE username = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultToUser(resultSet);
      } else {
        return null;
      }
    } catch (SQLException e) {
        ErrorHandler.handleError(e, "Error - issue with database");
    }
    return null;
  }

  @Override
  public User readByEmail(String email){
    try (Connection connection = getConnection()) {
      String query = "SELECT * FROM game_user WHERE email = ?";
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultToUser(resultSet);
      } else {
        return null;
      }
    } catch (SQLException e) {
      ErrorHandler.handleError(e, "Error - issue with database");
    }
    return null;
  }

  @Override
  public void updateBalance(String username, int newBalance) {
    User user = readByUsername(username);

    Balance balance = user.getBalance();
    balance.setAmount(newBalance);
  }

  @Override
  public int getBalance(String username) {
    User user = readByUsername(username);
    return user.getBalance().getAmount();
  }

  public UserDTO resultToDTO(ResultSet resultSet) throws SQLException {
    String dbsUsername = resultSet.getString("username");
    String dbsEmail = resultSet.getString("email");
    String dbsPassword = resultSet.getString("password_hash");
    boolean dbsIsAdmin = (resultSet.getByte("isAdmin")) == 1;
    int balance = resultSet.getInt("balance");

    return new UserDTO(dbsUsername, dbsEmail, dbsPassword, dbsIsAdmin, balance);
  }

  public User resultToUser(ResultSet resultSet) throws SQLException {
    String dbsUsername = resultSet.getString("username");
    String dbsEmail = resultSet.getString("email");
    String dbsPassword = resultSet.getString("password_hash");
    boolean dbsIsAdmin = resultSet.getBoolean("isAdmin");
    int balance = resultSet.getInt("balance");

    return new User(dbsUsername, dbsEmail, dbsPassword, dbsIsAdmin, balance);
  }

}

