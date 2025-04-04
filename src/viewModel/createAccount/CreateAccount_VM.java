package viewModel.createAccount;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CreateAccount_VM
{
  private StringProperty username = new SimpleStringProperty();
  private StringProperty errorMessage_username = new SimpleStringProperty();
  private StringProperty email = new SimpleStringProperty();
  private StringProperty errorMessage_email = new SimpleStringProperty();
  private StringProperty password = new SimpleStringProperty();
  private StringProperty errorMessage_password = new SimpleStringProperty();
  private StringProperty confirmPassword = new SimpleStringProperty();
  private StringProperty errorMessage_passwordsDontMatch = new SimpleStringProperty();
  private BooleanProperty confirmCreateAccount = new SimpleBooleanProperty(false);
  
  public CreateAccount_VM ()
  {
    username.addListener(this :: getConfirmCreateAccount);
    email.addListener(this :: getConfirmCreateAccount);
    password.addListener(this :: getConfirmCreateAccount);
    confirmPassword.addListener(this :: getConfirmCreateAccount);
  }
  public StringProperty getUsername(){
    return username;
  }
  public StringProperty getErrorMessage_username(){
    return errorMessage_username;
  }
  public StringProperty getEmail(){
    return email;
  }
  public StringProperty getErrorMessage_email(){
    return errorMessage_email;
  }
  public StringProperty getPassword(){
    return password;
  }
  public StringProperty getErrorMessage_password  (){
    return errorMessage_password;
  }
  public StringProperty getErrorMessage_passwordsDontMatch(){
    return errorMessage_passwordsDontMatch;
  }
  public BooleanProperty getConfirmCreateAccount ()
  {
    return confirmCreateAccount;
  }
  
  public void registerAccount (){
    if ( email.get() == null || email.get().isEmpty()){
      errorMessage_email.set("Incorrect email");
      return;
    }
    if ( username.get() == null || username.get().isEmpty()){
      errorMessage_username.set("Username is empty"); //if usernam is a PK in database?
      return;
    }
    if ( password.get() == null || password.get().isEmpty()){
      errorMessage_password.set("Password is empty"); //if rules?
      return;
    }
     if ( ! confirmPassword.equals(password.get()) ){
       errorMessage_passwordsDontMatch.set("Passwords do not match");
       return;
     }
     
     email.set("");
     errorMessage_email.set("");
     username.set("");
     errorMessage_username.set("");
     password.set("");
     errorMessage_password.set("");
     confirmPassword.set("");
     errorMessage_passwordsDontMatch.set("");
  }
  public void getConfirmCreateAccount ( Observable observable){
    boolean shouldDisable =
        username.get() == null || username.get().isEmpty() ||
            password.get() == null || password.get().isEmpty();
    confirmCreateAccount.set(shouldDisable);
    System.out.println("confirmAccountState: " + shouldDisable);
    
    username.set("");
    email.set("");
    password.set("");
    confirmPassword.set("");
  }
}
