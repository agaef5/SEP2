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

/**
 * Implementation of {@link HorseListService} for managing horse entities.
 * Provides methods for creating, reading, updating, and deleting horses.
 */
public class HorseListServiceImpl implements HorseListService {

  private final HorseRepository horseRepository;

  {
    horseRepository = HorseRepositoryImpl.getInstance();
  }

  /**
   * Retrieves a list of all horses from the repository.
   *
   * @return {@link HorseListResponse} containing a list of {@link HorseDTO}
   * @throws RuntimeException if a database error occurs
   */
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

  /**
   * Retrieves a horse by its ID.
   *
   * @param id the ID of the horse
   * @return {@link HorseResponse} containing the horse data
   * @throws IllegalArgumentException if the ID is negative
   * @throws RuntimeException         if a database error occurs
   */
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

  /**
   * Creates a new horse and persists it to the repository.
   *
   * @param horseName the name of the horse
   * @param speedMin  minimum speed of the horse
   * @param speedMax  maximum speed of the horse
   * @return the created {@link HorseDTO}
   * @throws IllegalArgumentException if validation fails
   * @throws RuntimeException         if a database error occurs
   */
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

  /**
   * Updates an existing horse.
   *
   * @param dto the {@link HorseDTO} containing updated horse information
   * @return the updated {@link HorseDTO}
   * @throws IllegalArgumentException if DTO is null
   * @throws RuntimeException         if a database error occurs
   */
  @Override
  public HorseDTO updateHorse(HorseDTO dto) {
    if (dto == null) throw new IllegalArgumentException("No horse to update");

    try {
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      horseRepository.updateHorse(fromDTO(dto)); // update is void
      Horse updated = horseRepository.readByID(dto.id()); // fetch updated horse
      return toDTO(updated);
    } catch (SQLException e) {
      System.err.println("Database error when updating horse: " + e.getMessage());
      throw new RuntimeException("Failed to update horse", e);
    }
  }

  /**
   * Removes a horse from the repository.
   *
   * @param dto the {@link HorseDTO} representing the horse to remove
   * @return "success" if deletion succeeded
   * @throws IllegalArgumentException if the DTO is null
   * @throws RuntimeException         if a database error occurs
   */
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

  /**
   * Converts a {@link Horse} domain object to a {@link HorseDTO}.
   *
   * @param horse the horse to convert
   * @return the corresponding DTO
   */
  private HorseDTO toDTO(Horse horse) {
    return new HorseDTO(horse.getId(), horse.getName(), horse.getSpeedMin(), horse.getSpeedMax());
  }

  /**
   * Converts a {@link HorseDTO} to a {@link Horse} domain object.
   *
   * @param dto the DTO to convert
   * @return the corresponding domain object
   */
  private Horse fromDTO(HorseDTO dto) {
    return new Horse(dto.id(), dto.name(), dto.speedMin(), dto.speedMax());
  }
}
