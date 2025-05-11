package shared.updates;

import shared.DTO.HorseDTO;

public record OnHorseFinished(HorseDTO horseDTO, int position) {
}
