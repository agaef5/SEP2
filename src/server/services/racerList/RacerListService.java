package server.services.racerList;

import shared.RacerListResponse;
import shared.RacerResponse;

public interface RacerListService {
  RacerListResponse getRacerList(String racerType);
  RacerResponse getRacer(String type, int id);
}
