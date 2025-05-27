package client.ui.userView.bettingPage;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import client.ui.util.ErrorHandler;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.updates.OnHorseFinished;
import shared.updates.OnRaceFinished;

/**
 * ViewModel for the User Betting View.
 *
 * Handles the logic related to selecting a horse, entering a bet, validating input,
 * listening to race start events, and updating the UI state accordingly.
 */
public class UserBettingViewVM implements ViewModel {

  private final ModelManager model;
  private final RaceDTO selectedRace;
  private final IntegerProperty balanceInfo = new SimpleIntegerProperty(0);
  private final ObservableList<HorseDTO> horses = FXCollections.observableArrayList();
  private final ObjectProperty<HorseDTO> selectedHorse = new SimpleObjectProperty<>();
  private final IntegerProperty betAmount = new SimpleIntegerProperty(0);
  private final StringProperty countdownText = new SimpleStringProperty("Race starting soon");
  private final StringProperty statusMessage = new SimpleStringProperty("");
  private final BooleanProperty betValid = new SimpleBooleanProperty(false);
  private final BooleanProperty uiLocked = new SimpleBooleanProperty(false);
  private final BooleanProperty navigateToGameView = new SimpleBooleanProperty(false);
  private final BooleanProperty navigateToUserLandingPage = new SimpleBooleanProperty(false);

  /**
   * Constructs the ViewModel with references to the model and selected race.
   *
   * @param model         the shared ModelManager
   * @param selectedRace  the race selected for betting
   */
  public UserBettingViewVM(ModelManager model, RaceDTO selectedRace) {
    this.model = model;
    this.selectedRace = selectedRace;
    horses.setAll(selectedRace.horses());
    balanceInfo.bind(model.getUserBalance());

    model.raceStartedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        String startedRaceName = model.currentRaceNameProperty().get();
        if (selectedRace.name().equals(startedRaceName) && model.betPlacedProperty().get()) {
          navigateToGameView.set(true);
        } else {
          navigateToUserLandingPage.set(true);
        }
      }
    });

    model.betPlacedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        statusMessage.set("Bet placed successfully!");
        uiLocked.set(true);
        startCountdown();
      }
    });

    setupValidation();
    if (!horses.isEmpty()) {
      selectedHorse.set(horses.get(0));
    }
  }

  /** @return property for current user balance */
  public IntegerProperty balanceInfoProperty() {
    return balanceInfo;
  }

  /** Sets up listeners to validate bet on user input */
  private void setupValidation() {
    selectedHorse.addListener((obs, oldVal, newVal) -> validateBet());
    betAmount.addListener((obs, oldVal, newVal) -> validateBet());
  }

  /**
   * Validates whether a bet is currently valid.
   * Updates the betValid property based on model checks and UI lock state.
   */
  private void validateBet() {
    if (selectedHorse.isNull().get()) {
      ErrorHandler.showAlert("No horse selected", "Select horse first!");
      return;
    }
    boolean valid = model.validateBet(selectedHorse.get(), betAmount.get()) && !uiLocked.get();
    betValid.set(valid);
  }

  /** @return list of horses available for betting */
  public ObservableList<HorseDTO> getHorses() {
    return horses;
  }

  /** @return the currently selected horse property */
  public ObjectProperty<HorseDTO> selectedHorseProperty() {
    return selectedHorse;
  }

  /** @return the property representing the user's bet amount */
  public IntegerProperty betAmountProperty() {
    return betAmount;
  }

  /** @return the countdown text to display before the race */
  public StringProperty countdownTextProperty() {
    return countdownText;
  }

  /** @return the property for feedback or status messages */
  public StringProperty statusMessageProperty() {
    return statusMessage;
  }

  /** @return whether the current bet is valid */
  public BooleanProperty betValidProperty() {
    return betValid;
  }

  /** @return whether the betting UI is currently locked */
  public BooleanProperty uiLockedProperty() {
    return uiLocked;
  }

  /** @return property for triggering navigation to the game view */
  public BooleanProperty navigateToGameViewProperty() {
    return navigateToGameView;
  }

  /** @return property for triggering navigation back to user landing page */
  public BooleanProperty getNavigateToUserLandingPageProperty() {
    return navigateToUserLandingPage;
  }

  /** @return the selected race associated with this view */
  public RaceDTO getSelectedRace() {
    return selectedRace;
  }

  /** Resets the game view navigation trigger after it's been used */
  public void resetGameViewNavigation() {
    navigateToGameView.set(false);
  }

  /** Increases the current bet by 100, if allowed */
  public void increaseBet() {
    if (!uiLocked.get()) {
      betAmount.set(betAmount.get() + 100);
    }
  }

  /** Decreases the current bet by 100, to a minimum of 0 */
  public void decreaseBet() {
    if (!uiLocked.get()) {
      int newAmount = Math.max(0, betAmount.get() - 100);
      betAmount.set(newAmount);
    }
  }

  /**
   * Sends the bet to the model if valid.
   *
   * @return true if the bet was sent, false if invalid or UI locked
   */
  public boolean placeBet() {
    if (!betValid.get() || uiLocked.get()) {
      statusMessage.set("Invalid bet: Please select a horse and valid amount");
      return false;
    }

    model.createBet(selectedHorse.get(), betAmount.get());
    return true;
  }

  /**
   * Starts the countdown text after a bet is placed.
   * Currently placeholder implementation.
   */
  private void startCountdown() {
    // TODO: Implement actual countdown logic
    countdownText.set("Race will start soon. Your bet has been placed!");
  }
}
