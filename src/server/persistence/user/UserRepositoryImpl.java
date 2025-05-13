package server.persistence.user;

import client.ui.util.ErrorHandler;
//import server.model.Admin;
import server.model.Player;
import server.model.Race;
import server.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
   * @throws SQLException if a database access error occurs
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
      String query = "INSERT INTO user (username, password_hash, email, role_id) VALUES (?, ?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(
              query,
              PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setString(1, username);
      statement.setString(2, password);
      statement.setString(3, email);
      statement.setInt(4, isAdmin ? 1 : 0);
      statement.executeUpdate();
      ResultSet resultSet = statement.getGeneratedKeys();

      if (!resultSet.next()) {
        throw new SQLException("No keys generated");
      }

      String dbsUsername = resultSet.getString("username");
      String dbsEmail = resultSet.getString("email");
      String dbsPassword = resultSet.getString("password_hash");
      Boolean dbsIsAdmin = resultSet.getInt(resultSet.getByte("isAdmin")) == 1;

      if(isAdmin){
        return new User(dbsUsername, dbsEmail, dbsPassword, dbsIsAdmin);
      }

    }catch (SQLException sqlException){
      ErrorHandler.handleError(sqlException, "Error connecting to database");
    }
    return null;
  }

//  public Player createPlayer(String username, String email, String password){
//    try (Connection connection = getConnection()) {
//    String query = "INSERT INTO player (username, balance) VALUES (?, ?)";
//    PreparedStatement statement = connection.prepareStatement(
//            query,
//            PreparedStatement.RETURN_GENERATED_KEYS);
//    statement.setString(1, username);
//    statement.setInt(2, 1000);
//    statement.executeUpdate();
//    ResultSet resultSet = statement.getGeneratedKeys();
//
//      if (!resultSet.next()) {
//        throw new SQLException("No keys generated");
//      }
//      return new Player(resultSet.getString("username"),
//              resultSet.getString("email"),
//              resultSet.getString("password_hash"), dbs);
//
//    }catch (SQLException sqlException){
//      ErrorHandler.handleError(sqlException, "Error connecting to database");
//    }
//    return null;
//  }

  /**
   * Retrieves a single user by their email or username.
   * Searches both email and username if the provided string contains an "@" symbol.
   *
   * @param string the username or email of the user to retrieve
   * @return the {@link User} object matching the provided identifier, or {@code null} if not found
   */
  @Override
  public User getSingle(String string) {
    UserRepository userRepository = UserRepositoryImpl.getInstance();
    if(userRepository == null) return null;
    ArrayList<User> userArrayList = userRepository.getMany(Integer.MAX_VALUE, Integer.MAX_VALUE, null);

    if(string.contains("@")) {
      for(User user : userArrayList) {
        if(user.getEmail().equals(string)) return user;
        else if(user.getUsername().equals(string)) return user;
      }
    }
    return null;
  }

  /**
   * Deletes a user from the repository.
   * Currently, this method does nothing as deletion functionality is not yet implemented.
   *
   * @param user the {@link User} object to be deleted
   */
  @Override
  public void delete(User user) {
    // Deletion functionality not implemented
  }

  /**
   * Saves the changes made to a user.
   * This method currently does not modify anything in the repository.
   *
   * @param user the {@link User} object to be saved
   */
  @Override
  public void save(User user) {
    // Save functionality not implemented
  }

  /**
   * Retrieves a list of users with pagination and optional filtering by username or email.
   *
   * @param pageIndex the index of the page to retrieve (starting from 0)
   * @param pageSize  the number of users to return per page
   * @param string    a string used to filter users by username or email (optional)
   * @return a list of {@link User} objects matching the filter and pagination criteria
   */
  @Override
  public ArrayList<User> getMany(int pageIndex, int pageSize, String string) {
    ArrayList<User> result = new ArrayList<>();
    for (int i = 0; pageIndex * pageSize + i < users.size(); i++) {
      // This is an attempt at implementing paging
      User user = users.get(i);

      // Filter by username or email if 'string' is provided
      if (string != null && !string.isEmpty() &&
              (user.getUsername().contains(string) || user.getEmail().contains(string))) {
        result.add(user);
      }
    }
    return result;
  }

  @Override
  public void add(User newUser) {

  }
}

