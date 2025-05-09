package shared.updates;

import shared.DTO.HorseDTO;

import java.util.List;

public record OnRaceFinished(String raceName, List<HorseDTO> finalPositionsDTO) {
}
