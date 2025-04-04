package viewModel.createAccount;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CreateAccount_Controller
{
  @FXML
  private TextField username;
  @FXML
  private TextField errorMessage_username;
  @FXML
  private TextField email;
  @FXML
  private TextField errorMessage_email;
  @FXML
  private TextField password;
  @FXML
  private TextField confirmPassword;
  @FXML
  private TextField errorMessage_password;
  @FXML
  private TextField errorMessage_passwordsDontMatch;
  @FXML
  private Button confirmCreateAccountButton;
  
  private CreateAccount_VM createAccount_vm;
  

  public CreateAccount_Controller(CreateAccount_VM createAccount_vm)
  {
    this.createAccount_vm = createAccount_vm;
  }
  public void initialize(){
    username.textProperty ().bindBidirectional (createAccount_vm.getUsername());
    errorMessage_username.textProperty().bindBidirectional(createAccount_vm.getErrorMessage_username());
    email.textProperty().bindBidirectional(createAccount_vm.getEmail());
    errorMessage_email.textProperty().bindBidirectional(createAccount_vm.getErrorMessage_email());
    password.textProperty ().bindBidirectional (createAccount_vm.getPassword());
    errorMessage_password.textProperty().bindBidirectional(createAccount_vm.getErrorMessage_password());
    confirmPassword.textProperty().bindBidirectional(createAccount_vm.getErrorMessage_passwordsDontMatch());
    confirmCreateAccountButton.disableProperty().bind(createAccount_vm.getConfirmCreateAccount());
  }
  public void createUsername(){
  
  }
  public void createPassword(){
  
  }
  public void confirmPassword (){
  
  }
  public void createEmail(){
  
  }
  
  public void confirmCreateAccountButton(){
  
  }
}
