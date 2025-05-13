package server.persistence.user;

import server.model.User;

import java.util.ArrayList;
import java.util.List;

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
  User createUser(String username, String email, String password, boolean isAdmin);

  /**
   * Retrieves a single user from the repository by a unique identifier.
   *
   * @param string the identifier (e.g., username or email) of the user
   * @return the {@link User} object associated with the given identifier, or {@code null} if no user exists
   *         with the specified identifier
   */
  User getSingle(String string);

  /**
   * Deletes a user from the repository.
   *
   * @param user the {@link User} object to be deleted
   */
  void delete(User user);

  /**
   * Saves the changes made to an existing user or adds a new user if it doesn't exist.
   *
   * @param user the {@link User} object to be saved
   */
  void save(User user);

  /**
   * Retrieves a list of users from the repository with pagination support.
   *
   * @param pageIndex the index of the page to retrieve (starting from 0)
   * @param pageSize  the number of users to return per page
   * @param string    the search string used to filter users (e.g., by username or email)
   * @return a list of {@link User} objects that match the search criteria
   */
  ArrayList<User> getMany(int pageIndex, int pageSize, String string);

  void add(User newUser);
}
