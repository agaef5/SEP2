package server.services.races;

import server.model.Horse;
import server.model.Race;
import server.model.RaceManager;
import server.model.RaceTrack;
import server.persistence.raceRepository.raceTrack.RaceTrackRepImpl;
import server.validation.baseValidation.BaseVal;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;
import shared.DTO.HorseDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RaceServiceImpl implements RacesService {


  @Override
  public RaceDTO createRace(String name, RaceTrackDTO raceTrackDTO,Integer capacity) {
    if (BaseVal.validate(name)) {
      throw new IllegalArgumentException("Cannot create new race. Name is empty.");
    }

    //TODO should these 2 be in here? They never return null
    if (raceTrackDTO == null) {
      throw new IllegalArgumentException("Start time cannot be null.");
    }

    if (raceTrackDTO == null) {
      throw new IllegalArgumentException("Race track cannot be null.");
    }

    try {
      RaceTrack raceTrack = fromDTO(raceTrackDTO);
      Race race = new Race(name, raceTrack,capacity);
      RaceManager.getInstance().addRace(race); // assuming this adds it to in-memory list
      return toDTO(race);
    } catch (SQLException e)
    { System.err.println("Database error when creating race: " + e.getMessage());
      throw new RuntimeException("Failed to create race", e);
    }
  }

  @Override
  public List<RaceDTO> getRaceList() {
    List<Race> raceList = RaceManager.getInstance().getAllRaces();
    List<RaceDTO> dtoList = new ArrayList<>();
    for (Race race : raceList) {
      dtoList.add(toDTO(race));
    }
    return dtoList;
  }

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

  private RaceDTO toDTO(Race race) {
    // Convert RaceTrack and HorseList to appropriate DTOs
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
            toDTO(race.getRaceTrack())
    );
  }




  private RaceTrackDTO toDTO(RaceTrack track) {
    return new RaceTrackDTO(track.getName(), track.getLength(), track.getLocation());
  }

  private RaceTrack fromDTO(RaceTrackDTO dto) {
    return new RaceTrack(dto.name(), dto.length(), dto.location());
  }
}
