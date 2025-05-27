package client.ui.userView.bettingPage;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import client.ui.util.ErrorHandler;
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
 *
 * Manages user interactions for viewing horse details and placing bets.
 * Binds UI components to the ViewModel and listens for transitions to the game or landing pages.
 */
public class UserBettingViewController implements Controller {

  @FXML private TableView<HorseDTO> horseTableView;
  @FXML private TableColumn<HorseDTO, String> nameColumn;
  @FXML private TableColumn<HorseDTO, Integer> minSpeedColumn;
  @FXML private TableColumn<HorseDTO, Integer> maxSpeedColumn;
  @FXML private Button plusButton;
  @FXML private Button minusButton;
  @FXML private TextField betAmount;
  @FXML private Label balance;
  @FXML private Label countDownLabel;
  @FXML private Button placeBetButton;

  private UserBettingViewVM viewModel;
  private MainWindowController mainWindowController;

  /** Default constructor required by FXML */
  public UserBettingViewController() {}

  /**
   * Initializes the controller with bindings and event setup.
   *
   * @param userBettingVM the ViewModel handling business logic and state
   */
  @Override
  public void initialize(ViewModel userBettingVM) {
    this.viewModel = (UserBettingViewVM) userBettingVM;

    // Setup table columns
    nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));
    minSpeedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().speedMin()).asObject());
    maxSpeedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().speedMax()).asObject());

    horseTableView.setItems(viewModel.getHorses());
    viewModel.getHorses().addListener((ListChangeListener<HorseDTO>) change -> {
      System.out.println("Horses list changed. New size: " + viewModel.getHorses().size());
    });

    // Bind selected horse
    horseTableView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> viewModel.selectedHorseProperty().set(newVal)
    );

    // Bind balance and countdown
    balance.textProperty().bind(viewModel.balanceInfoProperty().asString("$%d"));
    countDownLabel.textProperty().bind(viewModel.countdownTextProperty());

    // Bind bet input
    Bindings.bindBidirectional(betAmount.textProperty(), viewModel.betAmountProperty(), new IntegerStringConverter());

    // Button actions
    plusButton.setOnAction(e -> viewModel.increaseBet());
    minusButton.setOnAction(e -> viewModel.decreaseBet());
    placeBetButton.setOnAction(e -> handlePlaceBet());

    // Lock UI if betting not allowed
    horseTableView.disableProperty().bind(viewModel.uiLockedProperty());
    betAmount.disableProperty().bind(viewModel.uiLockedProperty());
    plusButton.disableProperty().bind(viewModel.uiLockedProperty());
    minusButton.disableProperty().bind(viewModel.uiLockedProperty());
    placeBetButton.disableProperty().bind(viewModel.betValidProperty().not().or(viewModel.uiLockedProperty()));

    // Handle navigation to game view when race starts
    viewModel.navigateToGameViewProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        navigateToGameView();
        viewModel.resetGameViewNavigation();
      }
    });

    // Handle fallback navigation if user missed betting
    viewModel.getNavigateToUserLandingPageProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        navigateToUserLandingPage();
        viewModel.resetGameViewNavigation();
      }
    });

    // Setup cleanup on window close
    setupWindowCloseHandler();
  }

  /**
   * Handles placing a bet when the button is clicked.
   */
  private void handlePlaceBet() {
    boolean success = viewModel.placeBet();
    if (success) {
      // UI locks automatically in ViewModel
    }
  }

  /**
   * Navigates to the game view for the currently selected race.
   */
  private void navigateToGameView() {
    RaceDTO selectedRace = viewModel.getSelectedRace();
    if (selectedRace != null) {
      mainWindowController.loadGameView(selectedRace);
    }
  }

  /**
   * Navigates back to the user landing page if betting window expired.
   */
  private void navigateToUserLandingPage() {
    ErrorHandler.showAlert("No bet placed", "No bet was placed and race has already started. Too late to place a bet.");
    mainWindowController.loadUserLandingPage();
  }

  /**
   * Sets up a handler to navigate back on window close.
   */
  private void setupWindowCloseHandler() {
    betAmount.sceneProperty().addListener((obs, oldScene, newScene) -> {
      if (newScene != null) {
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
   * Sets the main window controller to allow navigation from this view.
   *
   * @param mainWindowController the main app controller
   */
  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    if (mainWindowController != null) {
      this.mainWindowController = mainWindowController;
    }
  }

  /**
   * Converter for two-way binding between Integer properties and TextField.
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
}
