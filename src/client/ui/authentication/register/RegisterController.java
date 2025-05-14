package client.ui.authentication.register;

import client.ui.common.MessageListener;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import client.ui.util.ErrorHandler;
import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import shared.DTO.UserDTO;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRespond;

public class RegisterController implements MessageListener, Controller {
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
  private Gson gson;

  public RegisterController() {
  }

  public void initialize(ViewModel registerVM) {
    viewModel = (RegisterVM) registerVM;
    gson = new Gson();

    userNameInput.textProperty().bindBidirectional(viewModel.userNamePropriety());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordPropriety());
    repeatPasswordInput.textProperty().bindBidirectional(viewModel.repeatPropriety());
    emailInput.textProperty().bindBidirectional(viewModel.emailPropriety());
    buttonRegister.disableProperty().bind(viewModel.disableRegisterButtonPropriety());
  }

  public void onRegister() {
    viewModel.registerUser();
  }

  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    if (mainWindowController != null) {
      this.mainWindowController = mainWindowController;
    }
  }

  @Override
  public void update(String type, String payload) {
    System.out.println("Message received: " + type);
    if (type.equals("register")) {
      RegisterRespond registerRespond = gson.fromJson(payload, RegisterRespond.class);
      if (registerRespond.message().equals("success")) handleRegister(registerRespond);
    }
  }

  public void handleRegister(RegisterRespond registerRespond) {
    if (registerRespond.payload() == null) {
      Exception exception = new IllegalArgumentException();
      ErrorHandler.handleError(exception, "Problems with logging in");
      return;
    }

    UserDTO userDTO = gson.fromJson(gson.toJson(registerRespond.payload()), UserDTO.class);
    mainWindowController.authorizeUser(userDTO);
  }
}
