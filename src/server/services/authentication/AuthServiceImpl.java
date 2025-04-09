package server.services.authentication;

import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;

public class AuthServiceImpl implements AuthentificationService
{
  @Override public RegisterRespond registerUser(RegisterRequest request)
  {
return new RegisterRespond("success", "yess success");
  }

  @Override public LoginRespond loginUser(LoginRequest request)
  {
    return new LoginRespond("success", "yess success");
  }
}
