package server.persistence.raceRepository.raceTrack;

import server.model.RaceTrack;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for handling database operations related to {@link RaceTrack}.
 */
public interface RaceTrackRep {

    /**
     * Creates a new race track entry in the database.
     *
     * @param name     the name of the race track
     * @param length   the length of the race track
     * @param location the location of the race track
     * @return the created {@code RaceTrack} object
     * @throws SQLException if the database operation fails
     */
    RaceTrack create(String name, int length, String location) throws SQLException;

    /**
     * Retrieves all race tracks from the database.
     *
     * @return a list of all {@code RaceTrack} entries
     * @throws SQLException if the database operation fails
     */
    List<RaceTrack> getAll() throws SQLException;

    /**
     * Retrieves all race tracks that match the given name.
     *
     * @param name the name to search for
     * @return a list of {@code RaceTrack} entries with the specified name
     * @throws SQLException if the database operation fails
     */
    List<RaceTrack> readByName(String name) throws SQLException;

    /**
     * Retrieves a race track by its length.
     *
     * @param length the length to search for
     * @return the {@code RaceTrack} with the given length
     * @throws SQLException if the database operation fails
     */
    RaceTrack readByLength(int length) throws SQLException;

    /**
     * Retrieves a race track by its location.
     *
     * @param location the location to search for
     * @return the {@code RaceTrack} at the specified location
     * @throws SQLException if the database operation fails
     */
    RaceTrack readByLocation(String location) throws SQLException;

    /**
     * Updates an existing race track entry in the database.
     *
     * @param raceTrack the {@code RaceTrack} to update
     * @throws SQLException if the database operation fails
     */
    void updateRaceTrack(RaceTrack raceTrack) throws SQLException; // Hernoemd van updateRacer naar updateRaceTrack

    /**
     * Deletes a race track entry from the database.
     *
     * @param raceTrack the {@code RaceTrack} to delete
     * @throws SQLException if the database operation fails
     */
    void delete(RaceTrack raceTrack) throws SQLException;
}