package client.networking.authentication;

import shared.loginRegister.LoginRequest;
import shared.loginRegister.RegisterRequest;
import shared.user.BalanceUpdateRequest;
import shared.user.UserRequest;

/**
 * Interface for handling user authentication-related operations.
 *
 * Defines methods for registering users, logging in, and performing
 * user-related queries such as fetching user data and updating balances.
 */
public interface AuthenticationClient {

  /**
   * Sends a request to register a new user.
   *
   * @param registerRequest the request object containing user registration details
   */
  void registerUser(RegisterRequest registerRequest);

  /**
   * Sends a request to log in an existing user.
   *
   * @param loginRequest the request object containing user login credentials
   */
  void loginUser(LoginRequest loginRequest);

  /**
   * Sends a request to retrieve user information based on a username.
   *
   * @param userRequest the request object containing the username
   */
  void getUser(UserRequest userRequest);

  /**
   * Sends a request to update the balance of a user.
   *
   * @param balanceUpdateRequest the request containing balance update details
   */
  void updateBalance(BalanceUpdateRequest balanceUpdateRequest);
}
