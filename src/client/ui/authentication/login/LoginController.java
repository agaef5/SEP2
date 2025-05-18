package client.ui.authentication.login;


import client.modelManager.ModelManager;
import client.ui.common.MessageListener;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import client.ui.util.ErrorHandler;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import shared.DTO.RaceState;
import shared.DTO.UserDTO;
import shared.loginRegister.LoginRespond;


public class LoginController implements Controller
{
  @FXML public Button createNewAccountB;
  ModelManager modelManager;
  @FXML private TextField usernameNameInput;
  @FXML private TextField passwordInput;
  @FXML private Text messageLabel;
  @FXML private Button buttonLogin;
  private LoginVM viewModel;
  private MainWindowController mainWindowController;


  public void initialize (ViewModel loginVM)
  {
    this.viewModel = (LoginVM) loginVM;
    usernameNameInput.textProperty().bindBidirectional(viewModel.usernameProperty());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordProperty());
    messageLabel.textProperty().bind(viewModel.messageProperty());
    buttonLogin.disableProperty().bind(viewModel.disableLoginButtonProperty());
    createNewAccountB.disableProperty().bindBidirectional(viewModel.createNewUserProperty());


    buttonLogin.setOnAction(e -> viewModel.loginUser());
    createNewAccountB.setOnAction(e -> mainWindowController.loadRegisterPage());
  }


  @Override public void setWindowController(MainWindowController mainWindowController) {
    this.mainWindowController = mainWindowController;
  }
}
