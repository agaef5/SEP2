package shared;

import server.model.RaceTrack;
import shared.DTO.RaceTrackDTO;

import java.util.List;

public record GetRaceTrackResponse(List<RaceTrackDTO> raceTracks)
{
}
