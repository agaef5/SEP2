package server.services.races;

import server.model.Race;
import server.model.RaceTrack;

import java.util.Date;
import java.util.List;

public interface RacesService
{
   Race createRace(String name, long startTime,int raceCapacity, RaceTrack raceTrack);
  List getRaceList();
  List getRaceTracks();
}
