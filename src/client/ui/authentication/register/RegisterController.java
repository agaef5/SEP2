package client.ui.authentication.register;

import client.ui.common.MessageListener;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController implements MessageListener, Controller
{
  @FXML private TextField userNameInput;
  @FXML private TextField passwordInput;
  @FXML private TextField repeatPasswordInput;
  @FXML private Label messageLabel;
  @FXML private TextField emailInput;
  @FXML private Button buttonRegister;

  private RegisterVM viewModel;
  private MainWindowController mainWindowController;

  public RegisterController ()
  {
  }

  public void initialize(ViewModel registerVM)
  {
      viewModel = (RegisterVM) registerVM;

    userNameInput.textProperty()
        .bindBidirectional(viewModel.userNamePropriety());
    emailInput.textProperty().bindBidirectional(viewModel.emailPropriety());
    repeatPasswordInput.textProperty()
        .bindBidirectional(viewModel.messagePropriety());
    buttonRegister.disableProperty()
        .bind(viewModel.disableRegisterButtonPropriety());
  }

  public void onRegister ()
  {
    viewModel.registerUser();
  }

    @Override
    public void setWindowController(MainWindowController mainWindowController) {
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
