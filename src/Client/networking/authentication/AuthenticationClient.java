package Client.networking.authentication;

import Shared.LoginRequest;
import Shared.RegisterRequest;

public interface AuthenticationClient
{
  void registerUser(RegisterRequest registerRequest);
  void loginUser(LoginRequest loginRequest);
}
