package client.ui.authentication.login;


import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.property.*;


public class LoginVM implements ViewModel
{
    private final ModelManager modelManager;
    private final StringProperty usernameProp = new SimpleStringProperty();
    private final StringProperty passwordProp = new SimpleStringProperty();
    private final StringProperty messageProp = new SimpleStringProperty();
    private final BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(
            false);

    public LoginVM (ModelManager modelManager)
    {
        this.modelManager = modelManager;
        disableLoginButtonProp.bind(
                usernameProp.isEmpty().or(passwordProp.isEmpty())
        );
    }

    public void loginUser(){
        String username = usernameProp.get();
        String password = passwordProp.get();

        if (username.isEmpty() || password.isEmpty()){
            messageProp.set("Username is empty");
        }
        if ( passwordProp.get() == null || passwordProp.get().isEmpty() )
        {
            messageProp.set("Password is empty");
        }
        if ( usernameProp.get() == null || usernameProp.get().isEmpty() )
        {
            messageProp.set("Username is empty");
        }
        modelManager.loginUser(username, password);
        clearFields();
    }

    public StringProperty usernameProperty() {
        return usernameProp;
    }


    public StringProperty passwordProperty() {
        return passwordProp;
    }


    public StringProperty messageProperty() {
        return messageProp;
    }


    public BooleanProperty loginSuccessProperty() {
        return modelManager.loginSuccessProperty();
    }


    public StringProperty loginMessageProperty() {
        return modelManager.loginMessageProperty();
    }


    public BooleanProperty disableLoginButtonProperty() {
        return disableLoginButtonProp;
    }

    public void clearFields(){
        usernameProp.set("");
        passwordProp.set("");
        messageProp.set("");

    }
}
