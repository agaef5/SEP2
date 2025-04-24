package client.networking.racers;

import client.ui.MessageListener;
import client.ui.racerList.adminView.CreateEditRacerVM;
import server.model.Horse;
import server.model.Racer;
import shared.CreateRacerRequest;
import shared.RacerRequest;

import java.util.List;

public interface RacersClient
{
  void getRacerList();
  void getRacer(RacerRequest racerRequest);
//  void addListener(MessageListener listener);
  void deleteRacer(Racer selectedRacer);
  void updateRacer(Racer selectedRacer);
  void createRacer(CreateRacerRequest createRacerRequest);
}
