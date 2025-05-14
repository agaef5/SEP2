package server.services.authentication;

import server.model.User;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import server.util.DTOMapper;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;
import shared.user.BalanceUpdateRequest;
import shared.user.BalanceUpdateResponse;
import shared.user.UserRequest;
import shared.user.UserResponse;

import java.sql.SQLException;

/**
 * The {@code AuthServiceImpl} class implements the {@link AuthenticationService} interface
 * and provides functionality for user authentication, including user registration and login.
 * It interacts with the {@link UserRepository} to manage user data and performs validation
 * of the provided credentials during registration and login processes.
 */
public class AuthServiceImpl implements AuthenticationService
{
  /**
   * Registers a new user based on the provided {@link RegisterRequest}.
   * Validates the user data, checks if the username already exists, and adds the user
   * to the {@link UserRepository}.
   *
   * @param request The registration request containing username, email, and password.
   * @return A {@link RegisterRespond} indicating the result of the registration process.
   */
  @Override public RegisterRespond registerUser(RegisterRequest request)
  {
    // Check if none of the data are null
    String username = request.username();
    String email = request.email();
    String password = request.password();
    if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
      return new RegisterRespond("error", "Register data are empty");
    }

    // Getting UserRepository to work with
    UserRepository userRepository = UserRepositoryImpl.getInstance();

    try {
      // Checking if such username does not exist already
      if (userRepository.readByUsername(username) != null || userRepository.readByEmail(email) != null) {
        return new RegisterRespond("error", "User with such username already exists");
      }

      // Creating new user and adding to repository
      User newUser = userRepository.createUser(username, email, password, false);
      if (newUser == null) {
        return new RegisterRespond("error", "Error with creating user");
      }

      return new RegisterRespond("success", DTOMapper.UserToDTO(newUser));
    }
    catch (SQLException sqlException){
      return new RegisterRespond("error", "Error with creating user");
    }
  }

  /**
   * Logs in an existing user based on the provided {@link LoginRequest}.
   * Validates the credentials, checks if the user exists, and compares the
   * provided password with the stored password.
   *
   * @param request The login request containing the username and password.
   * @return A {@link LoginRespond} indicating the result of the login process.
   */
  @Override public LoginRespond loginUser(LoginRequest request)
  {
    // Check if none of the data are null
    String identifier = request.username();
    String password = request.password();
    if(identifier.isEmpty() || password.isEmpty()){
      return new LoginRespond("error", "Login data are empty");
    }

    try{
    // Get UserRepository and pull a list of Users
    UserRepository userRepository = UserRepositoryImpl.getInstance();
    User user;
    if(identifier.contains("@")){
      user = userRepository.readByEmail(identifier);
    }else{
      user = userRepository.readByUsername(identifier);
    }
//    if no user is found, return an error
    if(user == null)
      return new LoginRespond("error", "Error with getting user credentials");

//    check if the passwords are matching and return the LoginResponse
    if(password.equals(user.getPassword())) {
        return new LoginRespond("success", DTOMapper.UserToDTO(user));
    } else{
      return new LoginRespond("error", "Username and password do not match");
    }
    }catch (SQLException sqlException){
      return new LoginRespond("error", "Error with logging in the user");
    }
  }

  @Override
  public UserResponse getUser(UserRequest userRequest){
    UserRepository userRepository = UserRepositoryImpl.getInstance();
    try {
      User user;
      if (userRequest.identifier().contains("@")) {
        user = userRepository.readByEmail(userRequest.identifier());
      } else {
        user = userRepository.readByUsername(userRequest.identifier());
      }
      if(user != null) {
        return new UserResponse("success", DTOMapper.UserToDTO(user));
      }else throw new SQLException("User is null");
    }catch (SQLException sqlException){
      return new UserResponse("error", "No such user exists");
    }
  }

  @Override
  public BalanceUpdateResponse updateBalance(BalanceUpdateRequest balanceUpdateRequest){
    UserRepository userRepository = UserRepositoryImpl.getInstance();

    try {
      userRepository.updateBalance(balanceUpdateRequest.name(), balanceUpdateRequest.balance());
      User user = userRepository.readByUsername(balanceUpdateRequest.name());
      return new BalanceUpdateResponse("success", DTOMapper.UserToDTO(user));

    }catch (SQLException sqlException){
      return new BalanceUpdateResponse("error", "Error with updating balance");
    }
  }
}
