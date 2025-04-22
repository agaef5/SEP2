package server.services.racerList;

import server.model.RacerList;
import shared.HorseListRequest;
import shared.HorseListResponse;

public interface RacerListService {
  public HorseListResponse getHorsesList();
}
