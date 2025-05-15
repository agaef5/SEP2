package client.ui.userView.landingPage;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.common.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.race.GetRaceListResponse;

import java.util.List;

public class UserLandingPageVM implements MessageListener, ViewModel {

    private final SocketService socketService;
    private final RaceClient raceClient;
    private final Gson gson;

    // Existing properties
    private final StringProperty raceInfo = new SimpleStringProperty("No upcoming races");
    private final BooleanProperty navigateToBetting = new SimpleBooleanProperty(false);
    private final BooleanProperty bettingButtonDisabled = new SimpleBooleanProperty(true);
    private final StringProperty balanceInfo = new SimpleStringProperty("Balance: $1000");
    private final ObservableList<String> betHistory = FXCollections.observableArrayList();
    private RaceDTO selectedRace;
    private int userBalance = 1000;

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

    // Request the current race list from the server
    private void refreshRaceList() {
        raceClient.getRaceList();
    }

    // Get property containing formatted race information
    public StringProperty raceInfoProperty() {
        return raceInfo;
    }

    // Get property indicating if navigation to betting stage is requested
    public BooleanProperty navigateToBettingProperty() {
        return navigateToBetting;
    }

    // Get property that controls whether the betting button should be disabled
    public BooleanProperty bettingButtonDisabledProperty() {
        return bettingButtonDisabled;
    }

    // Get the currently selected race
    public RaceDTO getSelectedRace() {
        return selectedRace;
    }

    // Handle request to enter the betting stage
    public void enterBettingStage() {
        navigateToBetting.set(true);
    }

    // Reset navigation request after it has been handled
    public void resetNavigation() {
        navigateToBetting.set(false);
    }

    // Get property containing formatted balance information
    public StringProperty balanceInfoProperty() {
        return balanceInfo;
    }

    // Get observable list of bet history entries
    public ObservableList<String> getBetHistory() {
        return betHistory;
    }

    // Load the user's current balance from the server
    private void loadUserBalance() {
        // TODO: In a real implementation, this would fetch from server
        // For now, we'll use the default value
        updateBalanceDisplay();
    }

    // Update the balance display based on the current balance value
    private void updateBalanceDisplay() {
        balanceInfo.set("Balance: $" + userBalance);
    }

    // Load the bet history from the server
    private void loadBetHistory() {
        // TODO: In a real implementation, this would fetch from server
        // For now, we'll use dummy data
        Platform.runLater(() -> {
            betHistory.clear();
//            betHistory.add("Example bet: $100 on Thunder - Won $200");
//            betHistory.add("Example bet: $50 on Lightning - Lost");
//            betHistory.add("Example bet: $200 on Blizzard - Won $400");
        });
    }

    // Quit the application
    public void quitApplication() {
        // Perform any cleanup before exit
        socketService.removeListener(this);
        // The actual exit will be handled by the controller
    }

//     Update race information based on the received race list
    private void updateRaceInfo(List<RaceDTO> races) {
        Platform.runLater(() -> {
            if (races != null && !races.isEmpty()) {
                // Get the first race in the queue (assume it's sorted by time)
                selectedRace = races.getFirst();

                // Update button disabled state based on race state
//                TODO: initialize different race stats on server
                bettingButtonDisabled.set(selectedRace.raceState() != RaceState.NOT_STARTED);

                // Display different message based on the race state
                if (selectedRace.raceState() == RaceState.NOT_STARTED) {
                    // Format the race information for upcoming race
                    String info = String.format("Next race: %s - Track: %s - Horses: %d",
                            selectedRace.name(),
                            selectedRace.raceTrack().name(),
                            selectedRace.horses().size());

                    raceInfo.set(info);
                } else if (selectedRace.raceState() == RaceState.IN_PROGRESS) {
                    raceInfo.set("This race is in progress");
                } else if (selectedRace.raceState() == RaceState.FINISHED) {
                    raceInfo.set("The last race has finished. Waiting for the next race...");
                }
            } else {
                raceInfo.set("No upcoming races");
                selectedRace = null;
                bettingButtonDisabled.set(true); // Disable button when no races are available
            }
        });
    }

    // Handle messages received from the server
    @Override
    public void update(String type, String payload) {
        switch (type) {

            case "getRaceList" -> {
                GetRaceListResponse raceListResponse = gson.fromJson(payload, GetRaceListResponse.class);
                updateRaceInfo(raceListResponse.races());
                break;
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