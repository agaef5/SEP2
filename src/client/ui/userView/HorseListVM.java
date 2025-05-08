package client.ui.userView;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import shared.CreateHorseResponse;
import shared.DTO.HorseDTO;
import shared.HorseListResponse;
import shared.Respond;

import java.util.ArrayList;

/**
 * ViewModel for the Horse List view in the user interface.
 * Manages the data related to displaying horse information for regular users.
 * Unlike the admin version, this ViewModel is read-only and doesn't allow modifications.
 * Implements MessageListener to receive updates from the server.
 */
public class HorseListVM implements MessageListener
{
  /** Service for socket communication with the server */
  private final SocketService socketService;
  /** Observable property for the horse name */
  private final StringProperty horseName = new SimpleStringProperty();
  /** Observable property for the minimum speed value */
  private final IntegerProperty speedMin = new SimpleIntegerProperty();
  /** Observable property for the maximum speed value */
  private final IntegerProperty speedMax = new SimpleIntegerProperty();
  /** JSON parser for handling server responses */
  private final Gson gson;
  /** Client for communicating with horse-related server endpoints */
  private HorsesClient horseClient;
  /** Observable list containing all horses retrieved from the server */
  private ObservableList<HorseDTO> horseList = FXCollections.observableArrayList();
  /** The currently selected horse */
  private HorseDTO selectedHorse;

  /**
   * Constructs the ViewModel with necessary dependencies and initializes data.
   * Registers as a listener for socket messages and requests the horse list from the server.
   *
   * @param horsesClient Client for horse-related server operations
   * @param socketService Service for socket communication with the server
   */
  public HorseListVM(HorsesClient horsesClient, SocketService socketService)
  {
    this.horseClient = horsesClient;
    this.socketService = socketService;
    this.socketService.addListener(this);
    horseClient.getHorseList();
    this.gson = new Gson();
  }

  /**
   * Gets the observable property for the horse name.
   * @return Property containing the horse name
   */
  public Property<String> horseNameProperty() { return horseName; }

  /**
   * Gets the observable property for the minimum speed.
   * @return Property containing the minimum speed value
   */
  public IntegerProperty speedMinProperty() { return speedMin; }

  /**
   * Gets the observable property for the maximum speed.
   * @return Property containing the maximum speed value
   */
  public IntegerProperty speedMaxProperty() { return speedMax; }

  /**
   * Gets the observable list containing all horses.
   * @return Observable list of all horses
   */
  public ObservableList<HorseDTO> getHorseList() {
    return horseList;
  }

  /**
   * Sets the selected horse and updates form fields accordingly.
   * Used when a user selects a horse in the list view.
   *
   * @param newVal The newly selected horse, or null if no selection
   */
  public void setSelectedHorse(HorseDTO newVal) {
    this.selectedHorse = newVal;
    if (newVal != null) {
      horseName.set(newVal.name());
      speedMin.set(newVal.speedMin());
      speedMax.set(newVal.speedMax());
    }
  }

  /**
   * Updates the horse list with data received from the server.
   * Updates must be performed on the JavaFX application thread.
   *
   * @param horseListResponse Response object containing the list of horses
   */
  public void updateHorseList(HorseListResponse horseListResponse){
    if(horseListResponse == null) return;

    Platform.runLater(() -> {
      System.out.println("Platform.runLater: horses = " + horseListResponse.horseList());
      ArrayList<HorseDTO> newHorseList = new ArrayList<>();
      for (HorseDTO horse : horseListResponse.horseList()) {
        newHorseList.add(horse);
      }
      horseList.setAll(newHorseList);

      if(selectedHorse == null) setSelectedHorse(horseList.getFirst());
      System.out.println("List updated");
    });
  }

  /**
   * Handles messages received from the server via the socket connection.
   * Currently only processes horse list responses.
   *
   * @param type The type of message received
   * @param payload The JSON payload containing the message data
   */
  @Override
  public void update(String type, String payload){
    System.out.println("Message received: " + type);
    switch (type) {
      case "getHorseList":
        HorseListResponse horseListResponse = gson.fromJson(payload, HorseListResponse.class);
        System.out.println("Parsed horses: " + horseListResponse.horseList());
        updateHorseList(horseListResponse);
        break;
    }
  }
}