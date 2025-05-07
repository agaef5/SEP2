package server.services.races;

import server.model.Race;
import server.model.RaceTrack;

import java.util.Date;
import java.util.List;

public interface RacesService
{
   Race createRace(String name,int raceCapacity, RaceTrack raceTrack);
  List getRaceList();
  List getRaceTracks();
}
