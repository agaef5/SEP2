package client.networking.authentication;

import client.networking.SocketService;
import shared.LoginRequest;
import shared.RegisterRequest;
import shared.Request;
import com.google.gson.JsonElement;
import com.google.gson.Gson;

public class SocketAuthenticationClient implements AuthenticationClient {
  private SocketService socketService;
  private Gson gson = new Gson();

  public SocketAuthenticationClient(SocketService socketService) {
    this.socketService = socketService;
  }

  @Override
  public void registerUser(RegisterRequest registerRequest) {
    // Serialize the RegisterRequest to JsonElement
    JsonElement payload = gson.toJsonTree(registerRequest);

    // Create the Request object with JsonElement as payload
    Request request = new Request("auth", "register", payload);

    // Send the request using the socketService
    socketService.sendRequest(request);
  }

  @Override
  public void loginUser(LoginRequest loginRequest) {
    // Serialize the LoginRequest to JsonElement
    JsonElement payload = gson.toJsonTree(loginRequest);

    // Create the Request object with JsonElement as payload
    Request request = new Request("auth", "login", payload);

    // Send the request using the socketService
    socketService.sendRequest(request);
  }
}
