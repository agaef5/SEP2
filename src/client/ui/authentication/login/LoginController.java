package client.ui.authentication.login;

import client.ui.common.MessageListener;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements MessageListener, Controller
{
  public Button createNewAccountB;
  @FXML private TextField usernameNameInput;
  @FXML private TextField passwordInput;
//  @FXML private Label messageLabel;
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
//    messageLabel.textProperty()
//        .bindBidirectional(viewModel.userNamePropriety());
    passwordInput.textProperty()
        .bindBidirectional(viewModel.passwordPropriety());
    buttonLogin.disableProperty().bind(viewModel.disableLoginButtonPropriety());
  }

  public void onLogin () {
    //    TODO: only for test purposes - delete later
    if(usernameNameInput.getText().equals("admin")){
      mainWindowController.authenticateAdmin(true);
      mainWindowController.loadAdminPanel();
    }else{
      mainWindowController.loadUserLandingPage();
    }
//    _______________________________________________
    viewModel.loginUser();
  }

  public void onBack ()
  {
    mainWindowController.loadRegisterPage();
  }

  @Override public void setWindowController(MainWindowController mainWindowController) {
        if(mainWindowController != null){
            this.mainWindowController = mainWindowController;
        }
    }

    @Override
    public void update(String type, String payload) {
      if(mainWindowController.authenticateAdmin()){
        mainWindowController.loadAdminPanel();
      }else {
        mainWindowController.loadUserLandingPage();
      }
    }
}
