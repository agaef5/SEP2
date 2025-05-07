package shared;

import server.model.RaceTrack;

import java.util.List;

public record GetRaceTrackResponse(List<RaceTrack> raceTracks)
{
}
