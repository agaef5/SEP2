package client.ui.userView.landingPage;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.DTO.RaceDTO;
import shared.race.GetRaceListResponse;

import java.util.List;

/**
 * ViewModel for the User Landing Page.
 * Manages data and operations for displaying upcoming race information
 * in accordance with MVVM pattern.
 * Implements MessageListener to receive updates from the server.
 */
public class UserLandingPageVM implements MessageListener {

    /** Service for socket communication with the server */
    private final SocketService socketService;

    /** Client for communicating with race-related server endpoints */
    private final RaceClient raceClient;

    /** JSON parser for handling server responses */
    private final Gson gson;

    /** Observable property containing information about the upcoming race */
    private final StringProperty raceInfo = new SimpleStringProperty("No upcoming races");

    /** Property to indicate if betting stage should be navigated to */
    private final BooleanProperty navigateToBetting = new SimpleBooleanProperty(false);

    /** The currently selected race */
    private RaceDTO selectedRace;

    /**
     * Constructs the ViewModel with necessary dependencies and initializes data.
     *
     * @param raceClient Client for race-related server operations
     * @param socketService Service for socket communication with the server
     */
    public UserLandingPageVM(RaceClient raceClient, SocketService socketService) {
        this.raceClient = raceClient;
        this.socketService = socketService;
        this.gson = new Gson();

        // Register this ViewModel as a listener for socket messages
        this.socketService.addListener(this);

        // Request the current race list from the server
        refreshRaceList();
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
     * Handles messages received from the server via the socket connection.
     * Processes different message types and updates the ViewModel state accordingly.
     *
     * @param type The type of message received
     * @param payload The JSON payload containing the message data
     */
    @Override
    public void update(String type, String payload) {
        if ("getRaceList".equals(type)) {
            GetRaceListResponse response = gson.fromJson(payload, GetRaceListResponse.class);
            updateRaceInfo(response.races());
        }
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
}