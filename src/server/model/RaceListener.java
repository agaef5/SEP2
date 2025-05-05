package server.model;

public interface RaceListener
{
  void onRacerFinished(Horse horse, int position);
  void onRaceStarted(String raceName);
  void onRaceFinished(String raceName, HorseList finalPositions);
}
