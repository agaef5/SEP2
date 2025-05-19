package client.ui.userView.bettingPage;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;

/**
 * Controller for the User Betting View.
 * Manages user interactions for placing bets on horses.
 */
public class UserBettingViewController implements Controller {

  /** TableView displaying all available horses */
  @FXML private TableView<HorseDTO> horseTableView;

  /** Column for horse names */
  @FXML private TableColumn<HorseDTO, String> nameColumn;

  /** Column for minimum speed values */
  @FXML private TableColumn<HorseDTO, Integer> minSpeedColumn;

  /** Column for maximum speed values */
  @FXML private TableColumn<HorseDTO, Integer> maxSpeedColumn;

  /** Button to increase bet amount */
  @FXML private Button plusButton;

  /** Button to decrease bet amount */
  @FXML private Button minusButton;

  /** Text field for entering bet amount */
  @FXML private TextField betAmount;

  /** Label displaying user's balance */
  @FXML private Label balance;

  /** Label displaying countdown to race start */
  @FXML private Label countDownLabel;

  /** Button to place a bet */
  @FXML private Button placeBetButton;

  /** ViewModel that provides data and operations for this view */
  private UserBettingViewVM viewModel;

  private MainWindowController mainWindowController;

  /**
   * Default empty constructor required by FXML loader.
   */
  public UserBettingViewController() {}

  /**
   * Initializes the controller with the provided ViewModel.
   * Sets up bindings between UI components and ViewModel properties,
   * configures cell factories for the table view, and attaches event handlers.
   *
   * @param userBettingVM The ViewModel that provides data and operations for this view
   */
  @Override
  public void initialize(ViewModel userBettingVM) {
    this.viewModel = (UserBettingViewVM) userBettingVM;

    // Add debug statement
    System.out.println("Initializing UserBettingViewController");

    // Configure table columns
    nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().name()));
    minSpeedColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().speedMin()).asObject());
    maxSpeedColumn.setCellValueFactory(cellData ->
            new SimpleIntegerProperty(cellData.getValue().speedMax()).asObject());

    // Bind table data to ViewModel
    horseTableView.setItems(viewModel.getHorses());

    // Add debug listener to track changes in the horses list
    viewModel.getHorses().addListener((ListChangeListener<HorseDTO>) change -> {
      System.out.println("Horses list changed. New size: " + viewModel.getHorses().size());
    });

    // Bind selected horse to ViewModel
    horseTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> viewModel.selectedHorseProperty().set(newVal)
    );

    // Bind text properties
    balance.textProperty().bind(viewModel.balanceInfoProperty().asString("$%d"));
    countDownLabel.textProperty().bind(viewModel.countdownTextProperty());

    // Bidirectional binding for bet amount
    Bindings.bindBidirectional(betAmount.textProperty(), viewModel.betAmountProperty(),
            new IntegerStringConverter());

    // Set up button actions
    plusButton.setOnAction(e -> viewModel.increaseBet());
    minusButton.setOnAction(e -> viewModel.decreaseBet());
    placeBetButton.setOnAction(e -> handlePlaceBet());

    // Bind disabled properties based on UI locked state
    horseTableView.disableProperty().bind(viewModel.uiLockedProperty());
    betAmount.disableProperty().bind(viewModel.uiLockedProperty());
    plusButton.disableProperty().bind(viewModel.uiLockedProperty());
    minusButton.disableProperty().bind(viewModel.uiLockedProperty());

    // Place bet button is disabled when bet is invalid OR UI is locked
    placeBetButton.disableProperty().bind(
            viewModel.betValidProperty().not().or(viewModel.uiLockedProperty())
    );

    // Add listener for auto-navigation to game view
    viewModel.navigateToGameViewProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        navigateToGameView();
        viewModel.resetGameViewNavigation();
      }
    });

    viewModel.getNavigateToUserLandingPageProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        navigateToUserLandingPage();
        viewModel.resetGameViewNavigation();
      }
    });

    // Add window close handler to cleanup resources
    setupWindowCloseHandler();
  }

  /**
   * Sets up a handler to clean up resources when the window is closed.
   */
  private void setupWindowCloseHandler() {
    // We need to wait until the scene is set
    betAmount.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene != null) {
        // Get the window and add a close request handler
        Window window = newScene.getWindow();
        if (window != null) {
          window.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            mainWindowController.loadUserLandingPage();
          });
        }
      }
    });
  }

  /**
   * Handles the place bet button click event.
   * Delegates to the ViewModel to process the bet.
   */
  private void handlePlaceBet() {
    boolean success = viewModel.placeBet();
    if (success) {
      // The UI will be locked by the ViewModel
      // No need to clear fields as they'll be reset when the race ends
    }
  }

  /**
   * Handles navigation to the game view when the race starts.
   */
  private void navigateToGameView() {
    RaceDTO selectedRace = viewModel.getSelectedRace();
    if (selectedRace != null) {
      mainWindowController.loadGameView(selectedRace);
    }
  }

  private void navigateToUserLandingPage(){
    showAlert("No bet placed", "No bet was placed and race has " +
            "already started.Too late to place a bet.");
    mainWindowController.loadUserLandingPage();
  }

  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    if(mainWindowController != null) this.mainWindowController = mainWindowController;
  }

  /**
   * Custom converter for binding between Integer property and String text field.
   */
  private static class IntegerStringConverter extends javafx.util.StringConverter<Number> {
    @Override
    public String toString(Number number) {
      return number == null ? "" : number.toString();
    }

    @Override
    public Number fromString(String string) {
      try {
        return string.isEmpty() ? 0 : Integer.parseInt(string);
      } catch (NumberFormatException e) {
        return 0;
      }
    }
  }

  /**
   * Displays an alert dialog with the specified title and content.
   * Used for showing validation errors and other notifications.
   *
   * @param title The title of the alert dialog
   * @param content The content message to display in the alert
   */
  private void showAlert(String title, String content)
  {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}