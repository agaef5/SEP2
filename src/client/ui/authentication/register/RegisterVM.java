package client.ui.authentication.register;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
//import client.networking.authentication.AuthenticationClient;
//import client.networking.authentication.SocketAuthenticationClient;

public class RegisterVM
{
  private StringProperty emailProp = new SimpleStringProperty();
  private StringProperty passwordProp = new SimpleStringProperty();
  private StringProperty repeatProp = new SimpleStringProperty();
  private StringProperty usernameProp = new SimpleStringProperty();
  private StringProperty messageProp = new SimpleStringProperty();
  private BooleanProperty disableRegisterButtonProp = new SimpleBooleanProperty(false);
  
//  AuthecationClient authecationClient = new SocketAuthenticationClient();
  
  public RegisterVM ()
  {
  
  }
  
  public void registerUser(){
    if ( emailProp.get() == null || emailProp.get().isEmpty()){
      messageProp.set("Incorrect email");
      return;
    }
    if ( usernameProp.get() == null || usernameProp.get().isEmpty()){
      messageProp.set("Username is empty");
      return;
    }
    if ( passwordProp.get() == null || passwordProp.get().isEmpty()){
      messageProp.set("Password is empty");
      return;
    }
    if ( ! repeatProp.get().equals(passwordProp.get()) ){
      messageProp.set("Passwords do not match");
      return;
    }
    
    emailProp.set("");
    usernameProp.set("");
    passwordProp.set("");
    repeatProp.set("");
  }
  public StringProperty passwordPropriety(){
    return passwordProp;
  }
  public StringProperty repeatPropriety(){
    return repeatProp;
  }
  public StringProperty userNamePropriety(){
    return usernameProp;
  }
  public StringProperty emailPropriety(){
    return emailProp;
  }
  public StringProperty messagePropriety(){
    return messageProp;
  }
  public BooleanProperty disableRegisterButtonPropriety (){
    return disableRegisterButtonProp;
  }
//  public void DisableRegisterButtonPropriety ( Observable observable){
//    boolean shouldDisable =
//        usernameProp.get() == null || usernameProp.get().isEmpty() ||
//            passwordProp.get() == null || passwordProp.get().isEmpty();
//    disableRegisterButtonProp.set(shouldDisable);
//    System.out.println("confirmAccountState: " + shouldDisable);
//
//    usernameProp.set("");
//    emailProp.set("");
//    passwordProp.set("");
//    repeatProp.set("");
//  }
}
