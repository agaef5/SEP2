package server.services.authentication;

import server.model.User;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import server.util.DTOMapper;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;
import shared.user.BalanceUpdateRequest;
import shared.user.BalanceUpdateResponse;
import shared.user.UserRequest;
import shared.user.UserResponse;

import java.sql.SQLException;

/**
 * The {@code AuthServiceImpl} class implements the {@link AuthenticationService} interface
 * and provides functionality for user authentication, registration, and balance management.
 * It uses {@link UserRepository} to interact with the database and handle user data operations.
 */
public class AuthServiceImpl implements AuthenticationService {

  /**
   * Registers a new user with the provided information.
   * Performs validation checks, ensures username and email are unique,
   * and persists the new user to the database.
   *
   * @param request The {@link RegisterRequest} containing username, email, and password.
   * @return {@link RegisterRespond} with success status or error message.
   */
  @Override
  public RegisterRespond registerUser(RegisterRequest request) {
    // Extract fields
    String username = request.username();
    String email = request.email();
    String password = request.password();

    // Validate input fields are not empty
    if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
      return new RegisterRespond("error", "Register data are empty");
    }

    UserRepository userRepository = UserRepositoryImpl.getInstance();

    try {
      // Check if username or email already exists
      if (userRepository.readByUsername(username) != null || userRepository.readByEmail(email) != null) {
        return new RegisterRespond("error", "User with such username already exists");
      }

      // Create and save new user
      User newUser = userRepository.createUser(username, email, password, false);
      if (newUser == null) {
        return new RegisterRespond("error", "Error with creating user");
      }

      // Return success response with user data
      return new RegisterRespond("success", DTOMapper.UserToDTO(newUser));
    } catch (SQLException sqlException) {
      return new RegisterRespond("error", "Error with creating user");
    }
  }

  /**
   * Logs in a user based on provided credentials (username/email and password).
   * Looks up the user and verifies password match.
   *
   * @param request The {@link LoginRequest} containing identifier and password.
   * @return {@link LoginRespond} indicating success or failure.
   */
  @Override
  public LoginRespond loginUser(LoginRequest request) {
    String identifier = request.username();
    String password = request.password();

    // Validate input fields are not empty
    if (identifier.isEmpty() || password.isEmpty()) {
      return new LoginRespond("error", "Login data are empty");
    }

    try {
      UserRepository userRepository = UserRepositoryImpl.getInstance();

      // Determine if identifier is email or username
      User user = identifier.contains("@")
              ? userRepository.readByEmail(identifier)
              : userRepository.readByUsername(identifier);

      // User not found
      if (user == null) {
        return new LoginRespond("error", "Error with getting user credentials");
      }

      // Check if password matches
      if (password.equals(user.getPassword())) {
        return new LoginRespond("success", DTOMapper.UserToDTO(user));
      } else {
        return new LoginRespond("error", "Username and password do not match");
      }

    } catch (SQLException sqlException) {
      return new LoginRespond("error", "Error with logging in the user");
    }
  }

  /**
   * Retrieves a user by their identifier (username or email).
   *
   * @param userRequest A {@link UserRequest} containing the identifier.
   * @return A {@link UserResponse} with user data or an error message.
   */
  @Override
  public UserResponse getUser(UserRequest userRequest) {
    UserRepository userRepository = UserRepositoryImpl.getInstance();

    try {
      // Determine if identifier is email or username
      User user = userRequest.identifier().contains("@")
              ? userRepository.readByEmail(userRequest.identifier())
              : userRepository.readByUsername(userRequest.identifier());

      if (user != null) {
        return new UserResponse("success", DTOMapper.UserToDTO(user));
      } else {
        throw new SQLException("User is null");
      }

    } catch (SQLException sqlException) {
      return new UserResponse("error", "No such user exists");
    }
  }

  /**
   * Updates the balance of a user identified by username.
   *
   * @param balanceUpdateRequest {@link BalanceUpdateRequest} with username and new balance.
   * @return {@link BalanceUpdateResponse} with updated user data or error message.
   */
  @Override
  public BalanceUpdateResponse updateBalance(BalanceUpdateRequest balanceUpdateRequest) {
    UserRepository userRepository = UserRepositoryImpl.getInstance();

    try {
      // Update balance in the database
      userRepository.updateBalance(balanceUpdateRequest.name(), balanceUpdateRequest.balance());

      // Fetch updated user
      User user = userRepository.readByUsername(balanceUpdateRequest.name());

      return new BalanceUpdateResponse("success", DTOMapper.UserToDTO(user));
    } catch (SQLException sqlException) {
      return new BalanceUpdateResponse("error", "Error with updating balance");
    }
  }
}
