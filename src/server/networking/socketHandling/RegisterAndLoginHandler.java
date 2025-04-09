package server.networking.socketHandling;

import server.services.authentication.AuthServiceImpl;
import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;
import com.google.gson.Gson;

public class RegisterAndLoginHandler
{
  private final Gson gson = new Gson();
  private AuthServiceImpl authService;

  public Object handle(String action, Object payload) // check what action the user wants (login or register) and than call appropriate methods in "Model"
  {
    switch (action)
    {
      case "login":
        return handleLogin((LoginRequest) payload);
      case "register":
        return handleRegister((RegisterRequest) payload);
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
