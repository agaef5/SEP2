package server.services.racerList;

import server.model.Racer;
import shared.RacerListResponse;
import shared.RacerResponse;

public interface RacerListService {
  RacerListResponse getRacerList(String racerType);
  RacerResponse getRacer(String type, int id);
  Racer createRacer(Racer racer);
}
