package server.services.horseList;

import server.model.Horse;
import server.persistence.horses.HorseRepository;
import server.persistence.horses.HorseRepositoryImpl;
import server.validation.baseValidation.BaseVal;
import shared.DTO.HorseDTO;
import shared.horse.HorseListResponse;
import shared.horse.HorseResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class HorseListServiceImpl implements HorseListService {

  private final HorseRepository horseRepository;

    {
        try {
            horseRepository = HorseRepositoryImpl.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
  public HorseListResponse getHorseList() {
    try {
      List<Horse> horses = horseRepository.readAll();
      List<HorseDTO> horseDTOs = horses.stream()
              .map(this::toDTO)
              .collect(Collectors.toList());
      return new HorseListResponse(horseDTOs);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to fetch horse list", e);
    }
  }

  @Override
  public HorseResponse getHorse(int id) {
    if (id < 0) throw new IllegalArgumentException("Incorrect id");
    try {
      Horse horse = horseRepository.readByID(id);
      return new HorseResponse(toDTO(horse));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to fetch horse", e);
    }
  }

  @Override
  public HorseDTO createHorse(String horseName, int speedMin, int speedMax) {
    if (BaseVal.validate(horseName))
      throw new IllegalArgumentException("Cannot create new horse. Name is empty.");
    if (speedMin >= speedMax)
      throw new IllegalArgumentException("SpeedMin cannot be greater than or equal to SpeedMax");

    try {
      Horse horse = horseRepository.create(horseName, speedMin, speedMax);
      return toDTO(horse);
    } catch (SQLException e) {
      throw new RuntimeException("Failed to create horse", e);
    }
  }

  @Override
  public HorseDTO updateHorse(HorseDTO dto) {
    if (dto == null) throw new IllegalArgumentException("No horse to update");

    try {
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      horseRepository.updateHorse(fromDTO(dto)); // This is void
      Horse updated = horseRepository.readByID(dto.id()); // Now fetch the updated entity
      return toDTO(updated);
    } catch (SQLException e) {
      System.err.println("Database error when updating horse: " + e.getMessage());
      throw new RuntimeException("Failed to update horse", e);
    }
  }


  @Override
  public String removeHorse(HorseDTO dto) {
    if (dto == null)
      throw new IllegalArgumentException("No horse to remove");
    try {
      horseRepository.delete(fromDTO(dto));
      return "success";
    } catch (SQLException e) {
      throw new RuntimeException("Failed to delete horse", e);
    }
  }

  // Helpers to convert between domain and DTO
  private HorseDTO toDTO(Horse horse) {
    return new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax(), horse.getPosition());
  }

  private Horse fromDTO(HorseDTO dto) {
    return new Horse(dto.id(), dto.name(), dto.speedMin(), dto.speedMax());
  }
}
