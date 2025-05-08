package client.ui.adminView.horseList;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;

import shared.*;

import java.util.ArrayList;

/**
 * ViewModel for the Create/Edit Horse view.
 * Manages the data and operations related to horse management.
 * Handles communication with the server via HorsesClient and processes responses.
 * Implements MessageListener to receive updates from the server.
 */
public class CreateEditHorseVM implements MessageListener {

  /** Client for communicating with horse-related server endpoints */
  private final HorsesClient horseClient;

  /** Observable list containing all horses retrieved from the server */
  private final ObservableList<Horse> horseList = FXCollections.observableArrayList();

  /** Observable property for the horse name */
  private final StringProperty horseName = new SimpleStringProperty();

  /** Observable property for the minimum speed value */
  private final IntegerProperty speedMin = new SimpleIntegerProperty();

  /** Observable property for the maximum speed value */
  private final IntegerProperty speedMax = new SimpleIntegerProperty();

  /** Observable property controlling the edit button's disabled state */
  private final BooleanProperty editButtonDisabled = new SimpleBooleanProperty();

  /** Observable property controlling the remove button's disabled state */
  private final BooleanProperty removeButtonDisabled = new SimpleBooleanProperty();

  /** JSON parser for handling server responses */
  private final Gson gson;

  /** Service for socket communication with the server */
  private final SocketService socketService;

  /** The currently selected horse */
  private Horse selectedHorse;

  /** Flag indicating whether the view is in horse creation mode */
  private boolean creatingHorse;

  /**
   * Constructs the ViewModel with necessary dependencies and initializes data.
   *
   * @param client Client for horse-related server operations
   * @param socketService Service for socket communication with the server
   */
  public CreateEditHorseVM(HorsesClient client, SocketService socketService) {
    this.horseClient = client;
    this.socketService = socketService;
    this.socketService.addListener(this);
    horseClient.getHorseList();
    this.gson = new Gson();
    creatingHorse = false;
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
   * Gets the observable property controlling the edit button's disabled state.
   * @return Property controlling whether the edit button should be disabled
   */
  public BooleanProperty getEditButtonDisabledProperty() { return editButtonDisabled; }

  /**
   * Gets the observable property controlling the remove button's disabled state.
   * @return Property controlling whether the remove button should be disabled
   */
  public BooleanProperty getRemoveButtonDisableProperty() { return removeButtonDisabled; }

  /**
   * Gets the observable list containing all horses.
   * @return Observable list of all horses
   */
  public ObservableList<Horse> getHorseList() {
    return horseList;
  }

  /**
   * Sets the selected horse and updates form fields and button states accordingly.
   *
   * @param newVal The newly selected horse, or null if no selection
   */
  public void setSelectedHorse(Horse newVal) {
    this.selectedHorse = newVal;
    if (newVal != null) {
      horseName.set(newVal.getName());
      speedMin.set(newVal.getSpeedMin());
      speedMax.set(newVal.getSpeedMax());

      editButtonDisabled.set(false);
      removeButtonDisabled.set(false);
    }
  }

  /**
   * Clears the current selection and resets form fields and button states.
   */
  public void setNull() {
    this.selectedHorse = null;
    horseName.set(null);
    speedMin.set(0);
    speedMax.set(0);

    editButtonDisabled.set(true);
    removeButtonDisabled.set(true);
  }

  /**
   * Creates a new horse with the current form values.
   * Only executes if the view is in creation mode.
   */
  public void addHorse() {
    if(!creatingHorse) return;

    Horse newHorse = new Horse(
        -1, // temporary ID
        horseName.get(),
        speedMin.get(),
        speedMax.get()
    );

    CreateHorseRequest createHorseRequest = new CreateHorseRequest(horseName.get(), speedMin.get(), speedMax.get());
    horseClient.createHorse(createHorseRequest);
    setReadMode();
  }

  /**
   * Updates the currently selected horse with the form values.
   * Only executes if a horse is selected.
   */
  public void updateHorse() {
    if (selectedHorse != null) {
      selectedHorse.setName(horseName.get());
      selectedHorse.setSpeedMin(speedMin.get());
      selectedHorse.setSpeedMax(speedMax.get());
      horseClient.updateHorse(selectedHorse);
    }
  }

  /**
   * Removes the currently selected horse.
   * Only executes if a horse is selected.
   */
  public void removeHorse() {
    if (selectedHorse != null) {
      horseClient.deleteHorse(selectedHorse);
      setNull();
    }
  }

  /**
   * Updates the horse list with data received from the server.
   * Updates must be performed on the JavaFX application thread.
   *
   * @param horseListResponse Response object containing the list of horses
   */
  public void updateHorseList(HorseListResponse horseListResponse) {
    if(horseListResponse == null) return;

    Platform.runLater(() -> {
      System.out.println("Platform.runLater: horses = " + horseListResponse.horseList());
      ArrayList<Horse> newHorseList = new ArrayList<>();
      for (Horse horse : horseListResponse.horseList()) {
        newHorseList.add(horse);
      }
      horseList.setAll(newHorseList);

      if(selectedHorse == null) setSelectedHorse(horseList.getFirst());
      System.out.println("List updated");
    });
  }

  /**
   * Activates horse creation mode.
   * If already in creation mode, finalizes the current horse creation first.
   */
  public void setHorseCreationMode() {
    if(creatingHorse) addHorse();

    setNull();
    creatingHorse = true;
  }

  /**
   * Exits horse creation mode and returns to read mode.
   */
  public void setReadMode() {
    creatingHorse = false;
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
    System.out.println("Message received: " + type);
    switch (type) {
      case "getHorseList":
        HorseListResponse horseListResponse = gson.fromJson(payload, HorseListResponse.class);
        System.out.println("Parsed horses: " + horseListResponse.horseList());
        updateHorseList(horseListResponse);
        break;
      case "createHorse":
        CreateHorseResponse createHorseResponse = gson.fromJson(payload, CreateHorseResponse.class);
        handleCreateHorseResponse(createHorseResponse);
        break;
      case "updateHorse":
        Horse updatedHorse = gson.fromJson(payload, Horse.class);
        handleUpdateHorseResponse(updatedHorse);
        break;
      case "deleteHorse":
        String message = payload;
        handleRemoveHorseResponse(message);
        break;
    }
  }

  /**
   * Handles the response after creating a horse.
   * Updates the horse list and selects the newly created horse.
   *
   * @param createHorseResponse Response object from the create horse operation
   */
  private void handleCreateHorseResponse(CreateHorseResponse createHorseResponse) {
    horseClient.getHorseList();
    if (createHorseResponse.horse() != null) {
      Horse newHorse = gson.fromJson(createHorseResponse.horse().toString(), Horse.class);
      setSelectedHorse(newHorse);
    }
  }

  /**
   * Handles the response after updating a horse.
   * Updates the horse list and selects the updated horse.
   *
   * @param updatedHorse The updated horse object returned from the server
   */
  private void handleUpdateHorseResponse(Horse updatedHorse) {
    horseClient.getHorseList();
    if (updatedHorse != null) {
      setSelectedHorse(updatedHorse);
    }
  }

  /**
   * Handles the response after removing a horse.
   * Updates the horse list and clears the selection if successful.
   *
   * @param message Response message from the delete operation
   */
  private void handleRemoveHorseResponse(String message) {
    if(message.equals("success")) {
      horseClient.getHorseList();
      setNull();
    }
  }
}