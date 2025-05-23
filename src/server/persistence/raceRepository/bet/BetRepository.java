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
}
