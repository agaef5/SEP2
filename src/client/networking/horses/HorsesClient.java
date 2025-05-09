package client.networking.horses;

import shared.horse.CreateHorseRequest;
import shared.DTO.HorseDTO;
import shared.horse.HorseRequest;

/**
 * Interface for communication with the server regarding horse-related operations.
 * It defines methods for retrieving, creating, updating, and deleting horses.
 */
public interface HorsesClient {

  /**
   * Sends a request to retrieve the full list of horses from the server.
   */
  void getHorseList();

  /**
   * Sends a request to retrieve a specific horse based on the provided request.
   *
   * @param racerRequest the request containing information to identify the horse
   */
  void getHorse(HorseRequest racerRequest);

  /**
   * Sends a request to delete the specified horse.
   *
   * @param selectedRacer the horse to be deleted
   */
  void deleteHorse(HorseDTO selectedRacer);

  /**
   * Sends a request to update the specified horse.
   *
   * @param selectedRacer the horse to be updated
   */
  void updateHorse(HorseDTO selectedRacer);

  /**
   * Sends a request to create a new horse using the provided information.
   *
   * @param createRacerRequest the request containing data for the new horse
   */
  void createHorse(CreateHorseRequest createRacerRequest);
}
