package server.services.racerList;

import shared.RacerListResponse;
import shared.RacerResponse;

public interface RacerListService {
  RacerListResponse getRacerList();
  RacerResponse getRacer(String type, int id);
}
