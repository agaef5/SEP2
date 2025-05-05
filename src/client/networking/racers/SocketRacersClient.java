package client.networking.racers;

import client.networking.SocketService;
import client.ui.MessageListener;
import com.google.gson.JsonElement;
import  com.google.gson.*;
import server.model.Racer;
import shared.*;

import java.util.ArrayList;
import java.util.List;

public class SocketRacersClient implements RacersClient {
  private final SocketService socketService;
  private final Gson gson;

  public SocketRacersClient(SocketService socketService) {
    this.socketService = socketService;
    this.gson= new Gson();
  }


@Override
  public void getRacerList()
  {
    JsonElement payload = gson.toJsonTree(new RacerListRequest("horse"));
    Request request = new Request("racer", "getRacerList", payload);
    socketService.sendRequest(request);
  }

  @Override
  public void getRacer(RacerRequest racerRequest)
  {
    JsonElement payload = gson.toJsonTree(racerRequest);
    Request request = new Request("racer", "getRacer", payload);
    socketService.sendRequest(request);
  }


  @Override
  public void createRacer(CreateRacerRequest createRacerRequest)
  {
    JsonElement payload = gson.toJsonTree(createRacerRequest);
    Request request = new Request("racer", "createRacer", payload);
    socketService.sendRequest(request);
  }

  @Override
  public void updateRacer(Racer selectedRacer) {
    JsonElement payload = gson.toJsonTree(selectedRacer);
    Request request = new Request("racer", "updateRacer", payload);
    socketService.sendRequest(request);
  }

  @Override
  public void deleteRacer(Racer selectedRacer) {
    JsonElement payload = gson.toJsonTree(selectedRacer);
    Request request = new Request("racer", "deleteRacer", payload);
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
