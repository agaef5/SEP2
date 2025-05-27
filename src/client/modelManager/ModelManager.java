package client.modelManager;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import shared.DTO.*;

/**
 * The ModelManager interface defines all properties and methods
 * that the ViewModel layer uses to interact with the underlying application state.
 *
 * Provides JavaFX properties for data binding, and abstracts methods
 * for authentication, race, horse, and betting logic.
 */
public interface ModelManager {

    // —— Properties for VMs to bind to ——

    /** @return property indicating login success */
    BooleanProperty loginSuccessProperty();

    /** @return property containing login message */
    StringProperty loginMessageProperty();

    /** @return property indicating registration success */
    BooleanProperty registerSuccessProperty();

    /** @return property containing registration message */
    StringProperty registerMessageProperty();

    /** @return observable list of available race tracks */
    ObservableList<RaceTrackDTO> getRaceTracksList();

    /** @return observable list of all races */
    ObservableList<RaceDTO> getRaceList();

    /** @return property containing the next upcoming race */
    ObjectProperty<RaceDTO> getNextRace();

    /** @return property indicating success of race creation */
    BooleanProperty createRaceSuccessProperty();

    /** @return property containing race creation message */
    StringProperty createRaceMessageProperty();

    /** @return property holding the most recently created race */
    ObjectProperty<RaceDTO> getCreatedRace();

    /**
     * @return property indicating the current state of the active race
     * or null if a next race is already queued
     */
    ObjectProperty<RaceState> getCurrentRaceState();

    /** @return observable list of horse rankings during/after a race */
    ObservableList<HorseDTO> getRaceRank();

    /** @return property indicating whether a race has started */
    BooleanProperty raceStartedProperty();

    /** @return property containing the name of the current race */
    StringProperty currentRaceNameProperty();

    /** @return observable list of all horses */
    ObservableList<HorseDTO> getHorseList();

    /** @return property indicating horse creation success */
    BooleanProperty createHorseSuccessProperty();

    /** @return property containing horse creation message */
    StringProperty createHorseMessageProperty();

    /** @return property indicating horse update success */
    BooleanProperty updateHorseSuccessProperty();

    /** @return property containing horse update message */
    StringProperty updateHorseMessageProperty();

    /** @return property indicating horse deletion success */
    BooleanProperty deleteHorseSuccessProperty();

    /** @return property containing horse deletion message */
    StringProperty deleteHorseMessageProperty();

    /** @return property indicating whether a bet has been placed */
    BooleanProperty betPlacedProperty();

    /** @return property representing the current user's balance */
    IntegerProperty getUserBalance();

    // —— Methods to be used by VM ——

    /**
     * Sends login request with the given credentials.
     *
     * @param username the username or email
     * @param password the password
     */
    void loginUser(String username, String password);

    /**
     * Sends registration request with the given data.
     *
     * @param username the desired username
     * @param email the email address
     * @param password the desired password
     */
    void registerUser(String username, String email, String password);

    /** Requests all race tracks from the server. */
    void getRaceTracks();

    /** Requests all races from the server. */
    void getAllRaces();

    /**
     * Sends a request to create a new race.
     *
     * @param name name of the race
     * @param raceTrack the race track to use
     * @param capacity the maximum number of horses
     */
    void createRace(String name, RaceTrackDTO raceTrack, Integer capacity);

    /**
     * Sends a bet creation request.
     *
     * @param horseDTO the horse to bet on
     * @param amount the amount to bet
     */
    void createBet(HorseDTO horseDTO, int amount);

    /**
     * Validates a bet before placing it.
     *
     * @param horse the selected horse
     * @param amount the bet amount
     * @return true if valid, false otherwise
     */
    boolean validateBet(HorseDTO horse, int amount);

    /** Requests all horses from the server. */
    void getAllHorses();

    /**
     * Sends a request to create a new horse.
     *
     * @param name the horse's name
     * @param speedMin the minimum speed
     * @param speedMax the maximum speed
     */
    void createHorse(String name, int speedMin, int speedMax);

    /**
     * Sends a request to update an existing horse.
     *
     * @param id the horse's ID
     * @param horseName the new name
     * @param speedMin the new minimum speed
     * @param speedMax the new maximum speed
     */
    void updateHorse(int id, String horseName, int speedMin, int speedMax);

    /**
     * Sends a request to delete the given horse.
     *
     * @param horse the horse to delete
     */
    void deleteHorse(HorseDTO horse);

    /** @return observable list of horse positions during a race */
    ObservableList<Integer> getHorsePositions();

    /**
     * Handles incoming messages from the server and routes them appropriately.
     *
     * @param type the type of message
     * @param payload the message content
     */
    void update(String type, String payload);

    /** Sends a request to refresh the current user's data from the server. */
    void loadCurrentUser();

    /**
     * Gets the current user object.
     *
     * @return the logged-in user
     */
    UserDTO getCurrentUser();

    /**
     * Sets the current user and updates internal state accordingly.
     *
     * @param userDTO the user to set
     */
    void setCurrentUser(UserDTO userDTO);
}
