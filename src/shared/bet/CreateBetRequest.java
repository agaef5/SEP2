package shared.bet;

import shared.DTO.HorseDTO;

public record CreateBetRequest(String username, HorseDTO horseDTO, int amount) {
}
