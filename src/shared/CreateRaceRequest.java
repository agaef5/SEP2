package shared;

import server.model.RaceTrack;
import shared.DTO.RaceTrackDTO;

import java.util.Date;

public record CreateRaceRequest(String name, RaceTrackDTO raceTrack, int capacity)
{
}

