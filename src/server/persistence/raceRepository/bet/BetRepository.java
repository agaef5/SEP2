package server.persistence.raceRepository.bet;

import server.model.Bet;
import shared.DTO.BetDTO;

import java.sql.SQLException;

public interface BetRepository
{
    Bet create(int betAmount, boolean status) throws SQLException;
    Bet save(Bet bet) throws SQLException;
    BetDTO readByUsername(String username) throws SQLException;
    BetDTO readByRace (String raceName) throws SQLException;
    BetDTO readByStatus (boolean status) throws SQLException;
    BetDTO readByRaceTrack (String rtName) throws SQLException;
    void updateBet(Bet bet) throws SQLException;
    void deleteBet (Bet bet) throws SQLException;
}
