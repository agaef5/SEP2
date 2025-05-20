package server.networking.socketHandling;

import client.ui.util.ErrorHandler;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import server.model.Bet;
import server.services.bet.BetService;
import server.services.bet.BetServiceImpl;
import server.util.DTOMapper;
import shared.DTO.BetDTO;
import shared.DTO.BetResponseDTO;
import shared.DTO.RaceDTO;
import shared.DTO.UserDTO;
import shared.bet.CreateBetRequest;
import shared.bet.CreateBetResponse;
import shared.bet.GetBetHistoryOfUserRequest;

import java.sql.SQLException;

public class BetHandler extends BaseRequestHandler {
    private final BetService betService;
    private final Gson gson;

    public BetHandler()
    {
        this.betService = BetServiceImpl.getInstance();
        this.gson= new Gson();
    }

    @Override
    public Object safeHandle(String action , JsonElement payload) throws SQLException
    {
        switch(action){
            case "createBet"->{
                CreateBetRequest request = parsePayload(payload,CreateBetRequest.class);
                return handleCreateBetRequest(request);
            }
            case "getBetListByUser" ->{
                GetBetHistoryOfUserRequest request = parsePayload(payload,GetBetHistoryOfUserRequest.class);
                return handleGetBetHistoryOfUser(request);

            }
            default -> throw new IllegalArgumentException("Invalid action "+action);
        }

    }


    private Object handleCreateBetRequest(CreateBetRequest request) throws SQLException {
        // Get the bet from the service
        Bet bet = betService.createBet(
                request.username(),
                request.horseDTO(),
                request.amount()
        );

        // Convert directly to the response DTO
        BetResponseDTO responseDTO = DTOMapper.betToResponseDTO(bet);

        // Return the response
        return new CreateBetResponse(responseDTO);
    }

    private Object handleGetBetHistoryOfUser(GetBetHistoryOfUserRequest request)
            throws SQLException {
        return betService.getBetsByUser(request.username());
    }

}
