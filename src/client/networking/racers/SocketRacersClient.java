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
  private final List<MessageListener> listeners = new ArrayList<>();
  private final Gson gson;

  public SocketRacersClient(SocketService socketService) {
    this.socketService = socketService;
    this.gson= new Gson();
  }



  public void getRacerList()
  {
    JsonElement payload = gson.toJsonTree(new RacerListRequest("horse"));
    Request request = new Request("racer", "getRacerList", payload);
    socketService.sendRequest(request);
  }

  @Override
  public void getRacer(RacerRequest racerRequest) {
    Request request = new Request("racer", "getRacer", racerRequest);
    socketService.sendRequest(request);
  }

  @Override
  public void createRacer(Racer newRacer) {
    Request request = new Request("racer", "createRacer", newRacer);
    socketService.sendRequest(request);
  }

  @Override
  public void updateRacer(Racer selectedRacer) {
    Request request = new Request("racer", "updateRacer", selectedRacer);
    socketService.sendRequest(request);
  }

  @Override
  public void deleteRacer(Racer selectedRacer) {
    Request request = new Request("racer", "deleteRacer", selectedRacer);
    socketService.sendRequest(request);
  }

  @Override
  public void addListener(MessageListener listener) {
    listeners.add(listener);
  }

  private void handleMessage(Object message) {
    for (MessageListener listener : listeners) {
      listener.update(message);
    }
  }
}
