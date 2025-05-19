package client.ui.authentication.register;


import client.modelManager.ModelManager;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class RegisterController implements Controller {
  ModelManager modelManager;
  @FXML
  private TextField userNameInput;
  @FXML
  private TextField passwordInput;
  @FXML
  private TextField repeatPasswordInput;
  @FXML
  private Label messageLabel;
  @FXML
  private TextField emailInput;
  @FXML
  private Button buttonRegister;
  private RegisterVM viewModel;
  private MainWindowController mainWindowController;
  public void initialize(ViewModel registerVM) {
    viewModel = (RegisterVM) registerVM;


    userNameInput.textProperty().bindBidirectional(viewModel.userNamePropriety());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordPropriety());
    repeatPasswordInput.textProperty().bindBidirectional(viewModel.repeatPropriety());
    emailInput.textProperty().bindBidirectional(viewModel.emailPropriety());


    messageLabel.textProperty()
            .bindBidirectional(viewModel.messagePropriety());
    buttonRegister.disableProperty().bind(viewModel.disableRegisterButtonPropriety());


    buttonRegister.setOnAction(e -> {
      viewModel.registerUser();
      mainWindowController.loadLoginPage();
    });
  }


  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    if (mainWindowController != null) {
      this.mainWindowController = mainWindowController;
    }
  }


}
