package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import server.services.authentication.AuthServiceImpl;
import server.services.authentication.AuthenticationService;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;
import shared.user.BalanceUpdateRequest;
import shared.user.BalanceUpdateResponse;
import shared.user.UserRequest;
import shared.user.UserResponse;

/**
 * {@code RegisterAndLoginHandler} processes user authentication-related requests such as
 * login and registration. It interacts with the {@code AuthentificationService} to
 * handle user authentication logic.
 */
public class RegisterAndLoginHandler extends BaseRequestHandler {
  private final AuthenticationService authService;
  private final Gson gson = new Gson();

  /**
   * Constructor to initialize the {@code RegisterAndLoginHandler} with an
   * {@code AuthentificationService}. This service handles user login and registration.
   */
  public RegisterAndLoginHandler() {
    this.authService = new AuthServiceImpl();
  }

  /**
   * This method handles various authentication-related actions based on the provided action string.
   * Each action corresponds to a specific user authentication operation, which is delegated
   * to the appropriate method for processing.
   *
   * @param action The action to be performed (e.g., "login", "register").
   * @param payload The payload containing necessary data for the action.
   * @return The response generated after processing the request.
   * @throws IllegalArgumentException If the action is invalid.
   */
  @Override
  public Object safeHandle(String action, JsonElement payload) {
    switch (action) {
      case "login" -> {
        LoginRequest loginRequest = parsePayload(payload, LoginRequest.class);
        return handleLogin(loginRequest);
      }
      case "register" -> {
        RegisterRequest registerRequest = parsePayload(payload, RegisterRequest.class);
        return handleRegister(registerRequest);
      }
      case "updateBalance" -> {
        BalanceUpdateRequest balanceUpdateRequest = parsePayload(payload, BalanceUpdateRequest.class);
        return handleBalanceUpdate(balanceUpdateRequest);
      }
      case "getUser" -> {
        UserRequest userRequest = parsePayload(payload, UserRequest.class);
        return handleGetUser(userRequest);
      }
      default -> throw new IllegalArgumentException("Invalid action: " + action);
    }
  }

  /**
   * Handles the request to log in a user.
   *
   * @param loginRequest The login request containing the user's credentials.
   * @return The response containing login result information (success/failure).
   */
  private LoginRespond handleLogin(LoginRequest loginRequest) {
    return authService.loginUser(loginRequest);
  }

  /**
   * Handles the request to register a new user.
   *
   * @param registerRequest The registration request containing the user's details.
   * @return The response containing registration result information (success/failure).
   */
  private RegisterRespond handleRegister(RegisterRequest registerRequest) {
    return authService.registerUser(registerRequest);
  }

  private BalanceUpdateResponse handleBalanceUpdate(BalanceUpdateRequest balanceUpdateRequest) {
    return authService.updateBalance(balanceUpdateRequest);
  }

  private UserResponse handleGetUser(UserRequest userRequest){
    return authService.getUser(userRequest);
  }
}
