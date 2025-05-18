package client.ui.authentication.login;


import client.modelManager.ModelManager;
import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.beans.property.*;
import shared.loginRegister.LoginRequest;


public class LoginVM implements ViewModel
{
    private final ModelManager model;
    private final StringProperty usernameProp = new SimpleStringProperty();
    private final StringProperty passwordProp = new SimpleStringProperty();
    private final StringProperty messageProp = new SimpleStringProperty();
    private final BooleanProperty disableLoginButtonProp = new SimpleBooleanProperty(
            false);
    private final BooleanProperty createNewUser = new SimpleBooleanProperty(true);




    public LoginVM (ModelManager model)
    {
        this.model = model;
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
        model.loginUser(username, password);
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


    public BooleanProperty disableLoginButtonProperty() {
        return disableLoginButtonProp;
    }


    public BooleanProperty createNewUserProperty() {
        return createNewUser;
    }
    public void clearFields(){
        usernameProp.set("");
        passwordProp.set("");
        messageProp.set("");
    }
}
