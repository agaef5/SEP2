package client.ui.authentication.login;

import client.modelManager.ModelManager;
import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * Controller for the Login view.
 *
 * Handles user input for login, binds UI components to the ViewModel,
 * and reacts to login success by navigating to the authorized area of the app.
 */
public class LoginController implements Controller {

  @FXML public Button createNewAccountB;
  @FXML private TextField usernameNameInput;
  @FXML private TextField passwordInput;
  @FXML private Text messageLabel;
  @FXML private Button buttonLogin;
  private LoginVM viewModel;
  private MainWindowController mainWindowController;

  /**
   * Initializes the login form with bindings to the ViewModel and sets up UI event handlers.
   *
   * @param loginVM the ViewModel that holds the login logic and data
   */
  public void initialize(ViewModel loginVM) {
    this.viewModel = (LoginVM) loginVM;

    // Bind input fields to ViewModel properties
    usernameNameInput.textProperty().bindBidirectional(viewModel.usernameProperty());
    passwordInput.textProperty().bindBidirectional(viewModel.passwordProperty());

    // Bind login feedback message
    messageLabel.textProperty().bind(viewModel.loginMessageProperty());

    // Trigger navigation when login is successful
    viewModel.loginSuccessProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        mainWindowController.authorizeUser();
      }
    });

    // Set up login button state and action
    buttonLogin.disableProperty().bind(viewModel.disableLoginButtonProperty());
    buttonLogin.setOnAction(e -> viewModel.loginUser());

    // Set up register navigation button
    createNewAccountB.setOnAction(e -> mainWindowController.loadRegisterPage());
  }

  /**
   * Assigns the main window controller used for view navigation.
   *
   * @param mainWindowController the shared main window controller
   */
  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.mainWindowController = mainWindowController;
  }
}
