package server.services.races;

import server.model.Race;
import server.model.RaceTrack;

import java.util.Date;

public interface RacesService
{
   Race createRace(String name, long startTime,int raceCapacity, RaceTrack raceTrack);
  Race getRace();
}
