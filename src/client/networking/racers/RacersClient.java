package client.networking.racers;

import server.model.Horse;
import shared.RacerRequest;

import java.util.List;

public interface RacersClient
{

  List<Horse> getHorseList();
  void getRacerList();
  void getRacer(RacerRequest racerRequest);
}
