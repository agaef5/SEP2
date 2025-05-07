package server.services.authentication;

import server.model.Player;
import server.model.User;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;

import java.util.ArrayList;

/**
 * The {@code AuthServiceImpl} class implements the {@link AuthentificationService} interface
 * and provides functionality for user authentication, including user registration and login.
 * It interacts with the {@link UserRepository} to manage user data and performs validation
 * of the provided credentials during registration and login processes.
 */
public class AuthServiceImpl implements AuthentificationService
{
  /**
   * Registers a new user based on the provided {@link RegisterRequest}.
   * Validates the user data, checks if the username already exists, and adds the user
   * to the {@link UserRepository}.
   *
   * @param request The registration request containing username, email, and password.
   * @return A {@link RegisterRespond} indicating the result of the registration process.
   */
  @Override public RegisterRespond registerUser(RegisterRequest request)
  {
    // Check if none of the data are null
    String username = request.username();
    String email = request.email();
    String password = request.password();
    if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
      return new RegisterRespond("error", "Register data are empty");
    }

    // Checking and validating the UserRepository
    UserRepository userRepository = fetchUserRepository();
    ArrayList<User> userArrayList = fetchUserArrayList();
    if (userRepository == null || userArrayList == null || userArrayList.isEmpty())
      return new RegisterRespond("error", "Error with fetching UserRepository");

    // Checking if such username does not exist already
    for(User user : userArrayList){
      if(user.getUsername().equals(username)){
        return new RegisterRespond("error", "User with such username already exists");
      }
    }

    // Creating new user and adding to repository
    User newUser = new Player(username, email, password);
    userRepository.add(newUser);

    //TODO: check if the payload has to be "null" or contain User object
    return new RegisterRespond("success", newUser);
  }

  /**
   * Logs in an existing user based on the provided {@link LoginRequest}.
   * Validates the credentials, checks if the user exists, and compares the
   * provided password with the stored password.
   *
   * @param request The login request containing the username and password.
   * @return A {@link LoginRespond} indicating the result of the login process.
   */
  @Override public LoginRespond loginUser(LoginRequest request)
  {
    // Check if none of the data are null
    String username = request.username();
    String password = request.password();
    if(username.isEmpty() || password.isEmpty()){
      return new LoginRespond("error", "Login data are empty");
    }

    // Get UserRepository and pull a list of Users
    ArrayList<User> userArrayList = fetchUserArrayList();
    if (userArrayList == null || userArrayList.isEmpty())
      return new LoginRespond("error", "Error with fetching UserRepository");

    // Iterate through the list to find matching user
    for(User user : userArrayList){
      if(user.getUsername().equals(username)){
        if(user.getPassword().equals(password)){
          //TODO: check if the payload has to be "null" or contain User object
          return new LoginRespond("success", user);
        }
        else{
          return new LoginRespond("error", "Username and password do not match");
        }
      }
    }

    // If no user found, return an error
    return new LoginRespond("error", "No such user exists");
  }

  /**
   * Fetches the {@link UserRepository} instance.
   *
   * @return The {@link UserRepository} instance.
   */
  private UserRepository fetchUserRepository(){
    return UserRepositoryImpl.getInstance();
  }

  /**
   * Fetches the list of users from the {@link UserRepository}.
   *
   * @return An {@link ArrayList} of users, or {@code null} if no users are found.
   */
  private ArrayList<User> fetchUserArrayList(){
    UserRepository userRepository = fetchUserRepository();
    if(userRepository == null) return null;
    ArrayList<User> userArrayList = userRepository.getMany(Integer.MAX_VALUE, Integer.MAX_VALUE, null);
    if(userArrayList == null || userArrayList.isEmpty()) return null;

    return userArrayList;
  }
}
