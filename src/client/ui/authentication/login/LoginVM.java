package client.ui.authentication.login;

import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.startup.RunClient;
import client.ui.common.MessageListener;
import client.ui.common.ViewModel;
import client.ui.util.ErrorHandler;
import com.google.gson.Gson;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
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
      messageProp.set("Username is empty");
      return;
    }
    if ( passwordProp.get() == null || passwordProp.get().isEmpty() )
    {
      messageProp.set("Password is empty");
      return;
    }
    LoginRequest request = new LoginRequest(usernameProp.get(),
        passwordProp.get());
    authClient.loginUser(request);
  }

  public BooleanBinding disableLoginButtonPropriety ()
  {
    return usernameProp.isEmpty().or(passwordProp.isEmpty());
  }

  public StringProperty userNamePropriety ()
  {
    return usernameProp;
  }

  public StringProperty passwordPropriety ()
  {
    return passwordProp;
  }

  public StringProperty messageProperty(){
      return messageProp;
  }

  @Override
  public void update(String type, String payload) {
    System.out.println("Message received in LoginVM: " + type);
      if (type.equals("login")) {
          LoginRespond loginRespond = gson.fromJson(payload, LoginRespond.class);
          handleLogin(loginRespond);
      }
  }

  public void handleLogin(LoginRespond loginRespond){
      if(loginRespond.message().equals("error")){
        ErrorHandler.handleError(new Exception(""),loginRespond.payload().toString());
        messageProp.set("Login failed");
      }
  }
}
