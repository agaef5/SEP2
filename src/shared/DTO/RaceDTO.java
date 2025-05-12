package shared.DTO;

import java.sql.Timestamp;
import java.util.List;

public record RaceDTO(String name, Timestamp dateTime, List<HorseDTO> horses,List<HorseDTO> finalPosition, RaceTrackDTO raceTrack) {
}
