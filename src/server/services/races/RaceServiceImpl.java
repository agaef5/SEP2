package server.services.races;

import server.model.Horse;
import server.model.Race;
import server.model.RaceManager;
import server.model.RaceTrack;
import server.persistence.raceRepository.raceTrack.RaceTrackRepImpl;
import server.validation.baseValidation.BaseVal;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.DTO.RaceTrackDTO;
import shared.DTO.HorseDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link RacesService} interface.
 * Handles race creation, retrieval of race lists, and available race tracks.
 */
public class RaceServiceImpl implements RacesService {

  /**
   * Creates a new race with the provided name, track, and capacity.
   *
   * @param name         the name of the race
   * @param raceTrackDTO the track where the race will take place
   * @param capacity     the number of horses allowed in the race
   * @return a {@link RaceDTO} representing the newly created race
   * @throws IllegalArgumentException if name is empty or raceTrackDTO is null
   * @throws RuntimeException         if a database error occurs during creation
   */
  @Override
  public RaceDTO createRace(String name, RaceTrackDTO raceTrackDTO, Integer capacity) {
    if (BaseVal.validate(name)) {
      throw new IllegalArgumentException("Cannot create new race. Name is empty.");
    }

    if (raceTrackDTO == null) {
      throw new IllegalArgumentException("Race track cannot be null.");
    }

    try {
      RaceTrack raceTrack = fromDTO(raceTrackDTO);
      Race race = new Race(name, raceTrack, capacity);
      RaceManager.getInstance().addRace(race);
      return toDTO(race);
    } catch (SQLException e) {
      System.err.println("Database error when creating race: " + e.getMessage());
      throw new RuntimeException("Failed to create race", e);
    }
  }

  /**
   * Retrieves a list of all races currently managed.
   *
   * @return a list of {@link RaceDTO} representing all active races
   */
  @Override
  public List<RaceDTO> getRaceList() {
    List<Race> raceList = RaceManager.getInstance().getAllRaces();
    List<RaceDTO> dtoList = new ArrayList<>();

    for (Race race : raceList) {
      dtoList.add(toDTO(race));
    }
    return dtoList;
  }

  /**
   * Retrieves a list of all available race tracks from the database.
   *
   * @return a list of {@link RaceTrackDTO} representing all race tracks
   * @throws RuntimeException if a database error occurs during retrieval
   */
  @Override
  public List<RaceTrackDTO> getRaceTracks() {
    try {
      List<RaceTrack> tracks = RaceTrackRepImpl.getInstance().getAll();
      List<RaceTrackDTO> dtos = new ArrayList<>();
      for (RaceTrack track : tracks) {
        dtos.add(toDTO(track));
      }
      return dtos;
    } catch (SQLException e) {
      System.err.println("Database error when fetching race tracks: " + e.getMessage());
      throw new RuntimeException("Failed to fetch race tracks", e);
    }
  }

  // --- DTO conversion methods ---

  /**
   * Converts a {@link Race} to a {@link RaceDTO}.
   *
   * @param race the race to convert
   * @return the converted DTO
   */
  private RaceDTO toDTO(Race race) {
    List<HorseDTO> horseDTOs = new ArrayList<>();
    for (Horse horse : race.getHorseList().getList()) {
      horseDTOs.add(new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax()));
    }

    List<HorseDTO> finalPositionDTOs = new ArrayList<>();
    for (Horse horse : race.getFinalPositionlist().getList()) {
      finalPositionDTOs.add(new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax()));
    }

    return new RaceDTO(
            race.getName(),
            race.getDateTime(),
            horseDTOs,
            toDTO(race.getRaceTrack()),
            RaceState.NOT_STARTED);
  }

  /**
   * Converts a {@link RaceTrack} to a {@link RaceTrackDTO}.
   *
   * @param track the track to convert
   * @return the converted DTO
   */
  private RaceTrackDTO toDTO(RaceTrack track) {
    return new RaceTrackDTO(track.getName(), track.getLength(), track.getLocation());
  }

  /**
   * Converts a {@link RaceTrackDTO} to a {@link RaceTrack}.
   *
   * @param dto the DTO to convert
   * @return the domain model
   */
  private RaceTrack fromDTO(RaceTrackDTO dto) {
    return new RaceTrack(dto.name(), dto.length(), dto.location());
  }
}
