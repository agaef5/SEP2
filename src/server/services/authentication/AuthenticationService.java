package server.services.authentication;

import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;
import shared.user.BalanceUpdateRequest;
import shared.user.BalanceUpdateResponse;
import shared.user.UserRequest;
import shared.user.UserResponse;

/**
 * The {@code AuthenticationService} interface defines core operations
 * for handling user authentication and account-related services.
 * <p>
 * It includes methods for user registration, login, balance updates,
 * and user data retrieval based on identifiers (username/email).
 */
public interface AuthenticationService {

   /**
    * Registers a new user.
    * <p>
    * Validates the registration data, checks for duplicates,
    * and creates a new user record in the system.
    *
    * @param request The {@code RegisterRequest} object containing the user's registration data.
    * @return A {@code RegisterRespond} indicating success or failure with a message or user data.
    */
   RegisterRespond registerUser(RegisterRequest request);

   /**
    * Logs in a user.
    * <p>
    * Validates credentials and returns user data upon successful authentication.
    *
    * @param request The {@code LoginRequest} object containing the user's login credentials (username/email and password).
    * @return A {@code LoginRespond} indicating success or failure with an optional user DTO.
    */
   LoginRespond loginUser(LoginRequest request);

   /**
    * Updates the balance of an existing user.
    * <p>
    * Finds the user by username and modifies their balance in the database.
    *
    * @param balanceUpdateRequest A {@code BalanceUpdateRequest} object containing the username and new balance.
    * @return A {@code BalanceUpdateResponse} with updated user data or error message.
    */
   BalanceUpdateResponse updateBalance(BalanceUpdateRequest balanceUpdateRequest);

   /**
    * Retrieves a user by their identifier (username or email).
    *
    * @param userRequest A {@code UserRequest} containing the identifier to search for.
    * @return A {@code UserResponse} containing the user information or an error message.
    */
   UserResponse getUser(UserRequest userRequest);
}

