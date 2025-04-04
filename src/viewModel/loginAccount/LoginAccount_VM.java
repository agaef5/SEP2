package viewModel.loginAccount;

import javafx.beans.Observable;
import javafx.beans.property.*;

public class LoginAccount_VM
{

  private StringProperty username = new SimpleStringProperty();
  private StringProperty errorMessage_username = new SimpleStringProperty();
  private StringProperty password = new SimpleStringProperty();
  private  StringProperty errorMessage_password = new SimpleStringProperty();
  private BooleanProperty createNewAccount = new SimpleBooleanProperty(false);
  private BooleanProperty confirmLogin = new SimpleBooleanProperty(true);
  
  public LoginAccount_VM ()
  {
    username.addListener(this :: confirmLoginState);
    password.addListener(this :: confirmLoginState);
  }
  
  public StringProperty getUsername (){
    return username;
  }
  public StringProperty getErrorMessage_username(){
    return errorMessage_username;
  }
  public StringProperty getPassword (){
    return password;
  }
  public StringProperty getErrorMessage_password(){
    return errorMessage_password;
  }
  public BooleanProperty getConfirmLogin (){
    return confirmLogin;
  }
  public BooleanProperty getCreateNewAccount (){
    return createNewAccount;
  }
  public void confirmLogin (){
    if ( username.get() == null || username.get().isEmpty()){
      errorMessage_username.set("Username is empty"); //if usernam is a PK in database?
      return;
    }
    if ( password.get() == null || password.get().isEmpty()){
      errorMessage_password.set("Password is empty"); //if rules?
      return;
    }
    
    username.set("");
    errorMessage_username.set("");
    password.set("");
    errorMessage_password.set("");

  }
  public void confirmLoginState( Observable observable ){
    boolean shouldDisable =
        username.get() == null || username.get().isEmpty() ||
            password.get() == null || password.get().isEmpty();
    confirmLogin.set(shouldDisable);
    System.out.println("confirmLoginState: " + shouldDisable);
    
    username.set("");
    password.set("");
  }
}
