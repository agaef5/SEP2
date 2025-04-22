package server.services.authentication;

import server.model.Player;
import server.model.User;
import server.persistence.user.UserRepository;
import server.persistence.user.UserRepositoryImpl;
import shared.LoginRequest;
import shared.LoginRespond;
import shared.RegisterRequest;
import shared.RegisterRespond;

import java.util.ArrayList;

public class AuthServiceImpl implements AuthentificationService
{
  @Override public RegisterRespond registerUser(RegisterRequest request)
  {
    //    check if none of the data are null
    String username = request.username();
    String email = request.email();
    String password = request.password();
    if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
      return new RegisterRespond("error", "Register data are empty");
    }

    //    checking and validating the UserRepository
    UserRepository userRepository = fetchUserRepository();
    ArrayList<User> userArrayList = fetchUserArrayList();
    if (userRepository == null || userArrayList == null || userArrayList.isEmpty()) return new RegisterRespond("error", "Error with fetching UserRepository");

    //    checking if such username does not exist already
    for(User user : userArrayList){
      if(user.getUsername().equals(username)){
        return new RegisterRespond("error", "User with such username already exists");
      }
    }

    User newUser = new Player(username, email, password);
    userRepository.add(newUser);

    //TODO: check if the payload has to be "null" or contain User object
    return new RegisterRespond("success", newUser);
  }

  @Override public LoginRespond loginUser(LoginRequest request)
  {
    //    check if none of the data are null
    String username = request.username();
    String password = request.password();
    if(username.isEmpty() || password.isEmpty()){
      return new LoginRespond("error", "Login data are empty");
    }

    //  get UserRepository and pull a list of Users
    ArrayList<User> userArrayList = fetchUserArrayList();
    if (userArrayList == null || userArrayList.isEmpty()) return new LoginRespond("error", "Error with fetching UserRepository");

    //    iterate through the list
    for(User user : userArrayList){
      if(user.getUsername().equals(username)){
        if(user.getPassword().equals(password)){
          //TODO: check if the payload has to be "null" or contain User object
          return new LoginRespond("success", user);
        }
        else{
          return new LoginRespond("error", "Username and password do not match");
        }
      }
    }
    //    if no user found in the list, return an error
    return new LoginRespond("error", "No such user exists");
  }

  private UserRepository fetchUserRepository(){

    return UserRepositoryImpl.getInstance();
  }

  private ArrayList<User> fetchUserArrayList(){
    UserRepository userRepository = fetchUserRepository();
    if(userRepository == null) return null;
    ArrayList<User> userArrayList = userRepository.getMany(Integer.MAX_VALUE, Integer.MAX_VALUE, null);
    if(userArrayList == null || userArrayList.getFirst() == null) return null;

    return userArrayList;
  }
}