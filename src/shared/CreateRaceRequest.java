package shared;

import server.model.RaceTrack;

import java.util.Date;

public record CreateRaceRequest(String name, Date startTime, RaceTrack raceTrack) {
}


