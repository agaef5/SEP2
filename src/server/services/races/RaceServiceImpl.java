package server.services.races;

import server.model.Race;
import server.model.RaceManager;
import server.model.RaceTrack;
import server.persistence.raceRepository.raceTrack.RaceTrackRepImpl;
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
   * @param startTime The start time of the race.
   * @param raceTrack The race track where the race will take place.
   * @return The created {@link Race} object.
   * @throws IllegalArgumentException If the input data is invalid (e.g., empty name).
   * @throws RuntimeException If a database error occurs while creating the race.
   */
  @Override public Race createRace(String name,
                                   Date startTime, RaceTrack raceTrack)
  // Validate the input for creating race, if valid create a race and return it
  // TODO: plus add the Race to Database
  {
    if (BaseVal.validate(name))
    {
      throw new IllegalArgumentException("Cannot create new racer. Arguments are empty.");
    }

    // Valideer startTime indien nodig
    if (startTime == null)
    {
      throw new IllegalArgumentException("Start time cannot be null.");
    }

    // Valideer raceTrack indien nodig
    if (raceTrack == null)
    {
      throw new IllegalArgumentException("Race track cannot be null.");
    }

    try
    {
      Race race = new Race(name, startTime, raceTrack);
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
  @Override public List<RaceTrack> getRaceTracks() {
    try {
      return RaceTrackRepImpl.getInstance().getAll();
    } catch (SQLException e) {
      System.err.println("Database error when fetching race tracks: " + e.getMessage());
      throw new RuntimeException("Failed to fetch race tracks", e);
    }
  }
}