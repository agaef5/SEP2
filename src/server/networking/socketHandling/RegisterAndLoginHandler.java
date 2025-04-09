package server.networking.socketHandling;

import server.services.authentication.AuthServiceImpl;
import server.services.authentication.AuthentificationService;
import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;
import com.google.gson.Gson;

public class RegisterAndLoginHandler implements RequestHandler
{
  private final Gson gson = new Gson();
  private AuthentificationService authService;

  public RegisterAndLoginHandler()
  {
    this.authService= new AuthServiceImpl();
  }

  public Object handle(String action, Object payload) // check what action the user wants (login or register) and than call appropriate methods in "Model"
  {
    switch (action)
    {
      case "login":
        LoginRequest loginRequest = gson.fromJson(gson.toJson(payload), LoginRequest.class);
        return handleLogin(loginRequest);
      case "register":
        RegisterRequest registerRequest = gson.fromJson(gson.toJson(payload), RegisterRequest.class);
        return handleRegister(registerRequest);
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
