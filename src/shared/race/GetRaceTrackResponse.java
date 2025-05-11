package shared.race;

import shared.DTO.RaceTrackDTO;

import java.util.List;

public record GetRaceTrackResponse(List<RaceTrackDTO> raceTracks)
{
}
