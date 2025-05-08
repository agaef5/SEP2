package shared;

import server.model.Race;
import shared.DTO.RaceDTO;

import java.util.ArrayList;
import java.util.List;

public record GetRaceListResponse(List<RaceDTO> races)
{
}
