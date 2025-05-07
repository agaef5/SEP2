package server.services.races;

import server.model.Race;
import server.model.RaceManager;
import server.model.RaceTrack;
import server.validation.baseValidation.BaseVal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The {@code RaceServiceImpl} class implements the {@link RacesService} interface.
 * It provides methods for creating races, retrieving the list of races, and retrieving race tracks.
 */
public class RaceServiceImpl implements RacesService
{

  /**
   * Creates a new race with the specified parameters.
   *
   * @param name The name of the race.
   * @param raceCapacity The capacity of the race (number of participants).
   * @param raceTrack The race track where the race will take place.
   * @return The created {@link Race} object.
   * @throws IllegalArgumentException If the input data is invalid (e.g., empty name, non-positive capacity).
   * @throws RuntimeException If a database error occurs while creating the race.
   */
  @Override public Race createRace(String name,
      int raceCapacity, RaceTrack raceTrack)
  // Validate the input for creating race, if valid create a race and return it
  // TODO: plus add the Race to Database
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

  /**
   * Retrieves the list of all races.
   *
   * @return A list of all {@link Race} objects.
   */
  @Override public List<Race> getRaceList()
  {
    List<Race> raceList = RaceManager.getInstance().getAllRaces();
    return raceList;
  }

  /**
   * Retrieves the list of all race tracks.
   *
   * @return A list of all {@link RaceTrack} objects.
   * @throws RuntimeException If the race tracks cannot be fetched (e.g., database error).
   */
  @Override public List<RaceTrack> getRaceTracks()
  {
    // TODO: call method in persistence layer to get all RaceTracks
    return null;
  }
}
