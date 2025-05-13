package client.ui.userView.bettingPage;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.common.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.horse.HorseListResponse;

public class UserBettingViewVM implements MessageListener, ViewModel {

  // Service for socket communication with the server
  private final SocketService socketService;

  // Client for communicating with horse-related server endpoints
  private final HorsesClient horsesClient;

  // JSON parser for handling server responses
  private final Gson gson;

  // Observable list containing horses available for betting
  private final ObservableList<HorseDTO> horses = FXCollections.observableArrayList();

  // The currently selected horse
  private final ObjectProperty<HorseDTO> selectedHorse = new SimpleObjectProperty<>();

  // The amount of money to bet
  private final IntegerProperty betAmount = new SimpleIntegerProperty(0);

  // The user's current balance
  private final StringProperty balanceText = new SimpleStringProperty("Balance: $1000");

  // The countdown timer for race start
  private final StringProperty countdownText = new SimpleStringProperty("Race starting soon");

  // Status message for user feedback
  private final StringProperty statusMessage = new SimpleStringProperty("");

  // Property to indicate if a bet is valid
  private final BooleanProperty betValid = new SimpleBooleanProperty(false);

  // Property to indicate if betting UI is locked (after placing a bet)
  private final BooleanProperty uiLocked = new SimpleBooleanProperty(false);

  // The selected race for betting
  private final RaceDTO selectedRace;

  // User's balance value
  private int balanceValue = 1000; // Default starting balance

  // Constructor with race parameter
  public UserBettingViewVM(HorsesClient horsesClient, SocketService socketService, RaceDTO selectedRace) {
    this.horsesClient = horsesClient;
    this.socketService = socketService;
    this.gson = new Gson();
    this.selectedRace = selectedRace;

    System.out.println("UserBettingViewVM created with race: " +
            (selectedRace != null ? selectedRace.name() : "null"));
    System.out.println("Race has horses: " +
            (selectedRace != null && selectedRace.horses() != null ?
                    selectedRace.horses().size() : "0"));

    // Register this ViewModel as a listener for socket messages
    this.socketService.addListener(this);

    // Refresh the horse list based on selected race
    refreshHorseList();

    // Set up validation for bet placement
    setupValidation();

    // Update countdown text with race information if available
    updateRaceInfo();
  }

  // Overloaded constructor for when no race is selected
  // This should only be used when the betting page is accessed without selecting a race
  public UserBettingViewVM(HorsesClient horsesClient, SocketService socketService) {
    this(horsesClient, socketService, null);
  }

  // Updates race information in the UI
  private void updateRaceInfo() {
    if (selectedRace != null) {
      countdownText.set("Race: " + selectedRace.name() + " - Place your bets!");
    } else {
      countdownText.set("No race selected. Please return and select a race.");
    }
  }

  // Sets up validation logic for bet placement
  // A bet is valid if a horse is selected and the bet amount is between 1 and the current balance
  private void setupValidation() {
    // Monitor changes to selectedHorse and betAmount
    selectedHorse.addListener((obs, oldVal, newVal) -> validateBet());
    betAmount.addListener((obs, oldVal, newVal) -> validateBet());
  }

  // Validates if the current bet selection is valid
  // Updates the betValid property accordingly
  private void validateBet() {
    boolean valid = selectedHorse.get() != null &&
            betAmount.get() > 0 &&
            betAmount.get() <= balanceValue &&
            !uiLocked.get(); // Cannot place bet if UI is locked
    betValid.set(valid);
  }

  // Refreshes the horse list based on the selected race
  // If no race is selected, the horse list will be empty
  private void refreshHorseList() {
    System.out.println("Refreshing horse list in UserBettingViewVM");
    System.out.println("Selected race: " + (selectedRace != null ? selectedRace.name() : "null"));

    if (selectedRace != null && selectedRace.horses() != null && !selectedRace.horses().isEmpty()) {
      System.out.println("Using horses from selected race: " + selectedRace.horses().size() + " horses");
      for (HorseDTO horse : selectedRace.horses()) {
        System.out.println("  - " + horse.name());
      }

      Platform.runLater(() -> {
        horses.setAll(selectedRace.horses());

        // Select the first horse by default if available
        if (!horses.isEmpty() && selectedHorse.get() == null) {
          selectedHorse.set(horses.get(0));
        }

        System.out.println("Horses list updated with race horses. New size: " + horses.size());
        statusMessage.set(""); // Clear any status message
      });
    } else {
      System.out.println("No race selected or race has no horses. Showing empty horse list.");
      Platform.runLater(() -> {
        horses.clear(); // Show empty list if no race is selected
        selectedHorse.set(null);

        // Update status message
        if (selectedRace == null) {
          statusMessage.set("Please select a race to place bets.");
        } else {
          statusMessage.set("This race doesn't have any horses assigned to it.");
        }
      });
    }
  }

  // Gets the observable list containing available horses
  public ObservableList<HorseDTO> getHorses() {
    return horses;
  }

  // Gets the selected horse property
  public ObjectProperty<HorseDTO> selectedHorseProperty() {
    return selectedHorse;
  }

  // Gets the bet amount property
  public IntegerProperty betAmountProperty() {
    return betAmount;
  }

  // Gets the balance text property
  public StringProperty balanceTextProperty() {
    return balanceText;
  }

  // Gets the countdown text property
  public StringProperty countdownTextProperty() {
    return countdownText;
  }

  // Gets the status message property
  public StringProperty statusMessageProperty() {
    return statusMessage;
  }

  // Gets the bet valid property
  public BooleanProperty betValidProperty() {
    return betValid;
  }

  // Gets the UI locked property
  public BooleanProperty uiLockedProperty() {
    return uiLocked;
  }

  // Increases the bet amount by 100
  public void increaseBet() {
    if (!uiLocked.get()) {
      betAmount.set(betAmount.get() + 100);
    }
  }

  // Decreases the bet amount by 100, but not below 0
  public void decreaseBet() {
    if (!uiLocked.get()) {
      int newAmount = Math.max(0, betAmount.get() - 100);
      betAmount.set(newAmount);
    }
  }

  // Places a bet on the selected horse
  // Updates the user's balance, locks the UI, and starts the countdown
  // Returns true if the bet was successfully placed, false otherwise
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

  // Starts the countdown timer for the race
  // Updates the countdown text every second until the race starts
  private void startCountdown() {
    // TODO: Implement actual countdown functionality
    // For now, just set a static message
    countdownText.set("Race will start soon. Your bet has been placed!");
  }

  // Handles messages received from the server via the socket connection
  // Processes different message types and updates the ViewModel state accordingly
  @Override
  public void update(String type, String payload) {
    // We're no longer listening for getHorseList responses since we're only using horses from the selected race

    // Handle race updates and other messages
    if ("onRaceStarted".equals(type)) {
      // Handle race start event
      Platform.runLater(() -> {
        countdownText.set("Race has started! Watch the excitement unfold!");
      });
    } else if ("onHorseFinished".equals(type)) {
      // Handle horse finishing event
      // You could show which horses have finished and in what position
    } else if ("onRaceFinished".equals(type)) {
      // Handle race finished event
      Platform.runLater(() -> {
        countdownText.set("Race has finished! Results are in.");
        // Unlock UI after race is done
        uiLocked.set(false);
      });
    }
  }

  // Cleans up resources when the ViewModel is no longer needed
  // Should be called when the view is closed
  public void cleanup() {
    // Remove listener from socket service
    socketService.removeListener(this);
  }
}