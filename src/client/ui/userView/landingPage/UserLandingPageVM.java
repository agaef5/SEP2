package client.ui.userView.landingPage;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.RaceDTO;
import shared.race.GetRaceListResponse;

import java.util.List;

public class UserLandingPageVM implements MessageListener, ViewModel {

    private final SocketService socketService;
    private final RaceClient raceClient;
    private final Gson gson;

    // Existing properties
    private final StringProperty raceInfo = new SimpleStringProperty("No upcoming races");
    private final BooleanProperty navigateToBetting = new SimpleBooleanProperty(false);
    private RaceDTO selectedRace;



    // New properties
    private final StringProperty balanceInfo = new SimpleStringProperty("Balance: $1000"); // Default value
    private final ObservableList<String> betHistory = FXCollections.observableArrayList();
    private int userBalance = 1000; // Default starting balance

    public UserLandingPageVM(RaceClient raceClient, SocketService socketService) {
        this.raceClient = raceClient;
        this.socketService = socketService;
        this.gson = new Gson();

        // Register as listener
        this.socketService.addListener(this);

        // Initialize data
        refreshRaceList();
        loadUserBalance();
        loadBetHistory();
    }

    /**
     * Requests the current race list from the server.
     * This will trigger an update via the MessageListener when the response is received.
     */
    private void refreshRaceList() {
        raceClient.getRaceList();
    }

    /**
     * Gets the observable property containing information about the upcoming race.
     * This property can be bound to UI elements to display race information.
     *
     * @return StringProperty containing formatted race information
     */
    public StringProperty raceInfoProperty() {
        return raceInfo;
    }

    /**
     * Gets the observable property indicating if navigation to betting stage is requested.
     * The controller should observe this property and act accordingly when it changes.
     *
     * @return BooleanProperty indicating if navigation is requested
     */
    public BooleanProperty navigateToBettingProperty() {
        return navigateToBetting;
    }

    /**
     * Gets the currently selected race.
     * This can be used when navigating to the betting stage.
     *
     * @return The currently selected race, or null if no race is selected
     */
    public RaceDTO getSelectedRace() {
        return selectedRace;
    }

    /**
     * Handles the request to enter the betting stage.
     * Sets the navigateToBetting property to true, which the controller observes.
     */
    public void enterBettingStage() {
        navigateToBetting.set(true);
    }

    /**
     * Resets the navigation request after it has been handled.
     * This should be called by the controller after navigation occurs.
     */
    public void resetNavigation() {
        navigateToBetting.set(false);
    }

     /**
     * Gets the observable property for the balance information.
     * @return Property containing formatted balance information
     */
    public StringProperty balanceInfoProperty() {
        return balanceInfo;
    }

    /**
     * Gets the observable list of bet history entries.
     * @return Observable list of bet history entries
     */
    public ObservableList<String> getBetHistory() {
        return betHistory;
    }

    /**
     * Loads the user's current balance from the server.
     * Currently uses a default value, to be implemented with server communication.
     */
    private void loadUserBalance() {
        // TODO: In a real implementation, this would fetch from server
        // For now, we'll use the default value
        updateBalanceDisplay();
    }

    /**
     * Updates the balance display based on the current balance value.
     */
    private void updateBalanceDisplay() {
        balanceInfo.set("Balance: $" + userBalance);
    }

    /**
     * Loads the bet history from the server.
     * Currently populates with dummy data, to be implemented with server communication.
     */
    private void loadBetHistory() {
        // TODO: In a real implementation, this would fetch from server
        // For now, we'll use dummy data
        Platform.runLater(() -> {
            betHistory.clear();
            betHistory.add("Example bet: $100 on Thunder - Won $200");
            betHistory.add("Example bet: $50 on Lightning - Lost");
            betHistory.add("Example bet: $200 on Blizzard - Won $400");

        });
    }

    /**
     * Quit the application.
     */
    public void quitApplication() {
        // Perform any cleanup before exit
        socketService.removeListener(this);
        // The actual exit will be handled by the controller
    }

    /**
     * Updates the race information based on the received race list.
     * If races are available, formats information about the first race in the list.
     * Updates must be performed on the JavaFX application thread.
     *
     * @param races List of races received from the server
     */
    private void updateRaceInfo(List<RaceDTO> races) {
        Platform.runLater(() -> {
            if (races != null && !races.isEmpty()) {
                // Get the first race in the queue (assume it's sorted by time)
                selectedRace = races.get(0);

                // Format the race information
                String info = String.format("Next race: %s - Track: %s - Horses: %d",
                        selectedRace.name(),
                        selectedRace.raceTrack().name(),
                        selectedRace.horses().size());

                raceInfo.set(info);
            } else {
                raceInfo.set("No upcoming races");
                selectedRace = null;
            }
        });
    }


// Override the update method to handle additional message types
    @Override
    public void update(String type, String payload) {
        switch (type) {
            case "getRaceList" -> {
                GetRaceListResponse response = gson.fromJson(payload, GetRaceListResponse.class);
                updateRaceInfo(response.races());
            }
            case "userBalance" -> {
                // TODO create this case
                // Parse balance update from server and update userBalance
                // userBalance = parsedBalanceValue;
                // updateBalanceDisplay();
            }
            case "betHistory" -> {
                // TODO create this case
                // Parse bet history from server and update the list
            }
            default -> {
                // Optionally handle unknown message types
                System.out.println("Received unknown message type: " + type);
            }
        }
    }
}