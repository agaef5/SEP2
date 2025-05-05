package shared;

import server.model.RaceTrack;

import java.util.Date;

public record CreateRaceRequest(String name, long startDelayMin, RaceTrack raceTrack, int capacity)
{
}
