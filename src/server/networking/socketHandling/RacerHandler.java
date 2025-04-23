package server.networking.socketHandling;

import server.model.Racer;
import server.services.racerList.RacerListServiceImpl;
import server.services.racerList.RacerListService;
import shared.RacerListRequest;
import shared.RacerListResponse;
import shared.RacerRequest;
import shared.RacerResponse;

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
//      case "createRacer"->{
//        if(payload instanceof Racer racer){
//          return createRacerRequest(racer);
//       }
      }
      default -> throw new IllegalArgumentException("Invalid action: " + action);

    }
  }

  private RacerListResponse handleRacerListRequest(RacerListRequest racerListRequest){
    return racerListService.getRacerList(racerListRequest.racerType());
  }

  private RacerResponse handleGetRacerRequest(RacerRequest racerRequest){
    return racerListService.getRacer(racerRequest.racerType(), racerRequest.id());
  }
//  private RacerResponse createRacerRequest(Racer racer){
//    racerListService.createRacer(racer); // TODO finish  it later :)
//  }
}
