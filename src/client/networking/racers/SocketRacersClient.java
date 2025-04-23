package client.networking.racers;

import client.networking.SocketService;
import client.ui.MessageListener;
import client.ui.racerList.adminView.CreateEditRacerVM;
import shared.RacerListRequest;
import shared.RacerRequest;
import shared.Request;

import java.util.ArrayList;
import java.util.List;

public class SocketRacersClient implements RacersClient
{
  private SocketService socketService;
  private List<MessageListener> listeners = new ArrayList<>();

  public SocketRacersClient(SocketService socketService){
    this.socketService = socketService;
  }

  @Override public void getRacerList()
  {
    Request request = new Request("racer", "getRacerList", new RacerListRequest());
    socketService.sendRequest(request);
  }

  @Override public void getRacer(RacerRequest racerRequest)
  {
    Request request = new Request("racer", "getRacer", racerRequest);
    socketService.sendRequest(request);
  }

  public void addListener(MessageListener listener) {
    listeners.add(listener);
  }

}
