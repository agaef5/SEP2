package client.networking.authentication;

import client.networking.SocketService;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.RegisterRequest;
import shared.Request;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import shared.user.BalanceUpdateRequest;
import shared.user.UserRequest;

/**
 * Implementation of AuthenticationClient that sends authentication-related requests
 * (login and registration) over a socket connection using SocketService.
 */
public class SocketAuthenticationClient implements AuthenticationClient {

  private SocketService socketService;
  private Gson gson = new Gson();

  /**
   * Constructs a SocketAuthenticationClient with the specified socket service.
   *
   * @param socketService the socket service used to send requests
   */
  public SocketAuthenticationClient(SocketService socketService) {
    this.socketService = socketService;
  }

  /**
   * Sends a registration request to the server using the socket service.
   *
   * @param registerRequest the registration request containing user information
   */
  @Override
  public void registerUser(RegisterRequest registerRequest) {
    JsonElement payload = gson.toJsonTree(registerRequest);
    Request request = new Request("auth", "register", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a login request to the server using the socket service.
   *
   * @param loginRequest the login request containing user credentials
   */
  @Override
  public void loginUser(LoginRequest loginRequest) {
    JsonElement payload = gson.toJsonTree(loginRequest);
    Request request = new Request("auth", "login", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a user information request to the server.
   *
   * @param userRequest the request object containing user identifier data
   */
  @Override
  public void getUser(UserRequest userRequest){
    JsonElement payload = gson.toJsonTree(userRequest);
    Request request = new Request("auth", "getUser", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a request to update the user's balance on the server.
   *
   * @param balanceUpdateRequest the request containing updated balance data
   */
  @Override
  public void updateBalance(BalanceUpdateRequest balanceUpdateRequest){
    JsonElement payload = gson.toJsonTree(balanceUpdateRequest);
    Request request = new Request("auth", "updateBalance", payload);
    socketService.sendRequest(request);
  }
}
