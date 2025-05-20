package server.persistence.user;

import server.model.User;
import shared.DTO.UserDTO;

import java.sql.SQLException;

/**
 * The {@link UserRepository} interface defines methods for managing {@link User} objects in a repository.
 * It provides methods for adding, retrieving, saving, and deleting user records. Additionally, it supports
 * pagination for retrieving multiple users at once.
 */
public interface UserRepository {

  /**
   * Adds a new user to the repository.
   *
   * @param username the {@link User} object to be added
   */
  User createUser(String username, String email, String password, boolean isAdmin) throws SQLException;

  /**
   * Retrieves a single user from the repository by username.
   *
   * @param username the identifier of the user
   * @return the {@link User} object associated with the given identifier, or {@code null} if no user exists
   *         with the specified identifier
   */
  User readByUsername(String username) throws SQLException;

  /**
   * Retrieves a single user from the repository by email.
   *
   * @param email the identifier of the user
   * @return the {@link User} object associated with the given identifier, or {@code null} if no user exists
   *         with the specified identifier
   */
  User readByEmail(String email)  throws SQLException;

  /**
   * Updates user balance.
   *
   * @param username - user whose balance has to be updated
   * @param newBalance - new balance worth
   * @throws SQLException - exception thrown, if the operation is not successful
   */
  void updateBalance(String username, int newBalance)  throws SQLException;

}
