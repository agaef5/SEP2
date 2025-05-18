package client.ui.userView.bettingPage;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.updates.OnHorseFinished;
import shared.updates.OnRaceFinished;

public class UserBettingViewVM implements ViewModel {

  private final ModelManager model;
  private final RaceDTO selectedRace;
  private final IntegerProperty balanceInfo = new SimpleIntegerProperty(0);



  // Observable list containing horses available for betting
  private final ObservableList<HorseDTO> horses = FXCollections.observableArrayList();

  // The currently selected horse
  private final ObjectProperty<HorseDTO> selectedHorse = new SimpleObjectProperty<>();

  // The amount of money to bet
  private final IntegerProperty betAmount = new SimpleIntegerProperty(0);

    // The countdown timer for race start
  private final StringProperty countdownText = new SimpleStringProperty("Race starting soon");

  // Status message for user feedback
  private final StringProperty statusMessage = new SimpleStringProperty("");

  // Property to indicate if a bet is valid
  private final BooleanProperty betValid = new SimpleBooleanProperty(false);

  // Property to indicate if betting UI is locked (after placing a bet)
  private final BooleanProperty uiLocked = new SimpleBooleanProperty(false);

  // Property to trigger navigation to the race view
  private final BooleanProperty navigateToGameView = new SimpleBooleanProperty(false);

    // Constructor with race parameter
  public UserBettingViewVM(ModelManager model, RaceDTO selectedRace) {
    //saving dependencies
    this.model = model;
    this.selectedRace = selectedRace;
    horses.setAll(selectedRace.horses());     //initialise horses
    balanceInfo.bind(model.getUserBalance()); //bind balance

    //register listener for race updates
    model.raceStartedProperty().addListener((obs, oldVal, newVal) ->
    {
       if (newVal)
       {
         String startedRaceName = model.currentRaceNameProperty().get();
          if (selectedRace.name().equals(startedRaceName))
          {
          // It's our race - navigate to GameView
          navigateToGameView.set(true);
          }
        }
    });

    setupValidation();
    if (!horses.isEmpty())
    {
      selectedHorse.set(horses.get(0));
    }
  }

  public IntegerProperty balanceInfoProperty()
  {
    return balanceInfo;
  }

  //local listeners on UI level, only listen to UI changes.
  private void setupValidation() {
    selectedHorse.addListener((obs, oldVal, newVal) -> validateBet());
    betAmount.addListener((obs, oldVal, newVal) -> validateBet());
  }

  // Validates if the current bet selection is valid
  // Updates the betValid property accordingly
  private void validateBet() {
    boolean valid = model.validateBet(selectedHorse.get(), betAmount.get()) &&
            !uiLocked.get();
    betValid.set(valid);
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

  // Gets the property that indicates when to navigate to the race view
  public BooleanProperty navigateToGameViewProperty() {
    return navigateToGameView;
  }

  // Gets the selected race
  public RaceDTO getSelectedRace() {
    return selectedRace;
  }

  // Resets the navigation property after navigation has been handled
  public void resetGameViewNavigation() {
    navigateToGameView.set(false);
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
    if (!betValid.get() || uiLocked.get())
    {
      statusMessage.set("Invalid bet: Please select a horse and valid amount");
      return false;
    }

    // TODO send balance update this to server through modelmanager


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

  }