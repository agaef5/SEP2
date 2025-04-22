package server.networking.socketHandling;

import server.services.authentication.AuthServiceImpl;
import server.services.authentication.AuthentificationService;
import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;

public class RegisterAndLoginHandler implements RequestHandler {
  private final AuthentificationService authService;

  public RegisterAndLoginHandler() {
    this.authService =  new AuthServiceImpl();
  }

  public Object handle(String action, Object payload) {
    switch (action) {
      case "login" -> {
        if (payload instanceof LoginRequest loginRequest) {
          return handleLogin(loginRequest);
        } else {
          throw new IllegalArgumentException("Invalid payload for login");
        }
      }
      case "register" -> {
        if (payload instanceof RegisterRequest registerRequest) {
          return handleRegister(registerRequest);
        } else {
          throw new IllegalArgumentException("Invalid payload for register");
        }
      }
      default -> throw new IllegalArgumentException("Invalid action: " + action);
    }
  }

  private LoginRespond handleLogin(LoginRequest loginRequest) {
    return authService.loginUser(loginRequest);
  }

  private RegisterRespond handleRegister(RegisterRequest registerRequest) {
    return authService.registerUser(registerRequest);
  }
}
