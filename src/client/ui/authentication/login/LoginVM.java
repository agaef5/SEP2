package client.ui.authentication.login;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.property.*;

/**
 * ViewModel for the Login view.
 *
 * Manages login form fields, button state, and interaction with the ModelManager for authentication.
 */
public class LoginVM implements ViewModel {

    private final ModelManager modelManager;
    private final StringProperty usernameProp = new SimpleStringProperty();
    private final StringProperty passwordProp = new SimpleStringProperty();
    private final StringProperty messageProp = new SimpleStringProperty();
    private final BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(false);

    /**
     * Constructs the LoginVM with the provided ModelManager.
     * Automatically disables the login button if username or password fields are empty.
     *
     * @param modelManager the shared model manager for handling login logic
     */
    public LoginVM(ModelManager modelManager) {
        this.modelManager = modelManager;

        disableLoginButtonProp.bind(
                usernameProp.isEmpty().or(passwordProp.isEmpty())
        );
    }

    /**
     * Attempts to log in using the current username and password fields.
     * Performs basic input validation and clears fields after sending the login request.
     */
    public void loginUser() {
        String username = usernameProp.get();
        String password = passwordProp.get();

        if (username.isEmpty() || password.isEmpty()) {
            messageProp.set("Username is empty");
        }
        if (passwordProp.get() == null || passwordProp.get().isEmpty()) {
            messageProp.set("Password is empty");
        }
        if (usernameProp.get() == null || usernameProp.get().isEmpty()) {
            messageProp.set("Username is empty");
        }

        modelManager.loginUser(username, password);
        clearFields();
    }

    /** @return the property holding the entered username */
    public StringProperty usernameProperty() {
        return usernameProp;
    }

    /** @return the property holding the entered password */
    public StringProperty passwordProperty() {
        return passwordProp;
    }

    /** @return the property holding local form error messages */
    public StringProperty messageProperty() {
        return messageProp;
    }

    /** @return the property indicating login success (from the model) */
    public BooleanProperty loginSuccessProperty() {
        return modelManager.loginSuccessProperty();
    }

    /** @return the property holding the login response message (from the model) */
    public StringProperty loginMessageProperty() {
        return modelManager.loginMessageProperty();
    }

    /** @return property used to disable the login button based on form state */
    public BooleanProperty disableLoginButtonProperty() {
        return disableLoginButtonProp;
    }

    /**
     * Clears all input and message fields in the form.
     */
    public void clearFields() {
        usernameProp.set("");
        passwordProp.set("");
        messageProp.set("");
    }
}
