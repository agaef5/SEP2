package shared.bet;

import shared.DTO.BetDTO;
import shared.DTO.BetResponseDTO;

// Change the type of the wrapped object
public record CreateBetResponse(BetResponseDTO BetDTO) {
}
