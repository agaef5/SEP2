package client.networking.authentication;

import client.networking.SocketService;
import shared.LoginRequest;
import shared.RegisterRequest;
import shared.Request;

public class SocketAuthenticationClient implements AuthenticationClient
{
  private SocketService socketService;

  public SocketAuthenticationClient(SocketService socketService)
  {
    this.socketService=socketService;
  }

  @Override public void registerUser(RegisterRequest registerRequest)
  {
    Request request = new Request("auth","register", registerRequest);
    socketService.sendRequest(request);
  }

  @Override public void loginUser(LoginRequest loginRequest)
  {
  Request request = new Request("auth","login", loginRequest);
  socketService.sendRequest(request);
  }
}
