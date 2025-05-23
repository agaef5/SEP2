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
  private ModelManager modelManager;


  public RegisterVM(ModelManager modelManager)
  {
    this.modelManager = modelManager;

    disableRegisterButtonProp.bind(usernameProp.isEmpty()
            .or(emailProp.isEmpty())
            .or(passwordProp.isEmpty()).or(repeatProp.isEmpty()));
    messageProp.bind(modelManager.registerMessageProperty());
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
    modelManager.registerUser(emailProp.get(),
            usernameProp.get(), passwordProp.get());
  }

  public BooleanProperty registerSuccessProperty() {
    return modelManager.registerSuccessProperty();
  }

  public StringProperty registerMessageProperty() {
    return messageProp;
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

  public BooleanBinding disableRegisterButtonPropriety() {
    return usernameProp.isEmpty()
            .or(passwordProp.isEmpty())
            .or(emailProp.isEmpty())
            .or(repeatProp.isEmpty());
  }
}
