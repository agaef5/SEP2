package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;


import server.services.horseList.HorseListServiceImpl;
import server.services.horseList.HorseListService;
import shared.*;
import shared.DTO.HorseDTO;

/**
 * {@code HorseHandler} is responsible for processing horse-related requests from clients.
 * It interacts with the {@code HorseListService} to handle actions such as retrieving,
 * creating, updating, and deleting horses.
 */
public class HorseHandler extends BaseRequestHandler {
  private final HorseListService horseListService;
  private final Gson gson = new Gson();

  /**
   * Constructor to initialize the {@code HorseHandler} with a {@code HorseListService}.
   * This service is used for managing horse data in the system.
   */
  public HorseHandler() {
    this.horseListService = new HorseListServiceImpl();
  }

  /**
   * This method handles various actions related to horses based on the provided action string.
   * Each action corresponds to a specific horse-related operation, which is delegated
   * to the appropriate method for processing.
   *
   * @param action The action to be performed (e.g., "getHorseList", "createHorse").
   * @param payload The payload containing necessary data for the action.
   * @return The response generated after processing the request.
   * @throws IllegalArgumentException If the action is invalid.
   */
  @Override
  public Object safeHandle(String action, JsonElement payload) {
    switch (action) {
      case "getHorseList" -> {
        HorseListRequest request = parsePayload(payload, HorseListRequest.class);
        return handleHorseListRequest(request);
      }
      case "getHorse" -> {
        HorseRequest request = parsePayload(payload, HorseRequest.class);
        return handleGetHorseRequest(request);
      }
      case "createHorse" -> {
        CreateHorseRequest request = parsePayload(payload, CreateHorseRequest.class);
        return createHorserRequest(request);
      }
      case "updateHorse" -> {
        HorseDTO horseToUpdate = parsePayload(payload, HorseDTO.class);
        return handleUpdateHorse(horseToUpdate);
      }
      case "deleteHorse" -> {
        HorseDTO horseToRemove = parsePayload(payload, HorseDTO.class);
        return handleRemoveHorse(horseToRemove);
      }
      default -> throw new IllegalArgumentException("Invalid action: " + action);
    }
  }

  /**
   * Handles the request to retrieve a list of all horses.
   *
   * @param horseListRequest The request containing parameters for listing horses.
   * @return The response containing a list of horses.
   */
  private HorseListResponse handleHorseListRequest(HorseListRequest horseListRequest) {
    return horseListService.getHorseList();
  }

  /**
   * Handles the request to retrieve a specific horse by its ID.
   *
   * @param horseRequest The request containing the horse ID.
   * @return The response containing the requested horse's data.
   */
  private HorseResponse handleGetHorseRequest(HorseRequest horseRequest) {
    return horseListService.getHorse(horseRequest.id());
  }

  /**
   * Handles the request to update the data of an existing horse.
   *
   * @param horse The horse object containing the updated data.
   * @return The updated horse object.
   */
  private HorseDTO handleUpdateHorse(HorseDTO horse) {
    return horseListService.updateHorse(horse);
  }

  /**
   * Handles the request to remove a horse from the system.
   *
   * @param horse The horse object to be removed.
   * @return A success message indicating the removal was successful.
   */
  private String handleRemoveHorse(HorseDTO horse) {
    horseListService.removeHorse(horse);
    return "success";
  }

  /**
   * Handles the request to create a new horse.
   *
   * @param createHorseRequest The request containing data for creating the new horse.
   * @return The response containing the created horse's data.
   */
  private CreateHorseResponse createHorserRequest(CreateHorseRequest createHorseRequest) {
    HorseDTO createdHorse = horseListService.createHorse(
        createHorseRequest.name(), createHorseRequest.speedMin(), createHorseRequest.speedMax());
    return new CreateHorseResponse(createdHorse);
  }
}
