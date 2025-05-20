package server.services.bet;

import server.model.Bet;
import shared.DTO.BetDTO;
import shared.DTO.HorseDTO;

import java.util.List;

public interface BetService {

    /**
     * Persist any Bet (e.g. if you settle it outside of createBet).
     */
    void saveBet(Bet bet);


    Bet createBet(String username, HorseDTO horseDTO, int amount);

    /**
     * Fetch all bets ever placed by a given user. Wraps SQLExceptions similarly.
     */
    List<BetDTO> getBetsByUser(String username);
}
