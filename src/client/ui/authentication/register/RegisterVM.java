package client.ui.authentication.register;


import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import client.networking.authentication.AuthenticationClient;
import shared.loginRegister.RegisterRequest;


public class RegisterVM implements ViewModel
{
  private StringProperty emailProp = new SimpleStringProperty();
  private StringProperty passwordProp = new SimpleStringProperty();
  private StringProperty repeatProp = new SimpleStringProperty();
  private StringProperty usernameProp = new SimpleStringProperty();
  private StringProperty messageProp = new SimpleStringProperty();
  private BooleanProperty disableRegisterButtonProp = new SimpleBooleanProperty(
          false);
  private AuthenticationClient authClient;
  private ModelManager modelManager;


  public RegisterVM(ModelManager modelManager)
  {
    this.modelManager = modelManager;
  }


  public void registerUser ()
  {
    if ( emailProp.get() == null || emailProp.get().isEmpty() || !emailProp.get().contains("@") )
    {
      messageProp.set("Incorrect email");
      return;
    }
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
    if ( ! repeatProp.get().equals(passwordProp.get()) )
    {
      messageProp.set("Passwords do not match");
      return;
    }
    RegisterRequest request = new RegisterRequest(emailProp.get(),
            usernameProp.get(), passwordProp.get());
    authClient.registerUser(request);
  }


  public StringProperty passwordPropriety ()
  {
    return passwordProp;
  }


  public StringProperty repeatPropriety ()
  {
    return repeatProp;
  }


  public StringProperty userNamePropriety ()
  {
    return usernameProp;
  }


  public StringProperty emailPropriety ()
  {
    return emailProp;
  }


  public StringProperty messagePropriety ()
  {
    return messageProp;
  }


  public BooleanBinding disableRegisterButtonPropriety() {
    return usernameProp.isEmpty()
            .or(passwordProp.isEmpty())
            .or(emailProp.isEmpty())
            .or(repeatProp.isEmpty());
  }
}
