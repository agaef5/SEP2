package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import server.model.Racer;
import server.services.racerList.RacerListServiceImpl;
import server.services.racerList.RacerListService;
import shared.*;

public class RacerHandler implements RequestHandler
{
  private final RacerListService racerListService;
  private final Gson gson = new Gson();


  public RacerHandler(){
    this.racerListService = new RacerListServiceImpl();
  }

  @Override public Object handle(String action, JsonElement payload){
    switch (action) {
      case "getRacerList" -> {
        Object request = gson.fromJson(payload, RacerListRequest.class);
        if (request instanceof RacerListRequest racerListRequest) {
          return handleRacerListRequest(racerListRequest);
        } else {
          throw new IllegalArgumentException("Invalid payload for horse list request");
        }
      }
      case "getRacer" -> {
        Object request = gson.fromJson(payload, RacerRequest.class);
        if(request instanceof RacerRequest racerRequest){
          return handleGetRacerRequest(racerRequest);
        }else {
        throw new IllegalArgumentException("Invalid payload for horse list request");
        }
      }
      case "createRacer"->{

        Object request = gson.fromJson(payload, CreateRacerRequest.class);
        if(request instanceof CreateRacerRequest createRacerRequest){
          return createRacerRequest(createRacerRequest);
       }
      }
      default -> throw new IllegalArgumentException("Invalid action: " + action);
    }
    return null;
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
