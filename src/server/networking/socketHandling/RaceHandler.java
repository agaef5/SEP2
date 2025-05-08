package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import server.model.Race;
import server.model.RaceTrack;
import server.services.races.RaceServiceImpl;
import server.services.races.RacesService;
import shared.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * {@code RaceHandler} is responsible for processing race-related requests from clients.
 * It interacts with the {@code RacesService} to handle actions such as creating races,
 * retrieving race lists, and getting available race tracks.
 */
public class RaceHandler extends BaseRequestHandler {
  private final RacesService racesService;
  private final Gson gson = new Gson();

  /**
   * Constructor to initialize the {@code RaceHandler} with a {@code RacesService}.
   * This service is used for managing race data and race track information in the system.
   */
  public RaceHandler() {
    this.racesService = new RaceServiceImpl();
  }

  /**
   * This method handles various actions related to races based on the provided action string.
   * Each action corresponds to a specific race-related operation, which is delegated
   * to the appropriate method for processing.
   *
   * @param action The action to be performed (e.g., "createRace", "getRaceList").
   * @param payload The payload containing necessary data for the action.
   * @return The response generated after processing the request.
   * @throws IllegalArgumentException If the action is invalid.
   */
  @Override
  public Object safeHandle(String action, JsonElement payload) {
    switch (action) {
      case "createRace" -> {
        CreateRaceRequest request = parsePayload(payload, CreateRaceRequest.class);
        return handleCreateRaceRequest(request);
      }
      case "getRaceList" -> {
        return handleGetRace();
      }
      case "getRaceTracks" -> {
        return handleGetRaceTracks();
      }
      default -> throw new IllegalArgumentException("Invalid action " + action);
    }
  }

  /**
   * Handles the request to create a new race.
   *
   * @param request The request containing data for creating the new race.
   * @return The response containing the created race's data.
   */
  private Object handleCreateRaceRequest(CreateRaceRequest request) {

    Date startTime = request.startTime() != null ? request.startTime() : new Date();

    Race createdRace = racesService.createRace(request.name(), startTime, request.raceTrack());
    return new RaceResponse(createdRace);
  }

  /**
   * Handles the request to retrieve a list of all races.
   *
   * @return The response containing a list of all races.
   */
  private Object handleGetRace() {
    List<Race> races = racesService.getRaceList();
    return new GetRaceListResponse(races);
  }

  /**
   * Handles the request to retrieve a list of all available race tracks.
   *
   * @return The response containing a list of all race tracks.
   */
  private Object handleGetRaceTracks() throws SQLException {
    List<RaceTrack> raceTracks = racesService.getRaceTracks();
    return new GetRaceTrackResponse(raceTracks);
  }
}