package server.services.bet;

import server.model.Bet;
import shared.DTO.BetDTO;
import shared.DTO.HorseDTO;

import java.util.List;

public interface BetService {


    void saveBet(Bet bet);
    Bet createBet(String username, HorseDTO horseDTO, int amount);
    List<BetDTO> getBetsByUser(String username);
}
