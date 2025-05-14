package shared.bet;

import shared.DTO.BetDTO;

import java.util.List;

public record GetBetHistoryOfUserRespond(List<BetDTO> betDTOList) {
}
