package client.networking.authentication;

import shared.loginRegister.LoginRequest;
import shared.loginRegister.RegisterRequest;
import shared.user.UserRequest;

/**
 * Interface for handling user authentication-related operations.
 * <p>
 * This interface defines methods for registering and logging in users.
 * Implementations of this interface are responsible for sending the
 * appropriate requests to the server via a network layer.
 */
public interface AuthenticationClient
{
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

  void getUser(UserRequest userRequest);
}
