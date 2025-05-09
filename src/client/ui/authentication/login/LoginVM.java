package client.ui.authentication.login;

import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.ui.common.ViewModel;
import javafx.beans.property.*;
import shared.LoginRequest;

public class LoginVM implements ViewModel
{

  private StringProperty usernameProp = new SimpleStringProperty();
  private StringProperty passwordProp = new SimpleStringProperty();
  private StringProperty messageProp = new SimpleStringProperty();
  private BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(
      false);
  private AuthenticationClient authClient;
  private SocketService socketService;

    public LoginVM (AuthenticationClient authClient, SocketService socketService)
  {
    this.authClient = authClient;
    this.socketService = socketService;
  }

  public void loginUser ()
  {
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
        passwordProp.get()

    );
    try
    {
//      String response = authClient.loginUser(request);
//      messageProp.set(response);
    }
    catch ( Exception e )
    {
      messageProp.set("Registration failed: " + e.getMessage());
    }

    usernameProp.set("");
    passwordProp.set("");
    messageProp.set("");

  }

  public void disableLoginButtonProprietyLogic ()
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
}
