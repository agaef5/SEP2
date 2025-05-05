package server.services.races;

import server.model.Race;
import server.model.RaceTrack;
import server.validation.baseValidation.BaseVal;

import java.sql.SQLException;
import java.util.Date;

public class RaceServiceImpl implements RacesService
{

  @Override public Race createRace(String name, long startTime,
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
      Race race = new Race(name, startTime, raceCapacity, raceTrack);
      return race;
    }
    catch (SQLException e)
    {
      System.err.println("Database error when creating racer: " + e.getMessage());
      throw new RuntimeException("Failed to create race", e);
    }

  }

  @Override public Race getRace()
  {
    //TODO where are we storing ongoing race,
    return null;
  }
}
