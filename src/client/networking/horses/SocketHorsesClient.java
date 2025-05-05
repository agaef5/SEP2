package client.networking.horses;

import client.networking.SocketService;
import com.google.gson.JsonElement;
import  com.google.gson.*;
import server.model.Horse;
import shared.*;

public class SocketHorsesClient implements HorsesClient
{
  private final SocketService socketService;
  private final Gson gson;

  public SocketHorsesClient(SocketService socketService) {
    this.socketService = socketService;
    this.gson= new Gson();
  }


@Override
  public void getHorseList()
  {
    JsonElement payload = gson.toJsonTree(new HorseListRequest());
    Request request = new Request("horse", "getHorseList", payload);
    socketService.sendRequest(request);
  }

  @Override
  public void getHorse(HorseRequest horseRequest)
  {
    JsonElement payload = gson.toJsonTree(horseRequest);
    Request request = new Request("horse", "getHorse", payload);
    socketService.sendRequest(request);
  }


  @Override
  public void createHorse(CreateHorseRequest createHorseRequest)
  {
    JsonElement payload = gson.toJsonTree(createHorseRequest);
    Request request = new Request("horse", "createHorse", payload);
    socketService.sendRequest(request);
  }

  @Override
  public void updateHorse(Horse selectedHorse) {
    JsonElement payload = gson.toJsonTree(selectedHorse);
    Request request = new Request("horse", "updateHorse", payload);
    socketService.sendRequest(request);
  }

  @Override
  public void deleteHorse(Horse selectedHorse) {
    JsonElement payload = gson.toJsonTree(selectedHorse);
    Request request = new Request("horse", "deleteHorse", payload);
    socketService.sendRequest(request);
  }

//  @Override
//  public void addListener(MessageListener listener) {
//    listeners.add(listener);
//  }
//
//  private void handleMessage(Object message) {
//    for (MessageListener listener : listeners) {
//      listener.update(message);
//    }
//  }
}
