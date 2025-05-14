package client.networking.bet;

import shared.bet.CreateBetRequest;
import shared.bet.GetBetHistoryOfUserRequest;

/**
 * Interface for communication with the server regarding bet-related operations.
 * Defines methods for creating a bet and retrieving a user’s bet history.
 */
public interface BetClient {

    /**
     * Sends a request to the server to place a new bet.
     *
     * @param request  the data needed to create the bet (username, HorseDTO, amount)
     */
    void createBet(CreateBetRequest request);

    /**
     * Sends a request to the server to fetch all bets placed by a given user.
     *
     * @param request  the request containing the username whose bets to retrieve
     */
    void getBetListByUser(GetBetHistoryOfUserRequest request);
}
