package Server.networking.socketHandling;

import Server.services.authentication.AuthServiceImpl;
import Server.services.authentication.AuthentificationService;
import Shared.LoginRequest;
import Shared.LoginRespond;
import Shared.RegisterRequest;
import Shared.RegisterRespond;
import com.google.gson.Gson;

public class RegisterAndLoginHandler
{
  private final Gson gson = new Gson();
  private AuthServiceImpl authService;

  public Object handle(String action, Object payload)
  {
    switch (action)
    {
      case "login":
        return handleLogin(
            gson.fromJson(gson.toJson(payload), LoginRequest.class));
      case "register":
        return handleRegister(
            gson.fromJson(gson.toJson(payload), RegisterRequest.class));
      default:
        throw new IllegalArgumentException("Invalid action: " + action);
    }
  }

  private LoginRespond handleLogin(LoginRequest loginRequest)
  {
    LoginRespond respond = authService.loginUser(loginRequest);
    return respond;
  }

  private RegisterRespond handleRegister(RegisterRequest registerRequest)
  {
    RegisterRespond respond = authService.registerUser(registerRequest);
    return respond;
  }
}
