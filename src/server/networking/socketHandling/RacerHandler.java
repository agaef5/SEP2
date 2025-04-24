package server.networking.socketHandling;

import server.model.Racer;
import server.services.racerList.RacerListServiceImpl;
import server.services.racerList.RacerListService;
import shared.*;

public class RacerHandler implements RequestHandler
{
  private final RacerListService racerListService;

  public RacerHandler(){
    this.racerListService = new RacerListServiceImpl();
  }

  @Override public Object handle(String action, Object payload){
    switch (action) {
      case "getRacerList" -> {
        if (payload instanceof RacerListRequest racerListRequest) {
          return handleRacerListRequest(racerListRequest);
        } else {
          throw new IllegalArgumentException("Invalid payload for horse list request");
        }
      }
      case "getRacer" -> {
        if(payload instanceof RacerRequest racerRequest){
          return handleGetRacerRequest(racerRequest);
        }else {
        throw new IllegalArgumentException("Invalid payload for horse list request");
        }
      }
      case "createRacer"->{
        if(payload instanceof CreateRacerRequest createRacerRequest){
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
