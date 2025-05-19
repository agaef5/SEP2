package client.modelManager;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import shared.DTO.*;
import shared.user.UserRequest;

public interface ModelManager {
    // —— Properties for VMs to bind to ——
    BooleanProperty    loginSuccessProperty();
    StringProperty     loginMessageProperty();
    BooleanProperty    registerSuccessProperty();
    StringProperty     registerMessageProperty();

    ObservableList<RaceTrackDTO> getRaceTracksList();
    ObservableList<RaceDTO>      getRaceList();
    ObjectProperty<RaceDTO> nextRaceProperty();
    BooleanProperty    createRaceSuccessProperty();
    StringProperty     createRaceMessageProperty();
    public ObjectProperty<RaceState> getCurrentRaceState();
    public ObservableList<HorseDTO> getRaceRank();
    BooleanProperty raceStartedProperty();
    StringProperty currentRaceNameProperty() ;


    ObservableList<HorseDTO> getHorseList();
    BooleanProperty    createHorseSuccessProperty();
    StringProperty     createHorseMessageProperty();
    BooleanProperty    updateHorseSuccessProperty();
    StringProperty     updateHorseMessageProperty();
    BooleanProperty deleteHorseSuccessProperty();
    StringProperty deleteHorseMessageProperty();
    BooleanProperty betPlacedProperty();

    IntegerProperty getUserBalance();

//    Methods to be used by VM
    // Authentication
    void loginUser(String username, String password);
    void registerUser(String username, String email, String password);

    // Race
    void getRaceTracks();
    void getAllRaces();
    void createRace(String name, RaceTrackDTO raceTrack, Integer capacity);

    //    Bet
    void createBet(HorseDTO horseDTO, int amount);
    boolean validateBet(HorseDTO horse, int amount);

    // Horse
    void getAllHorses();
    void createHorse(String name, int speedMin, int speedMax);
    void updateHorse(int id,String horseName,int speedMin,int speedMax);
    void deleteHorse(HorseDTO horse);
    ObservableList<Integer> getHorsePositions();

    //  Game

    void update(String type, String payload);

    void loadCurrentUser();

    UserDTO getCurrentUser();

    void setCurrentUser(UserDTO userDTO);
}
