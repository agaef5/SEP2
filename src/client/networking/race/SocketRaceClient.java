package client.networking.race;

import client.networking.SocketService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import shared.CreateRaceRequest;
import shared.Request;

public class SocketRaceClient implements RaceClient
{
  private final SocketService socketService;
  private final Gson gson;

  public SocketRaceClient(SocketService socketService)
  {
    this.socketService=socketService;
    this.gson=new Gson();
  }


  @Override public void getRace()
  {
    //TODO add for geting the races from database
  }

  @Override public void createRace(CreateRaceRequest createRaceRequest)
  {
    JsonElement payload = gson.toJsonTree(createRaceRequest);
    Request request = new Request("race","createRace", payload);
    socketService.sendRequest(request);
  }
}
