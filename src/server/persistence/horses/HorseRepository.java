package server.persistence.horses;

import server.model.Horse;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for managing horse entities in a data repository.
 * Provides methods for creating, reading, updating, and deleting horse records.
 */
public interface HorseRepository {

  /**
   * Creates a new horse record in the repository.
   * @param name the name of the horse.
   * @param speedMin the minimum speed of the horse.
   * @param speedMax the maximum speed of the horse.
   * @return a {@link Horse} object representing the newly created horse.
   * @throws SQLException if an error occurs while inserting the horse record into the database.
   */
  Horse create(String name, int speedMin, int speedMax) throws SQLException;

  /**
   * Retrieves a horse by its ID from the repository.
   * @param id the ID of the horse to retrieve.
   * @return a {@link Horse} object representing the horse, or null if no horse is found.
   * @throws SQLException if an error occurs while querying the database.
   */
  Horse readByID(int id) throws SQLException;

  /**
   * Retrieves a horse by its minimum speed from the repository.
   * @param speedMin the minimum speed of the horse to retrieve.
   * @return a {@link Horse} object representing the horse, or null if no horse is found.
   * @throws SQLException if an error occurs while querying the database.
   */
  Horse readBySpeed_min(int speedMin) throws SQLException;

  /**
   * Retrieves a horse by its maximum speed from the repository.
   * @param speedMax the maximum speed of the horse to retrieve.
   * @return a {@link Horse} object representing the horse, or null if no horse is found.
   * @throws SQLException if an error occurs while querying the database.
   */
  Horse readBySpeed_max(int speedMax) throws SQLException;

  /**
   * Retrieves a list of horses that match the provided name search.
   * @param searchName the name (or partial name) of the horse to search for.
   * @return a list of {@link Horse} objects matching the search criteria.
   * @throws SQLException if an error occurs while querying the database.
   */
  List<Horse> readByName(String searchName) throws SQLException;

  /**
   * Retrieves all horses from the repository.
   * @return a list of all {@link Horse} objects.
   * @throws SQLException if an error occurs while querying the database.
   */
  List<Horse> readAll() throws SQLException;

  /**
   * Updates an existing horse record in the repository.
   * @param horse the {@link Horse} object containing updated information.
   * @throws SQLException if an error occurs while updating the horse record in the database.
   */
  void updateHorse(Horse horse) throws SQLException;

  /**
   * Deletes a horse record from the repository.
   * @param horse the {@link Horse} object to delete.
   * @throws SQLException if an error occurs while deleting the horse record from the database.
   */
  void delete(Horse horse) throws SQLException;
}
