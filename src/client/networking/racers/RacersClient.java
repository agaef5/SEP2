package client.networking.racers;

import client.ui.MessageListener;
import client.ui.racerList.adminView.CreateEditRacerVM;
import server.model.Horse;
import shared.RacerRequest;

import java.util.List;

public interface RacersClient
{
  void getRacerList();
  void getRacer(RacerRequest racerRequest);
  void addListener(MessageListener listener);
}
