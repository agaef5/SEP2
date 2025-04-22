package server.networking.socketHandling;

import server.services.racerList.RacerListServiceImpl;
import server.services.racerList.RacerListService;
import shared.HorseListRequest;
import shared.HorseListResponse;
import shared.RacerRequest;
import shared.RacerResponse;

public class RaceHandler implements RequestHandler
{
  private final RacerListService racerListService;

  public RaceHandler(){
    this.racerListService = new RacerListServiceImpl();
  }

  @Override public Object handle(String action, Object payload){
    switch (action) {
      case "getRacerList" -> {
        if (payload instanceof HorseListRequest horseListRequest) {
          return handleHorseListRequest();
        } else {
          throw new IllegalArgumentException("Invalid payload for horse list request");
        }
      }
      case "getRacer" -> {
        if(payload instanceof RacerRequest racerRequest){
          return handleGetRacerRequest(racerRequest);
        }else {
        throw new IllegalArgumentException("Invalid payload for racer request");
        }
      }
      default -> throw new IllegalArgumentException("Invalid action: " + action);

    }
  }

  public HorseListResponse handleHorseListRequest(){
    return racerListService.getHorsesList();
  }

  public RacerResponse handleGetRacerRequest(RacerRequest racerRequest){
    return racerListService.getRacer(racerRequest.racerType(), racerRequest.id());
  }
}
