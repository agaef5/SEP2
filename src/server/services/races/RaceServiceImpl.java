package server.services.races;

import server.model.Race;
import server.model.RaceManager;
import server.model.RaceTrack;
import server.validation.baseValidation.BaseVal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RaceServiceImpl implements RacesService
{

  @Override public Race createRace(String name,
      int raceCapacity, RaceTrack raceTrack)
      //Validate the input for creating race, if valid create a race and return it
      //TODO plus add the Race to Datebase
  {
    if (BaseVal.validate(name))
    {
      throw new IllegalArgumentException("Cannot create new racer. Arguments are empty.");
    }
    if (BaseVal.valPosInt(raceCapacity))
    {
      throw new IllegalArgumentException("Capacity for Race needs to be positive number");
    }
    try
    {
      Race race = new Race(name, raceCapacity, raceTrack);
      return race;
    }
    catch (SQLException e)
    {
      System.err.println("Database error when creating racer: " + e.getMessage());
      throw new RuntimeException("Failed to create race", e);
    }

  }

  @Override public List<Race> getRaceList()
  {
    List<Race> raceList = RaceManager.getInstance().getAllRaces();
    return raceList;
  }

  @Override public List<RaceTrack> getRaceTracks()
  {
    //TODO call method in persistance layer to get all RaceTracks
    return null;
  }
}
