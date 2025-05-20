package server.services.races;

import server.model.Race;
import server.model.RaceTrack;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * The {@code RacesService} interface defines the methods for managing races and race tracks.
 * It includes methods for creating races, retrieving a list of races, and getting available race tracks.
 */
public interface RacesService
{
  /**
   * Creates a new race with the specified parameters.
   *
   * @param name The name of the race.
   * @param raceTrack The race track where the race will take place.
   * @return The created {@link Race} object.
   */
  RaceDTO createRace(String name, RaceTrackDTO raceTrack,Integer capacity);

  /**
   * Retrieves the list of all races.
   *
   * @return A list of all {@link Race} objects.
   */
  List<RaceDTO> getRaceList();

  /**
   * Retrieves the list of all race tracks.
   *
   * @return A list of all {@link RaceTrack} objects.
   */
  List<RaceTrackDTO> getRaceTracks() throws SQLException;
}
