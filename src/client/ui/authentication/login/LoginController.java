package client.ui.authentication.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController
{
  @FXML
  private TextField usernameNameInput;
  @FXML
  private TextField passwordInput;
  @FXML
  private Label messageLabel;
  @FXML
  private Button buttonLogin;
  
  private LoginVM viewModel;
  
  public LoginController (LoginVM loginAccount_vm)
  {
    this.viewModel = loginAccount_vm;
  }
  
  public void initialize(){
    usernameNameInput.textProperty ().bindBidirectional (viewModel.userNamePropriety());
    messageLabel.textProperty().bindBidirectional(viewModel.userNamePropriety());
    passwordInput.textProperty ().bindBidirectional (viewModel.passwordPropriety());
    buttonLogin.disableProperty ().bind(viewModel.disableLoginButtonPropriety());
  }
  public void onLogin(){
  
  }
  public void onBack(){
  
  }
}
