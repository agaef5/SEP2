package viewModel.loginAccount;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginAccount_Controller
{
  @FXML
  private TextField username;
  @FXML
  private TextField errorMessage_username;
  @FXML
  private TextField password;
  @FXML
  private TextField errorMessage_password;
  @FXML
  private Button confirmLoginB;
  @FXML
  private Button createNewAccountB;
  
  private LoginAccount_VM loginAccount_vm;
  
  public LoginAccount_Controller (LoginAccount_VM loginAccount_vm)
  {
    this.loginAccount_vm = loginAccount_vm;
  }
  public void initialize(){
    username.textProperty ().bindBidirectional (loginAccount_vm.getUsername());
    errorMessage_username.textProperty().bindBidirectional(loginAccount_vm.getErrorMessage_username());
    password.textProperty ().bindBidirectional (loginAccount_vm.getPassword());
    errorMessage_password.textProperty().bindBidirectional(loginAccount_vm.getErrorMessage_password());
    confirmLoginB.disableProperty ().bind(loginAccount_vm.getConfirmLogin());
    createNewAccountB.disableProperty ().bind(loginAccount_vm.getCreateNewAccount());
  }

  public void loginUsername(){
    loginAccount_vm.getUsername();
  }
  public void loginPassword(){
    loginAccount_vm.getPassword();
  }
  public void confirmButton(){
    loginAccount_vm.getConfirmLogin();
  }
  public void createAccountButton(){
    loginAccount_vm.getCreateNewAccount();
  }
  
}
