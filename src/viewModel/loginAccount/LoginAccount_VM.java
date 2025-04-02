package viewModel.loginAccount;

import javafx.beans.Observable;
import javafx.beans.property.*;

public class LoginAccount_VM
{
  private StringProperty username = new SimpleStringProperty();
  private StringProperty password = new SimpleStringProperty();
  private BooleanProperty createNewAccount = new SimpleBooleanProperty(false);
  private BooleanProperty confirmLogin = new SimpleBooleanProperty(true);
  
  public LoginAccount_VM ()
  {
    username.addListener(this :: confirmLoginState);
    password.addListener(this :: confirmLoginState);
  }
  
  public StringProperty username(){
    return username;
  }
  public StringProperty password(){
    return password;
  }
  public BooleanProperty confirmLogin(){
    return confirmLogin;
  }
  public BooleanProperty createNewAccountButton(){
    return createNewAccount;
  }
  public void confirmLoginState ( Observable observable){
    boolean shouldDisable =
        username.get() == null || username.get().isEmpty() ||
            password.get() == null || password.get().isEmpty();
    confirmLogin.set(shouldDisable);
    System.out.println(shouldDisable);
  }
}
