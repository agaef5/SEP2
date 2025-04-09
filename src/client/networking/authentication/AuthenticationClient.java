package client.networking.authentication;

import shared.LoginRequest;
import shared.RegisterRequest;

public interface AuthenticationClient
{
  void registerUser(RegisterRequest registerRequest);
  void loginUser(LoginRequest loginRequest);
}
