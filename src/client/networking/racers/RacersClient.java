package client.networking.racers;

import server.model.Racer;
import shared.CreateRacerRequest;
import shared.RacerRequest;

public interface RacersClient
{
  void getRacerList();
  void getRacer(RacerRequest racerRequest);
//  void addListener(MessageListener listener);
  void deleteRacer(Racer selectedRacer);
  void updateRacer(Racer selectedRacer);
  void createRacer(CreateRacerRequest createRacerRequest);
}
