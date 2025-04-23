//package client.ui.authentication.register;
//
//import client.ui.common.Controller;
//import javafx.fxml.FXML;
//import javafx.scene.control.*;
//
//public class RegisterController implements Controller
//{
//  @FXML private TextField userNameInput;
//  @FXML private TextField passwordInput;
//  @FXML private TextField repeatPasswordInput;
//  @FXML private Label messageLabel;
//  @FXML private TextField emailInput;
//  @FXML private Button buttonRegister;
//
//  private RegisterVM viewModel;
//
//  public RegisterController ( RegisterVM viewModel )
//  {
//    this.viewModel = viewModel;
//  }
//
//  public void initialize ()
//  {
//    userNameInput.textProperty()
//        .bindBidirectional(viewModel.userNamePropriety());
//    emailInput.textProperty().bindBidirectional(viewModel.emailPropriety());
//    repeatPasswordInput.textProperty()
//        .bindBidirectional(viewModel.messagePropriety());
//    buttonRegister.disableProperty()
//        .bind(viewModel.disableRegisterButtonPropriety());
//  }
//
//  public void onRegister ()
//  {
//
//  }
//
//  public void onBack ()
//  {
//
//  }
//
//  @Override public void changePage ( Controller page )
//  {
//
//  }
//}
