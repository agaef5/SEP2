package client.ui.authentication.register;

import client.modelManager.ModelManager;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the Register view.
 *
 * Handles user input for registration, binds form fields to the ViewModel,
 * and navigates the user upon successful registration.
 */
public class RegisterController implements Controller {

  @FXML private TextField userNameInput;
  @FXML private TextField passwordInput;
  @FXML private TextField repeatPasswordInput;
  @FXML private Label messageLabel;
  @FXML private TextField emailInput;
  @FXML private Button buttonRegister;

  private RegisterVM viewModel;
  private MainWindowController mainWindowController;

  /**
   * Initializes the controller and binds UI components to ViewModel properties.
   * Sets up listeners and action handlers for the register button.
   *
   * @param registerVM the ViewModel handling registration logic and state
   */
  @Override
  public void initialize(ViewModel registerVM) {
    viewModel = (RegisterVM) registerVM;

    // Bind form input fields to ViewModel properties
    userNameInput.textProperty().bindBidirectional(viewModel.userNamePropriety());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordPropriety());
    repeatPasswordInput.textProperty().bindBidirectional(viewModel.repeatPropriety());
    emailInput.textProperty().bindBidirectional(viewModel.emailPropriety());

    // Bind message label to feedback from ViewModel
    messageLabel.textProperty().bindBidirectional(viewModel.registerMessageProperty());

    // Handle navigation after successful registration
    viewModel.registerSuccessProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        mainWindowController.authorizeUser();
      }
    });

    // Disable register button if fields are incomplete
    buttonRegister.disableProperty().bind(viewModel.disableRegisterButtonPropriety());

    // Handle register button click
    buttonRegister.setOnAction(e -> {
      viewModel.registerUser();
    });
  }

  /**
   * Sets the reference to the main window controller for view navigation.
   *
   * @param mainWindowController the main application window controller
   */
  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    if (mainWindowController != null) {
      this.mainWindowController = mainWindowController;
    }
  }
}
