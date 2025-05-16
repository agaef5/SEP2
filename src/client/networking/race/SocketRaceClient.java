package client.networking.race;

import client.networking.SocketService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import shared.race.CreateRaceRequest;
import shared.race.GetRaceListRequest;
import shared.race.GetRaceTracksRequest;
import shared.Request;

/**
 * Implementation of the {@link RaceClient} interface that communicates with the server for race-related requests.
 * It sends requests related to races, including fetching race lists, creating races, and getting race tracks.
 */
public class SocketRaceClient implements RaceClient {

  private final SocketService socketService;
  private final Gson gson;

  /**
   * Constructs a new {@code SocketRaceClient} with the specified {@link SocketService}.
   *
   * @param socketService the socket service used to send requests to the server
   */
  public SocketRaceClient(SocketService socketService) {
    this.socketService = socketService;
    this.gson = new Gson();
  }

  /**
   * Sends a request to retrieve a list of races to the server.
   *
   */
  @Override public void getRaceList()
  {
    GetRaceListRequest getRaceList = new GetRaceListRequest();
    JsonElement payload = gson.toJsonTree(getRaceList);
    Request request = new Request("race", "getRaceList", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a request to create a new race on the server.
   *
   * @param createRaceRequest the request containing the details for the new race
   */
  @Override
  public void createRace(CreateRaceRequest createRaceRequest) {
    JsonElement payload = gson.toJsonTree(createRaceRequest);
    Request request = new Request("race", "createRace", payload);
    socketService.sendRequest(request);
  }

  /**
   * Sends a request to retrieve a list of available race tracks from the server.
   *
   * @param getRaceTracksRequest the request containing the criteria for retrieving race tracks
   */
  @Override
  public void getRaceTracks(GetRaceTracksRequest getRaceTracksRequest) {
    JsonElement payload = gson.toJsonTree(getRaceTracksRequest);
    Request request = new Request("race", "getRaceTracks", payload);
    socketService.sendRequest(request);
  }
}
