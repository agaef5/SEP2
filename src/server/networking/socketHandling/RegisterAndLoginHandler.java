package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import server.services.authentication.AuthServiceImpl;
import server.services.authentication.AuthentificationService;
import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;

public class RegisterAndLoginHandler extends BaseRequestHandler
{
  private final AuthentificationService authService;
  private final Gson gson = new Gson();

  public RegisterAndLoginHandler()
  {
    this.authService = new AuthServiceImpl();
  }

  @Override public Object safeHandle(String action, JsonElement payload)
  {
      switch (action)
      {
        case "login" ->
        {
          LoginRequest loginRequest = parsePayload(payload,
              LoginRequest.class);
          return handleLogin(loginRequest);
        }
        case "register" ->
        {
          RegisterRequest registerRequest = parsePayload(payload,
              RegisterRequest.class);
          return handleRegister(registerRequest);
        }
        default ->
            throw new IllegalArgumentException("Invalid action: " + action);
      }
  }

  private LoginRespond handleLogin(LoginRequest loginRequest)
  {
    return authService.loginUser(loginRequest);
  }

  private RegisterRespond handleRegister(RegisterRequest registerRequest)
  {
    return authService.registerUser(registerRequest);
  }
}
