package client.ui.userView.bettingPage;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import client.ui.util.ErrorHandler;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import server.model.Race;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
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

  // Property to trigger navigation back to user landing page
  private final BooleanProperty navigateToUserLandingPage = new SimpleBooleanProperty(false);

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
          if (selectedRace.name().equals(startedRaceName) && model.betPlacedProperty().get())
          {
          // It's our race - navigate to GameView
          navigateToGameView.set(true);
          }else {
            navigateToUserLandingPage.set(true);
          }
        }
    });

    model.betPlacedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        // Bet was successful
        statusMessage.set("Bet placed successfully!");
        uiLocked.set(true);
        startCountdown();
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
    if(selectedHorse.isNull().get()) {
      ErrorHandler.showAlert("No horse selected", "Select horse first!");
      return;
    }
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

  // Gets the property that indicates when to navigate to the landing page
  public BooleanProperty getNavigateToUserLandingPageProperty() {
    return navigateToUserLandingPage;
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

  public boolean placeBet() {
    if (!betValid.get() || uiLocked.get()) {
      statusMessage.set("Invalid bet: Please select a horse and valid amount");
      return false;
    }

    // Send bet via ModelManager (no username needed)
    model.createBet(selectedHorse.get(), betAmount.get());

    return true;
  }

  // Starts the countdown timer for the race
  // Updates the countdown text every second until the race starts
  private void startCountdown() {
    // TODO: Implement actual "countdown till race starts" functionality

    // For now, just set a static message
    countdownText.set("Race will start soon. Your bet has been placed!");
  }

  }