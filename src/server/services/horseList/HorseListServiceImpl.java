package server.services.horseList;

import server.model.Horse;
import server.persistence.horses.HorseRepository;
import server.persistence.horses.HorseRepositoryImpl;
import server.validation.baseValidation.BaseVal;
import shared.HorseListResponse;
import shared.HorseResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code HorseListServiceImpl} class implements the {@link HorseListService} interface.
 * It provides methods for managing the horse list, including fetching, creating, updating, and removing horses.
 */
public class HorseListServiceImpl implements HorseListService
{

  /**
   * Retrieves the list of all horses.
   *
   * @return A {@link HorseListResponse} containing the list of all horses.
   * @throws RuntimeException If a database error occurs while fetching the horse list.
   */
  @Override public HorseListResponse getHorseList()
  {
    try{
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      ArrayList<Horse> horseList = new ArrayList<>(horseRepository.readAll());
      return new HorseListResponse(horseList);
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching horse list: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse list", sqlException);
    }
  }

  /**
   * Retrieves a specific horse by its ID.
   *
   * @param id The ID of the horse to be retrieved.
   * @return A {@link HorseResponse} containing the details of the specified horse.
   * @throws IllegalArgumentException If the provided ID is invalid.
   * @throws RuntimeException If a database error occurs while fetching the horse.
   */
  @Override public HorseResponse getHorse(int id)
  {
    if(id < 0) throw new IllegalArgumentException("Incorrect id");
    try{
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      Horse horse = horseRepository.readByID(id);
      return new HorseResponse(horseRepository.readByID(id));
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse", sqlException);
    }
  }

  /**
   * Creates a new horse with the specified details.
   *
   * @param horseName The name of the horse.
   * @param speedMin The minimum speed of the horse.
   * @param speedMax The maximum speed of the horse.
   * @return The created {@link Horse} object.
   * @throws IllegalArgumentException If the input data is invalid (e.g., empty name, invalid speed range).
   * @throws RuntimeException If a database error occurs while creating the horse.
   */
  @Override public Horse createHorse(String horseName, int speedMin, int speedMax)
  {
    // Data validation
    if(BaseVal.validate(horseName)) throw new IllegalArgumentException("Cannot create new horse. Arguments are empty.");
    if(speedMin >= speedMax) throw new IllegalArgumentException("SpeedMin cannot be bigger than speedMax");
    try
    {
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      return horseRepository.create(horseName, speedMin, speedMax);
    }
    catch (SQLException sqlException)
    {
      System.err.println("Database error when creating horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to create horse", sqlException);
    }
  }

  /**
   * Updates the details of an existing horse.
   *
   * @param horse The horse object containing the updated details.
   * @return The updated {@link Horse} object.
   * @throws IllegalArgumentException If the provided horse object is null.
   * @throws RuntimeException If a database error occurs while updating the horse.
   */
  @Override public Horse updateHorse(Horse horse)
  {
    if (horse == null)
      throw new IllegalArgumentException("No horse to update");
    try
    {
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      horseRepository.updateHorse(horse);
      return horseRepository.readByID(horse.getId());
    }
    catch (SQLException sqlException)
    {
      System.err.println("Database error when fetching horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse", sqlException);
    }
  }

  /**
   * Removes a horse from the system.
   *
   * @param horse The horse to be removed.
   * @return A message indicating the success of the removal.
   * @throws IllegalArgumentException If the provided horse object is null.
   * @throws RuntimeException If a database error occurs while removing the horse.
   */
  @Override public String removeHorse(Horse horse)
  {
    if (horse == null)
      throw new IllegalArgumentException("No horse to remove");
    try{
      HorseRepository horseRepository = HorseRepositoryImpl.getInstance();
      horseRepository.delete(horse);
      return "success";
    }catch (SQLException sqlException){
      System.err.println("Database error when fetching horse: " + sqlException.getMessage());
      throw new RuntimeException("Failed to fetch horse", sqlException);
    }
  }

}
