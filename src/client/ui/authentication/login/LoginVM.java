package client.ui.authentication.login;

import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.ui.common.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.beans.property.*;
import shared.DTO.HorseDTO;
import shared.horse.CreateHorseResponse;
import shared.horse.HorseListResponse;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;

public class LoginVM implements ViewModel, MessageListener
{

  private StringProperty usernameProp = new SimpleStringProperty();
  private StringProperty passwordProp = new SimpleStringProperty();
  private StringProperty messageProp = new SimpleStringProperty();
  private BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(
      false);

  private AuthenticationClient authClient;
  private SocketService socketService;
  private Gson gson;

    public LoginVM (AuthenticationClient authClient, SocketService socketService)
  {
    this.authClient = authClient;
    this.socketService = socketService;
    gson = new Gson();
  }

  public void loginUser()
  {
    messageProp.set("");

    if ( usernameProp.get() == null || usernameProp.get().isEmpty() )
    {
      messageProp.set("Username is empty"); //if usernam is a PK in database?
      return;
    }
    if ( passwordProp.get() == null || passwordProp.get().isEmpty() )
    {
      messageProp.set("Password is empty"); //if rules?
      return;
    }
    LoginRequest request = new LoginRequest(usernameProp.get(),
        passwordProp.get());
    authClient.loginUser(request);
  }

  public void disableLoginButtonProprietyLogic()
  {
    boolean shouldDisable =
        usernameProp.get() == null || usernameProp.get().isEmpty()
            || passwordProp.get() == null || passwordProp.get().isEmpty();
    disableLoginButtonProp.set(shouldDisable);
    System.out.println("confirmLoginState: " + shouldDisable);

    usernameProp.set("");
    passwordProp.set("");
  }

  public BooleanProperty disableLoginButtonPropriety ()
  {
    return disableLoginButtonProp;
  }

  public StringProperty userNamePropriety ()
  {
    return usernameProp;
  }

  public StringProperty passwordPropriety ()
  {
    return passwordProp;
  }

  @Override
  public void update(String type, String payload) {
    System.out.println("Message received: " + type);
      if (type.equals("login")) {
          LoginRespond loginRespond = gson.fromJson(payload, LoginRespond.class);
          handleLogin(loginRespond);
      }
  }

  public void handleLogin(LoginRespond loginRespond){
      if(loginRespond.message().equals("error")){
        messageProp.set("Login failed: " + loginRespond.payload().toString());
      }
  }
}
