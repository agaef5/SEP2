package server.services.racerList;

import server.model.RacerList;
import shared.HorseListRequest;
import shared.HorseListResponse;
import shared.RacerResponse;

public interface RacerListService {
  HorseListResponse getRacerList();
  RacerResponse getRacer(String type, int id);
}
