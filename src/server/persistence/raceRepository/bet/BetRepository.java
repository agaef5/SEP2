package server.persistence.raceRepository.bet;

import server.model.Bet;
import server.model.User;
import shared.DTO.BetDTO;

import java.sql.SQLException;

public interface BetRepository
{
    /**
     * Creates a Bet object from the database if one matches the specified amount and status.
     *
     * @param betAmount the amount of the bet
     * @param status    the status of the bet
     * @return a Bet object if found, otherwise null
     * @throws SQLException if a database error occurs
     */
    Bet create(int betAmount, boolean status) throws SQLException;

    /**
     * Saves a bet to the database.
     *
     * @param bet the Bet object to save
     * @return the saved Bet
     * @throws SQLException if a database error occurs
     */
    Bet save(Bet bet) throws SQLException;

    /**
     * Reads a bet by the username of the user.
     *
     * @param username the username to search by
     * @return a BetDTO if found
     * @throws SQLException if a database error occurs
     */
    BetDTO readByUsername(String username) throws SQLException;

    /**
     * Reads a bet by race name.
     *
     * @param raceName the name of the race
     * @return a BetDTO if found
     * @throws SQLException if a database error occurs
     */
    BetDTO readByRace (String raceName) throws SQLException;

    /**
     * Reads bets by their status (e.g., active/inactive).
     *
     * @param status the status of the bet
     * @return a BetDTO if found
     * @throws SQLException if a database error occurs
     */
    BetDTO readByStatus (boolean status) throws SQLException;

    /**
     * Reads bets by the name of the race track.
     *
     * @param rtName the name of the racetrack
     * @return a BetDTO if found
     * @throws SQLException if a database error occurs
     */
    BetDTO readByRaceTrack (String rtName) throws SQLException;

    /**
     * Updates the given bet in the database.
     *
     * @param bet the Bet to update
     * @throws SQLException if a database error occurs
     */
    void updateBet(Bet bet) throws SQLException;

    /**
     * Deletes the given bet from the database.
     *
     * @param bet the Bet to delete
     * @throws SQLException if a database error occurs
     */
    void deleteBet (Bet bet) throws SQLException;
}
