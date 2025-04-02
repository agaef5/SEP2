package viewModel.loginAccount;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginAccount_Controller
{
  @FXML
  private TextField username;
  @FXML
  private TextField password;
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
    username.textProperty ().bindBidirectional (loginAccount_vm.username ());
    password.textProperty ().bindBidirectional (loginAccount_vm.password ());
    confirmLoginB.disableProperty ().bind(loginAccount_vm.confirmLogin());
    createNewAccountB.disableProperty ().bind(loginAccount_vm.createNewAccountButton ());
  }

  public void loginUsername(){
    loginAccount_vm.username();
  }
  public void loginPassword(){
    loginAccount_vm.password();
  }
  public void confirmButton(){
    loginAccount_vm.confirmLogin();
  }
  public void createAccountButton(){
    loginAccount_vm.createNewAccountButton();
  }
  
}
