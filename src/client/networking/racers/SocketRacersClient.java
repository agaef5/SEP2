package client.networking.racers;

import client.networking.SocketService;
import server.model.Horse;
import shared.HorseListRequest;
import shared.RacerRequest;
import shared.Request;

import java.util.List;

public class SocketRacersClient implements RacersClient
{
  private SocketService socketService;

  public SocketRacersClient(SocketService socketService){
    this.socketService = socketService;
  }

//  TODO: remove this method and use "getRacerList" instead
//  expect response from the SocketService (check out socketAuthentication)
  @Override public List<Horse> getHorseList()
  {
    return List.of();
  }

  @Override public void getRacerList()
  {
    Request request = new Request("racer", "getRacerList", new HorseListRequest());
    socketService.sendRequest(request);
  }

  @Override public void getRacer(RacerRequest racerRequest)
  {
    Request request = new Request("racer", "getRacer", racerRequest);
    socketService.sendRequest(request);
  }

}
