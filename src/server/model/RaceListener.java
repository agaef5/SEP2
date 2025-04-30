package server.model;

import java.util.List;

public interface RaceListener
{
  void onRacerFinished(Racer racer, int position);
  void onRaceStarted(String raceName);
  void onRaceFinished(String raceName, List<Racer> finalPositions);
}
