package client.ui.authentication.register;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ViewModel for the Register view.
 *
 * Handles user input validation and communicates with the ModelManager to register a new user.
 * Exposes bindable properties for UI elements and controls button state.
 */
public class RegisterVM implements ViewModel {

  private StringProperty emailProp = new SimpleStringProperty();
  private StringProperty passwordProp = new SimpleStringProperty();
  private StringProperty repeatProp = new SimpleStringProperty();
  private StringProperty usernameProp = new SimpleStringProperty();
  private StringProperty messageProp = new SimpleStringProperty();
  private BooleanProperty disableRegisterButtonProp = new SimpleBooleanProperty(false);
  private ModelManager modelManager;

  /**
   * Constructs the ViewModel and binds button state and message label.
   *
   * @param modelManager the model manager to handle registration logic
   */
  public RegisterVM(ModelManager modelManager) {
    this.modelManager = modelManager;

    disableRegisterButtonProp.bind(usernameProp.isEmpty()
            .or(emailProp.isEmpty())
            .or(passwordProp.isEmpty())
            .or(repeatProp.isEmpty()));

    messageProp.bindBidirectional(modelManager.registerMessageProperty());
  }

  /**
   * Validates user input and triggers a registration request.
   * Displays appropriate validation error messages if input is invalid.
   */
  public void registerUser() {
    if (emailProp.get() == null || emailProp.get().isEmpty() || !emailProp.get().contains("@")) {
      messageProp.set("Incorrect email");
      return;
    }
    if (usernameProp.get() == null || usernameProp.get().isEmpty()) {
      messageProp.set("Username is empty");
      return;
    }
    if (passwordProp.get() == null || passwordProp.get().isEmpty()) {
      messageProp.set("Password is empty");
      return;
    }
    if (!repeatProp.get().equals(passwordProp.get())) {
      messageProp.set("Passwords do not match");
      return;
    }
    if (usernameProp.get().length() > 10) {
      messageProp.set("Username can maximum be 10 characters long.");
      return;
    }

    modelManager.registerUser(usernameProp.get(), emailProp.get(), passwordProp.get());
  }

  /** @return property indicating whether registration succeeded */
  public BooleanProperty registerSuccessProperty() {
    return modelManager.registerSuccessProperty();
  }

  /** @return property for the message label */
  public StringProperty registerMessageProperty() {
    return messageProp;
  }

  /** @return property for the password input */
  public StringProperty passwordPropriety() {
    return passwordProp;
  }

  /** @return property for the repeated password input */
  public StringProperty repeatPropriety() {
    return repeatProp;
  }

  /** @return property for the username input */
  public StringProperty userNamePropriety() {
    return usernameProp;
  }

  /** @return property for the email input */
  public StringProperty emailPropriety() {
    return emailProp;
  }

  /**
   * Returns a binding used to disable the register button if any required field is empty.
   *
   * @return binding expression controlling button state
   */
  public BooleanBinding disableRegisterButtonPropriety() {
    return usernameProp.isEmpty()
            .or(passwordProp.isEmpty())
            .or(emailProp.isEmpty())
            .or(repeatProp.isEmpty());
  }
}
