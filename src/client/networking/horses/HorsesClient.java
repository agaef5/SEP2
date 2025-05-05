package client.networking.horses;

import server.model.Horse;
import shared.CreateHorseRequest;
import shared.HorseRequest;

public interface HorsesClient
{
  void getHorseList();
  void getHorse(HorseRequest racerRequest);
//  void addListener(MessageListener listener);
  void deleteHorse(Horse selectedRacer);
  void updateHorse(Horse selectedRacer);
  void createHorse(CreateHorseRequest createRacerRequest);
}
