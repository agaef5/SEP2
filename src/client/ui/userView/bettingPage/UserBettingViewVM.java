package client.ui.userView.bettingPage;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.horse.HorseListResponse;

/**
 * ViewModel for the User Betting View with race countdown functionality.
 * Manages data and operations for the betting functionality.
 * Implements MessageListener to receive updates from the server.
 */
public class UserBettingViewVM implements MessageListener, ViewModel {

  /** Service for socket communication with the server */
  private final SocketService socketService;

  /** Client for communicating with horse-related server endpoints */
  private final HorsesClient horsesClient;

  /** JSON parser for handling server responses */
  private final Gson gson;

  /** Observable list containing horses available for betting */
  private final ObservableList<HorseDTO> horses = FXCollections.observableArrayList();

  /** The currently selected horse */
  private final ObjectProperty<HorseDTO> selectedHorse = new SimpleObjectProperty<>();

  /** The amount of money to bet */
  private final IntegerProperty betAmount = new SimpleIntegerProperty(0);

  /** The user's current balance */
  private final StringProperty balanceText = new SimpleStringProperty("Balance: $1000");

  /** The countdown timer for race start */
  private final StringProperty countdownText = new SimpleStringProperty("Race starting soon");

  /** Status message for user feedback */
  private final StringProperty statusMessage = new SimpleStringProperty("");

  /** Property to indicate if a bet is valid */
  private final BooleanProperty betValid = new SimpleBooleanProperty(false);

  /** Property to indicate if betting UI is locked (after placing a bet) */
  private final BooleanProperty uiLocked = new SimpleBooleanProperty(false);

//TODO connect actual user balance to this
  private int balanceValue = 1000; // Default starting balance

  /**
   * Constructs the ViewModel with necessary dependencies and initializes data.
   *
   * @param horsesClient Client for horse-related server operations
   * @param socketService Service for socket communication with the server
   */
  public UserBettingViewVM(HorsesClient horsesClient, SocketService socketService) {
    this.horsesClient = horsesClient;
    this.socketService = socketService;
    this.gson = new Gson();

    // Register this ViewModel as a listener for socket messages
    this.socketService.addListener(this);

    // Request the list of horses from the server
    refreshHorseList();

    // Set up validation for bet placing
    setupValidation();
  }

  /**
   * Sets up validation logic for bet placement.
   * A bet is valid if a horse is selected and the bet amount is between 1 and the current balance.
   */
  private void setupValidation() {
    // Monitor changes to selectedHorse and betAmount
    selectedHorse.addListener((obs, oldVal, newVal) -> validateBet());
    betAmount.addListener((obs, oldVal, newVal) -> validateBet());
  }

  /**
   * Validates if the current bet selection is valid.
   * Updates the betValid property accordingly.
   */
  private void validateBet() {
    boolean valid = selectedHorse.get() != null &&
            betAmount.get() > 0 &&
            betAmount.get() <= balanceValue &&
            !uiLocked.get(); // Cannot place bet if UI is locked
    betValid.set(valid);
  }

  /**
   * Requests the current horse list from the server.
   * This will trigger an update via the MessageListener when the response is received.
   */
  private void refreshHorseList() {
    horsesClient.getHorseList();
  }

  /**
   * Gets the observable list containing all horses.
   * @return Observable list of all horses
   */
  public ObservableList<HorseDTO> getHorses() {
    return horses;
  }

  /**
   * Gets the selected horse property.
   * @return Property containing the selected horse
   */
  public ObjectProperty<HorseDTO> selectedHorseProperty() {
    return selectedHorse;
  }

  /**
   * Gets the bet amount property.
   * @return Property containing the bet amount
   */
  public IntegerProperty betAmountProperty() {
    return betAmount;
  }

  /**
   * Gets the balance text property.
   * @return Property containing the formatted balance text
   */
  public StringProperty balanceTextProperty() {
    return balanceText;
  }

  /**
   * Gets the countdown text property.
   * @return Property containing the countdown text
   */
  public StringProperty countdownTextProperty() {
    return countdownText;
  }

  /**
   * Gets the status message property.
   * @return Property containing status messages
   */
  public StringProperty statusMessageProperty() {
    return statusMessage;
  }

  /**
   * Gets the bet valid property.
   * @return Property indicating if the bet is valid
   */
  public BooleanProperty betValidProperty() {
    return betValid;
  }

  /**
   * Gets the UI locked property.
   * @return Property indicating if the betting UI is locked
   */
  public BooleanProperty uiLockedProperty() {
    return uiLocked;
  }

  /**
   * Increases the bet amount by 100.
   */
  public void increaseBet() {
    if (!uiLocked.get()) {
      betAmount.set(betAmount.get() + 100);
    }
  }

  /**
   * Decreases the bet amount by 100, but not below 0.
   */
  public void decreaseBet() {
    if (!uiLocked.get()) {
      int newAmount = Math.max(0, betAmount.get() - 100);
      betAmount.set(newAmount);
    }
  }

  /**
   * Places a bet on the selected horse.
   * Updates the user's balance, locks the UI, and starts the countdown.
   *
   * @return true if the bet was successfully placed, false otherwise
   */
  public boolean placeBet() {
    if (!betValid.get() || uiLocked.get()) {
      statusMessage.set("Invalid bet: Please select a horse and valid amount");
      return false;
    }

    // TODO send this to server
    // For now, we'll just update the local balance
    balanceValue -= betAmount.get();
    balanceText.set("Balance: $" + balanceValue);

    // Lock the UI
    uiLocked.set(true);

    // Show confirmation message
    String horseName = selectedHorse.get().name();
    statusMessage.set("Bet of $" + betAmount.get() + " placed on " + horseName);

    // Start the countdown timer
    startCountdown();

    return true;
  }

  /**
   * Starts the countdown timer for the race.
   * Updates the countdown text every second until the race starts.
   */
  private void startCountdown() {
    //TODO create somthing like this
  }
  /**
   *
   * Updates the horse list with data received from the server.
   * Updates must be performed on the JavaFX application thread.
   *
   * @param horseListResponse Response object containing the list of horses
   */
  private void updateHorseList(HorseListResponse horseListResponse) {
    if (horseListResponse == null) return;

    Platform.runLater(() -> {
      horses.setAll(horseListResponse.horseList());

      // Select the first horse by default if available
      if (!horses.isEmpty() && selectedHorse.get() == null) {
        selectedHorse.set(horses.get(0));
      }
    });
  }

  /**
   * Handles messages received from the server via the socket connection.
   * Processes different message types and updates the ViewModel state accordingly.
   *
   * @param type The type of message received
   * @param payload The JSON payload containing the message data
   */
  @Override
  public void update(String type, String payload) {
    if ("getHorseList".equals(type)) {
      HorseListResponse horseListResponse = gson.fromJson(payload, HorseListResponse.class);
      updateHorseList(horseListResponse);
    }
    // Add more message handlers as needed, such as race updates and results
  }

  /**
   * Sets the RaceDTO to display race-specific information.
   * This can be called when this ViewModel is created to provide race context.
   *
   * @param race The race to display information for
   */
  public void setRace(RaceDTO race) {
    if (race != null) {
      // Update UI with race information
      countdownText.set("Race: " + race.name() + " - Place your bets!");
    }
  }

  /**
   * Cleans up resources when the ViewModel is no longer needed.
   * Should be called when the view is closed.
   */
  public void cleanup() {
     // Remove listener from socket service
    socketService.removeListener(this);
  }
}