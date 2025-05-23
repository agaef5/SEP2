package client.ui.authentication.login;


import client.modelManager.ModelManager;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


public class LoginController implements Controller
{
  @FXML private TextField usernameNameInput;
  @FXML private TextField passwordInput;
  @FXML private Text messageLabel;
  @FXML private Button buttonLogin;
  @FXML public Button createNewAccountB;
  private LoginVM viewModel;
  private MainWindowController mainWindowController;


  public void initialize(ViewModel loginVM)
  {
    this.viewModel = (LoginVM) loginVM;

    usernameNameInput.textProperty().bindBidirectional(viewModel.usernameProperty());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordProperty());
    messageLabel.textProperty().bind(viewModel.messageProperty());

    messageLabel.textProperty().bind(viewModel.loginMessageProperty());

    // Listen for login success to trigger navigation
    viewModel.loginSuccessProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        mainWindowController.authorizeUser();
      }
    });

    buttonLogin.disableProperty().bind(viewModel.disableLoginButtonProperty());
    buttonLogin.setOnAction(e -> viewModel.loginUser());
    createNewAccountB.setOnAction(e -> mainWindowController.loadRegisterPage());
  }


  @Override public void setWindowController(MainWindowController mainWindowController) {
    this.mainWindowController = mainWindowController;
  }
}
