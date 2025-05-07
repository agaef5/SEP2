package server.persistence.user;

import server.model.Admin;
import server.model.Player;
import server.model.User;

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
  private UserRepositoryImpl() {
    // TODO: Replace with actual database
    users = new ArrayList<>(Arrays.asList(
            new Player("user1", "trmo@via.dk", "1234"),
            new Player("user2", "jaja@gmail.com", "1234"),
            new Player("user3", "pepe@gmail.com", "1234"),
            new Player("user4", "jeje@gmail.com", "1234"),
            new Player("user5", "momo@gmail.com", "1234"),
            new Player("user6", "anan@gmail.com", "1234"),
            new Admin("admin", "admin@gamil.com", "1234")
    ));
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
          instance = new UserRepositoryImpl();
        }
      }
    }
    return instance;
  }

  /**
   * Adds a new user to the repository.
   *
   * @param user the {@link User} object to be added
   */
  @Override
  public void add(User user) {
    users.add(user);
  }

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
}
