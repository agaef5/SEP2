package client.networking.racers;

import client.networking.SocketService;
import server.model.Horse;

import java.util.List;

public class SocketRacersClient implements RacersClient
{
  private SocketService socketService;

  @Override public List<Horse> getHorseList()
  {
    return List.of();
  }
}
