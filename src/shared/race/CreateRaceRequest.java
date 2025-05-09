package shared.race;

import shared.DTO.RaceTrackDTO;

public record CreateRaceRequest(String name, RaceTrackDTO raceTrack, Integer capacity)
{
}

