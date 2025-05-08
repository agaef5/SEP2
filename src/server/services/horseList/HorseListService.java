package server.services.horseList;

import shared.DTO.HorseDTO;
import shared.HorseListResponse;
import shared.HorseResponse;

/**
 * The {@code HorseListService} interface defines methods for managing the list of horses
 * in the system. It includes functionality for retrieving, creating, updating, and removing horses.
 */
public interface HorseListService
{
  /**
   * Retrieves the list of all horses.
   *
   * @return A {@link HorseListResponse} containing the list of all horses.
   */
  HorseListResponse getHorseList();

  /**
   * Retrieves a specific horse by its ID.
   *
   * @param id The ID of the horse to be retrieved.
   * @return A {@link HorseResponse} containing the details of the specified horse.
   */
  HorseResponse getHorse(int id);

  /**
   * Creates a new horse with the given details.
   *
   * @param horseName The name of the horse.
   * @param speedMin The minimum speed of the horse.
   * @param speedMax The maximum speed of the horse.
   * @return The created {@link HorseDTO} object.
   */
  HorseDTO createHorse(String horseName, int speedMin, int speedMax);

  /**
   * Updates the details of an existing horse.
   *
   * @param horse The horse object containing the updated details.
   * @return The updated {@link HorseDTO} object.
   */
  HorseDTO updateHorse(HorseDTO horse);

  /**
   * Removes a horse from the system.
   *
   * @param horse The horse to be removed.
   * @return A string message indicating the result of the removal.
   */
  String removeHorse(HorseDTO horse);
}
