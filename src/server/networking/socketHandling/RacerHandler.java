package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import server.model.Racer;
import server.services.racerList.RacerListServiceImpl;
import server.services.racerList.RacerListService;
import shared.*;

public class RacerHandler extends BaseRequestHandler
{
  private final RacerListService racerListService;
  private final Gson gson = new Gson();

  public RacerHandler(){
    this.racerListService = new RacerListServiceImpl();
  }

  @Override public Object safeHandle(String action, JsonElement payload)
  {
      switch (action)
      {
        case "getRacerList" ->
        {
          RacerListRequest request = parsePayload(payload, RacerListRequest.class);
          return handleRacerListRequest(request);
        }
        case "getRacer" ->
        {
          RacerRequest request = parsePayload(payload, RacerRequest.class);
          return handleGetRacerRequest(request);
        }
        case "createRacer" ->
        {
          CreateRacerRequest request = parsePayload(payload, CreateRacerRequest.class);
          return createRacerRequest(request);
        }
        default ->
            throw new IllegalArgumentException("Invalid action: " + action);
      }
  }

  private RacerListResponse handleRacerListRequest(RacerListRequest racerListRequest){
    return racerListService.getRacerList(racerListRequest.racerType());
  }

  private RacerResponse handleGetRacerRequest(RacerRequest racerRequest){
    return racerListService.getRacer(racerRequest.racerType(), racerRequest.id());
  }

  private CreateRacerResponse createRacerRequest(CreateRacerRequest createRacerRequest){
    Racer createdRacer = racerListService.createRacer(createRacerRequest.racerType(),
        createRacerRequest.name(), createRacerRequest.speedMin(), createRacerRequest.speedMax() );
    return new CreateRacerResponse(createdRacer);
  }
}
