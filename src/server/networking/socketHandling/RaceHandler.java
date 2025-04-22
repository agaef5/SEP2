package server.networking.socketHandling;

import server.services.authentication.AuthentificationService;
import server.services.racerList.RacerListImplementation;
import server.services.racerList.RacerListService;
import shared.HorseListRequest;
import shared.HorseListResponse;
import shared.LoginRequest;
import shared.RegisterRequest;

public class RaceHandler implements RequestHandler
{
  private final RacerListService racerListService;

  public RaceHandler(){
    this.racerListService = new RacerListImplementation();
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
      default -> throw new IllegalArgumentException("Invalid action: " + action);
    }
  }


  public HorseListResponse handleHorseListRequest(){
    return ;
  }
}
