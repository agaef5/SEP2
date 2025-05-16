package client.modelManager;

import client.networking.authentication.AuthenticationClient;
import client.networking.horses.HorsesClient;
import client.networking.race.RaceClient;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import server.model.Horse;
import shared.DTO.*;

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

    ObservableList<HorseDTO> getHorseList();
    BooleanProperty    createHorseSuccessProperty();
    StringProperty     createHorseMessageProperty();
    BooleanProperty    updateHorseSuccessProperty();
    StringProperty     updateHorseMessageProperty();
    BooleanProperty deleteHorseSuccessProperty();
    StringProperty deleteHorseMessageProperty();

//    Methods to be used by VM
    // Authentication
    void loginUser(String username, String password);
    void registerUser(String username, String email, String password);

    // Race
    void getRaceTracks();
    void getAllRaces();
    void createRace(String name, RaceTrackDTO raceTrack, Integer capacity);

    //    Bet
    void createBet(String username, HorseDTO horseDTO, int amount);
    void getBetList(String username);

    // Horse
    void getAllHorses();
    void createHorse(String name, int speedMin, int speedMax);
    void updateHorse(HorseDTO horse);
    void deleteHorse(HorseDTO horse);

    void update(String type, String payload);
}
