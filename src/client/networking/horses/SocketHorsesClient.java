package client.networking.horses;

import client.networking.SocketService;
import com.google.gson.JsonElement;
import com.google.gson.Gson;

import shared.*;
import shared.DTO.HorseDTO;

/**
 * Implementation of the {@link HorsesClient} interface using socket communication.
 * This class sends requests to the server to manage horse-related data,
 * including retrieving, creating, updating, and deleting horses.
 */
public class SocketHorsesClient implements HorsesClient {

  private final SocketService socketService;
  private final Gson gson;

  /**
   * Constructor that initializes the SocketService and Gson for handling socket communication
   * and JSON serialization/deserialization.
   *
   * @param socketService the SocketService used for sending requests
   */
  public SocketHorsesClient(SocketService socketService) {
    this.socketService = socketService;
    this.gson = new Gson();
  }

  /**
   * Sends a request to retrieve the full list of horses.
   * This method serializes a {@link HorseListRequest} to JSON and sends it to the server.
   */
  @Override
  public void getHorseList() {
    JsonElement payload = gson.toJsonTree(new HorseListRequest());
    Request request = new Request("horse", "getHorseList", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a request to retrieve a specific horse based on the provided {@link HorseRequest}.
   *
   * @param horseRequest the request containing the identifier of the horse to be retrieved
   */
  @Override
  public void getHorse(HorseRequest horseRequest) {
    JsonElement payload = gson.toJsonTree(horseRequest);
    Request request = new Request("horse", "getHorse", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a request to create a new horse using the provided {@link CreateHorseRequest}.
   *
   * @param createHorseRequest the request containing data for the new horse
   */
  @Override
  public void createHorse(CreateHorseRequest createHorseRequest) {
    JsonElement payload = gson.toJsonTree(createHorseRequest);
    Request request = new Request("horse", "createHorse", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a request to update the information of an existing horse.
   *
   * @param selectedHorse the horse with updated data
   */
  @Override
  public void updateHorse(HorseDTO selectedHorse) {
    JsonElement payload = gson.toJsonTree(selectedHorse);
    Request request = new Request("horse", "updateHorse", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a request to delete the specified horse from the system.
   *
   * @param selectedHorse the horse to be deleted
   */
  @Override
  public void deleteHorse(HorseDTO selectedHorse) {
    JsonElement payload = gson.toJsonTree(selectedHorse);
    Request request = new Request("horse", "deleteHorse", payload);
    socketService.sendRequest(request);
  }

  // Uncomment to add listeners for messages if necessary
  /*
  @Override
  public void addListener(MessageListener listener) {
    listeners.add(listener);
  }

  private void handleMessage(Object message) {
    for (MessageListener listener : listeners) {
      listener.update(message);
    }
  }
  */
}
