package server.services.horseList;

import server.model.Horse;
import shared.HorseListResponse;
import shared.HorseResponse;

public interface HorseListService
{
  HorseListResponse getHorseList();
  HorseResponse getHorse( int id);
  Horse createHorse(String horseName, int speedMin, int speedMax);
}
