package client.ui.authentication.login;

import client.networking.SocketService;
import client.ui.MessageListener;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController implements MessageListener, Controller
{
  @FXML private TextField usernameNameInput;
  @FXML private TextField passwordInput;
  @FXML private Label messageLabel;
  @FXML private Button buttonLogin;

  private LoginVM viewModel;
    private MainWindowController mainWindowController;

    public LoginController ()
  {
  }

  public void initialize (ViewModel loginVM)
  {
      this.viewModel = (LoginVM) loginVM;

    usernameNameInput.textProperty()
        .bindBidirectional(viewModel.userNamePropriety());
    messageLabel.textProperty()
        .bindBidirectional(viewModel.userNamePropriety());
    passwordInput.textProperty()
        .bindBidirectional(viewModel.passwordPropriety());
    buttonLogin.disableProperty().bind(viewModel.disableLoginButtonPropriety());
  }

  public void onLogin () {
//            TODO: authenticate user, to check if its user or admin in order to navigate further!!!!
        mainWindowController.loadUserLandingPage();
        mainWindowController.loadAdminPanel();
  }

  public void onBack ()
  {

  }

  @Override public void setWindowController(MainWindowController mainWindowController) {
        if(mainWindowController != null){
            this.mainWindowController = mainWindowController;
        }
    }

    @Override
    public void update(String type, String payload) {

    }
}
