package shared.race;

import shared.DTO.RaceDTO;

import java.util.List;

public record GetRaceListResponse(List<RaceDTO> races)
{
}
