package server.persistence.raceRepository;

import server.model.Race;
import server.model.RaceTrack;
import shared.DTO.RaceDTO;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * The {@link RaceRepository} interface defines the contract for a repository responsible for managing {@link Race} entities.
 * This includes CRUD (Create, Read, Update, Delete) operations for races in a data store.
 * The repository provides methods to interact with race data, such as creating a new race, retrieving races by various criteria,
 * updating race details, and deleting races.
 */
public interface RaceRepository {

    /**
     * Creates a new {@link Race} entry in the repository.
     *
     * @param name      the name of the race
     *
     * @param time      the scheduled start time of the race
     * @param raceTrack the {@link RaceTrack} associated with the race
     * @return the created {@link Race} object
     * @throws SQLException if there is an error during the database operation
     */
    Race create(String name, Timestamp time, RaceTrack raceTrack) throws SQLException;

    Race save(Race race) throws SQLException;
    /**
     * Retrieves a race by its unique identifier.
     *
     * @param id the unique identifier of the race
     * @return the {@link Race} object matching the given ID, or {@code null} if not found
     * @throws SQLException if there is an error during the database operation
     */
    RaceDTO readByID(int id) throws SQLException;

    /**
     * Retrieves a list of races whose names match the specified search criteria.
     *
     * @param name the name (or part of the name) to search for
     * @return a list of {@link Race} objects that match the search criteria
     * @throws SQLException if there is an error during the database operation
     */
    List<RaceDTO> readByName(String name) throws SQLException;

    /**
     * Retrieves all races from the repository.
     *
     * @return a list of all {@link Race} objects in the repository
     * @throws SQLException if there is an error during the database operation
     */
    List<RaceDTO> getAll() throws SQLException;

    /**
     * Retrieves a race by its scheduled start time.
     *
     * @param time the start time of the race
     * @return the {@link Race} object scheduled for the given time, or {@code null} if not found
     * @throws SQLException if there is an error during the database operation
     */
    Race readByTime(Timestamp time) throws SQLException;

    /**
     * Updates an existing race in the repository.
     *
     * @param race the {@link Race} object with updated details
     * @throws SQLException if there is an error during the database operation
     */
    void updateRace(Race race) throws SQLException;

    /**
     * Deletes a race from the repository.
     *
     * @param race the {@link Race} object to be deleted
     * @throws SQLException if there is an error during the database operation
     */
    void delete(Race race) throws SQLException;
}
