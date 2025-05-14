package client.ui.authentication.login;

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
import shared.DTO.UserDTO;
import shared.loginRegister.LoginRespond;

public class LoginController implements MessageListener, Controller
{
  public Button createNewAccountB;
  @FXML private TextField usernameNameInput;
  @FXML private TextField passwordInput;
  @FXML private Text messageLabel;
  @FXML private Button buttonLogin;

  private LoginVM viewModel;
  private MainWindowController mainWindowController;
  private Gson gson = new Gson();

    public LoginController ()
  {
  }

  public void initialize (ViewModel loginVM)
  {
    this.viewModel = (LoginVM) loginVM;

    usernameNameInput.textProperty()
        .bindBidirectional(viewModel.userNamePropriety());
    passwordInput.textProperty()
        .bindBidirectional(viewModel.passwordPropriety());

    messageLabel.textProperty()
            .bindBidirectional(viewModel.messageProperty());
    buttonLogin.disableProperty().bind(viewModel.disableLoginButtonPropriety());
  }

  public void onLogin () {
    viewModel.loginUser();
  }

  public void onBack()
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
      System.out.println("Message received: " + type);
      if (type.equals("login")) {
        LoginRespond loginRespond = gson.fromJson(payload, LoginRespond.class);
        if (loginRespond.message().equals("success")) handleLogin(loginRespond);
      }
    }

    public void handleLogin(LoginRespond loginRespond){
      if(!loginRespond.message().equals("success")) return;
      if(loginRespond.payload() == null){
        Exception exception = new IllegalArgumentException();
        ErrorHandler.handleError(exception, "Problems with logging in");
        return;
      }

      UserDTO userDTO = gson.fromJson(gson.toJson(loginRespond.payload()), UserDTO.class);;
      mainWindowController.authorizeUser(userDTO);
    }
}
