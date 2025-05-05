package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import server.model.Race;
import server.services.races.RaceServiceImpl;
import server.services.races.RacesService;
import shared.CreateRaceRequest;
import shared.RaceRequest;
import shared.RaceResponse;

public class RaceHandler extends BaseRequestHandler
{
  private final RacesService racesService;
  private final Gson gson = new Gson();

  public RaceHandler()
  {
    this.racesService=new RaceServiceImpl();
  }

  @Override public Object safeHandle(String action, JsonElement payload)
  //check what action is required and based on that calls method on Service layer
  {
    switch (action)
    {
      case "createRace"-> //action for creating Race
      {
        CreateRaceRequest request = parsePayload(payload,CreateRaceRequest.class);
        return handleCreateRaceRequest(request);
      }
//      case "getRace"->//action for geting the race
//      {
//       return handleGetRace()

//      }
      default ->
        throw new IllegalArgumentException("Invalid action "+ action);
    }

  }

  private Object handleCreateRaceRequest(CreateRaceRequest request)
      //creating race in service layer and returning this race
  {
    Race createdRace = racesService.createRace(request.name(),request.startDelayMin(),request.capacity(),request.raceTrack());
    return new RaceResponse(createdRace);
  }

  private Object handleGetRace()
  {
    Race race = racesService.getRace();
    return new RaceResponse(race);
  }
}
