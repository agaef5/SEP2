package server.services.authentication;

import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;

/**
 * The {@code AuthentificationService} interface defines methods for user authentication services,
 * including user registration and login functionality.
 */
public interface AuthentificationService {

   /**
    * Registers a new user.
    *
    * @param request The {@code RegisterRequest} object containing the user's registration data.
    * @return A {@code RegisterRespond} object with the response of the registration attempt.
    */
   RegisterRespond registerUser(RegisterRequest request);

   /**
    * Logs in a user.
    *
    * @param request The {@code LoginRequest} object containing the user's login credentials.
    * @return A {@code LoginRespond} object with the response of the login attempt.
    */
   LoginRespond loginUser(LoginRequest request);
}
