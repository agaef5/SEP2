package client.ui.authentication.login;

import javafx.beans.property.*;

public class LoginVM
{
  
  private StringProperty usernameProp = new SimpleStringProperty();
  private StringProperty passwordProp = new SimpleStringProperty();
  private StringProperty messageProp = new SimpleStringProperty();
  private BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(false);
  
  
  public LoginVM ()
  {
  
  }
  
  public void loginUser (){
    if ( usernameProp.get() == null || usernameProp.get().isEmpty()){
      messageProp.set("Username is empty"); //if usernam is a PK in database?
      return;
    }
    if ( passwordProp.get() == null || passwordProp.get().isEmpty()){
      messageProp.set("Password is empty"); //if rules?
      return;
    }
    
    usernameProp.set("");
    passwordProp.set("");
    messageProp.set("");

  }
  public void disableLoginButtonProprietyLogic (){
    boolean shouldDisable =
        usernameProp.get() == null || usernameProp.get().isEmpty() ||
            passwordProp.get() == null || passwordProp.get().isEmpty();
    disableLoginButtonProp.set(shouldDisable);
    System.out.println("confirmLoginState: " + shouldDisable);
    
    usernameProp.set("");
    passwordProp.set("");
  }
  public BooleanProperty disableLoginButtonPropriety(){
    return disableLoginButtonProp;
  }
  public StringProperty userNamePropriety (){
    return usernameProp;
  }
  public StringProperty passwordPropriety (){
    return passwordProp;
  }
}
