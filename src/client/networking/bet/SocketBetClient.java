package client.networking.bet;

import client.networking.SocketService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import shared.Request;
import shared.bet.CreateBetRequest;
import shared.bet.GetBetHistoryOfUserRequest;

/**
 * Implementation of the {@link BetClient} interface using socket communication.
 * Sends create-bet and get-bet-history requests to the server.
 */
public class SocketBetClient implements BetClient {

    private final SocketService socketService;
    private final Gson          gson;

    public SocketBetClient(SocketService socketService) {
        this.socketService = socketService;
        this.gson          = new Gson();
    }

    /**
     * Sends a request to place a new bet.
     *
     * @param request  contains username, HorseDTO, and amount
     */
    @Override
    public void createBet(CreateBetRequest request) {
        JsonElement payload = gson.toJsonTree(request);
        Request req = new Request("bet", "createBet", payload);
        socketService.sendRequest(req);
    }

    /**
     * Sends a request to fetch all bets for a given user.
     *
     * @param request  contains the username
     */
    @Override
    public void getBetListByUser(GetBetHistoryOfUserRequest request) {
        JsonElement payload = gson.toJsonTree(request);
        Request req = new Request("bet", "getBetListByUser", payload);
        socketService.sendRequest(req);
    }
}
